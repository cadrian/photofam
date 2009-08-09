/**
 * This file is part or PhotoFam.
 * 
 * PhotoFam is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.cadrian.photofam.impl.data;

import net.cadrian.photofam.Services;
import net.cadrian.photofam.exception.UnexpectedException;
import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.album.AlbumListener;
import net.cadrian.photofam.services.album.Image;
import net.cadrian.photofam.services.album.ImageFilter;
import net.cadrian.photofam.services.album.Tag;
import net.cadrian.photofam.xml.DataObject;
import net.cadrian.photofam.xml.rawalbum.AlbumData;
import net.cadrian.photofam.xml.rawalbum.AlbumDataType;
import net.cadrian.photofam.xml.rawalbum.Child;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Low-level album (underlying shared albums, and private albums)
 * 
 * @author Cyril ADRIAN
 */
public class RawAlbum implements Album, Serializable {

	private static final long serialVersionUID = 5179315224341934149L;

	private static final Set<String> extensions = new HashSet<String>();
	static {
		extensions.add("jpg");
		extensions.add("jpeg");
		extensions.add("gif");
		extensions.add("png");
	}

	private final String name;
	private final List<Album> children;
	private final List<Image> images;
	private final String owner;
	private final boolean shared;
	private transient List<AlbumListener> listeners = new ArrayList<AlbumListener>();

	/**
	 * @param a_services
	 *            the services
	 * @param a_name
	 *            the album name
	 * @param a_owner
	 *            the album owner
	 * @param a_password
	 *            the password to encrypt the album; or <code>null</code> for a shared album
	 * @param a_directory
	 *            the directory containing the images
	 */
	public RawAlbum (Services a_services, String a_name, String a_owner, String a_password, File a_directory) {
		assert a_services != null;
		assert a_name != null;
		assert a_owner != null;
		assert a_directory.isDirectory();

		name = a_name;
		owner = a_owner;
		shared = a_password == null;

		children = new ArrayList<Album>();

		try {
			Map<String, Image> allImages = new TreeMap<String, Image>();
			addImages(a_services, a_directory, a_directory.getCanonicalPath(), allImages);
			images = new ArrayList<Image>(allImages.values());
			write(a_password);
		} catch (Exception x) {
			throw new UnexpectedException(a_services, x);
		}
	}

	RawAlbum (Services services, AlbumDataType a_albumData) {
		name = a_albumData.getName();
		owner = a_albumData.getOwner();
		shared = a_albumData.getShared();

		children = new ArrayList<Album>();
		images = new ArrayList<Image>();

		for (Child child : a_albumData.getChild()) {
			children.add(new RawAlbum(services, child));
		}

		for (net.cadrian.photofam.xml.rawalbum.Image image : a_albumData.getImage()) {
			images.add(new ImageImpl(services, image));
		}
	}

	private void addImages (Services services, File dir, String rootdir, Map<String, Image> allImages) throws IOException {
		assert dir.isDirectory();

		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				addImages(services, f, rootdir, allImages);
			} else {
				String canonical = f.getCanonicalPath();
				String filename = f.getName().toLowerCase();
				int i = filename.lastIndexOf('.');
				if (i != -1) {
					String ext = filename.substring(i + 1);
					if (extensions.contains(ext)) {
						if (!allImages.containsKey(canonical)) {
							allImages.put(canonical, new ImageImpl(f));
						}
						assert canonical.startsWith(rootdir);
						String canonicalTag = cleanTag(canonical.substring(rootdir.length()));
						if (canonicalTag != null) {
							allImages.get(canonical).addTag(services.getTagService().getTag(canonicalTag));
						}
						String absolute = f.getAbsolutePath();
						if (canonical != absolute && absolute.startsWith(rootdir)) {
							// usually a symbolic link
							String absoluteTag = cleanTag(absolute.substring(rootdir.length()));
							if (absoluteTag != null) {
								allImages.get(canonical).addTag(services.getTagService().getTag(absoluteTag));
							}
						}
					}
				}
			}
		}
	}

	private String cleanTag (String imageRelativePath) {
		char[] data = imageRelativePath.toCharArray();
		int lo = 0;
		int hi = data.length;
		while (data[lo] == File.separatorChar) {
			lo++;
		}
		while (hi > lo && data[hi - 1] != File.separatorChar) {
			hi--;
		}
		while (hi > lo && data[hi - 1] == File.separatorChar) {
			hi--;
		}
		for (int i = lo; i < hi; i++) {
			if (data[i] == File.separatorChar) {
				data[i] = '/';
			}
		}
		return hi == lo ? null : new String(data, lo, hi - lo);
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public void setName (String a_name) {
		throw new RuntimeException("BUG: cannot change raw album name");
	}

	@Override
	public List<Album> getChildren () {
		return children;
	}

	@Override
	public List<Image> getImages (ImageFilter filter) {
		List<Image> result = new ArrayList<Image>(images.size());
		for (Image image : images) {
			if (filter.accept(image)) {
				result.add(image);
			}
		}
		return result;
	}

	@Override
	public String getOwner () {
		return owner;
	}

	@Override
	public boolean isShared () {
		return shared;
	}

	@Override
	public void add (Services a_services, Image a_image) {
		images.add(a_image);
	}

	@Override
	public void remove (Image a_image) {
		images.remove(a_image);
	}

	@Override
	public void addAlbumListener (AlbumListener a_albumListener) {
		listeners.add(a_albumListener);
	}

	/**
	 * @param services
	 *            the services
	 * @param name
	 *            the album name
	 * @param user
	 *            the user identifier
	 * @param password
	 *            the user password, or <code>null</code> if the album is shared (non-encrypted)
	 * @return an album known by its name
	 */
	public static RawAlbum find (Services services, String name, String user, String password) {
		try {
			return read(services, name, user, password);
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

	private static RawAlbum read (Services services, String name, String user, String password) throws Exception {
		RawAlbum result = null;
		boolean shared = password == null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File datafile = IOUtils.getAlbumFile(shared ? null : user, name);
		if (datafile.exists()) {
			InputStream in = new BufferedInputStream(new FileInputStream(datafile));
			int c = in.read();
			while (c != -1) {
				out.write(c);
				c = in.read();
			}
			byte[] data = password == null ? out.toByteArray() : IOUtils.decode(password, out.toByteArray());
			GZIPInputStream gzin = new GZIPInputStream(new ByteArrayInputStream(data));
			AlbumData albumData = DataObject.read(gzin, AlbumData.class);
			gzin.close();
			result = new RawAlbum(services, albumData);
		}
		return result;
	}

	private void write (String password) throws Exception {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		GZIPOutputStream gzout = new GZIPOutputStream(bo);
		createCastorAlbum().write(gzout);
		gzout.flush();
		gzout.close();
		byte[] encodedData = password == null ? bo.toByteArray() : IOUtils.encode(password, bo.toByteArray());
		File datafile = IOUtils.getAlbumFile(shared ? null : owner, name);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(datafile));
		for (int i = 0, n = encodedData.length; i < n; i++) {
			out.write(encodedData[i]);
		}
		out.flush();
		out.close();
	}

	private AlbumData createCastorAlbum () {
		AlbumData result = new AlbumData();
		fillCastorAlbum(result);
		return result;
	}

	private void fillCastorAlbum (AlbumDataType album) {
		album.setName(name);
		album.setOwner(owner);
		album.setShared(shared);
		for (Album child : children) {
			Child c = new Child();
			((RawAlbum) child).fillCastorAlbum(c);
			album.addChild(c);
		}
		for (Image img : images) {
			album.addImage(((ImageImpl) img).getCastorImage());
		}
	}

	@Override
	public List<Tag> getAllTags () {
		Set<Tag> result = new TreeSet<Tag>();
		for (Image i : images) {
			result.addAll(i.getTags());
		}
		return new ArrayList<Tag>(result);
	}

}
