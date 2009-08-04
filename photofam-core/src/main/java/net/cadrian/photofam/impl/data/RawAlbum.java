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
import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.album.AlbumListener;
import net.cadrian.photofam.services.album.Image;
import net.cadrian.photofam.services.album.ImageFilter;
import net.cadrian.photofam.services.authentication.User;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Low-level album (underlying shared albums, and private albums)
 * 
 * @author Cyril ADRIAN
 */
public class RawAlbum implements Album, Serializable {

	private static final long serialVersionUID = 5179315224341934149L;

	private static final Logger log = LoggerFactory.getLogger(RawAlbum.class);

	private String name;
	private final List<Album> children;
	private final List<Image> images;
	private final User owner;
	private final boolean shared;
	private transient List<AlbumListener> listeners = new ArrayList<AlbumListener>();

	/**
	 * @param a_name
	 *            the album name
	 * @param a_owner
	 *            the album owner
	 * @param a_shared
	 *            <code>true</code> if the album is shared, <code>false</code> otherwise.
	 * @param a_directory
	 *            the directory containing the images
	 */
	public RawAlbum (String a_name, User a_owner, boolean a_shared, File a_directory) {
		assert a_name != null;
		assert a_owner != null;
		assert a_directory.isDirectory();

		name = a_name;
		owner = a_owner;
		shared = a_shared;

		children = new ArrayList<Album>();
		images = new ArrayList<Image>();

		addImages(a_directory);
	}

	private void addImages (File dir) {
		assert dir.isDirectory();

		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				addImages(f);
			} else {
				try {
					ImageIO.read(f);
					// get here only if the file contains an image
					images.add(new ImageImpl(f));
				} catch (IOException x) {
					// forget it; not an image (anyway, not in a recognized format)
				}
			}
		}
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public void setName (String a_name) {
		assert a_name != null;

		String old = name;
		name = a_name;
		for (AlbumListener l : listeners) {
			l.nameChanged(this, old);
		}
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
	public User getOwner () {
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
	 * @param name
	 *            the album name
	 * @param user
	 *            the user identifier
	 * @return an album known by its name
	 */
	public static RawAlbum findShared (String name, String user) {
		try {
			return read(name, user, null);
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

	/**
	 * @param name
	 *            the album name
	 * @param user
	 *            the user identifier
	 * @param password
	 *            the user password
	 * @return an album known by its name
	 */
	public static RawAlbum findPrivate (String name, String user, String password) {
		try {
			return read(name, user, password);
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

	private static RawAlbum read (String name, String user, String password) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File datafile = getAlbumFile(user, name);
		InputStream in = new BufferedInputStream(new FileInputStream(datafile));
		int c = in.read();
		while (c != -1) {
			out.write(c);
			c = in.read();
		}
		byte[] data = password == null ? out.toByteArray() : EncodeUtils.decode(password, out.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		RawAlbum result = (RawAlbum) ois.readObject();
		ois.close();
		return result;
	}

	private static void write (RawAlbum data, String password) throws Exception {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bo);
		oos.writeObject(data);
		oos.flush();
		oos.close();
		byte[] encodedData = password == null ? bo.toByteArray() : EncodeUtils.encode(password, bo.toByteArray());
		File datafile = getAlbumFile(data.isShared() ? null : data.getOwner().getIdentifier(), password);
		if (!datafile.getParentFile().mkdirs()) {
			String msg = "Cannot create directory " + datafile.getParent();
			log.error(msg);
			throw new IOException(msg);
		}
		OutputStream out = new BufferedOutputStream(new FileOutputStream(datafile));
		for (int i = 0, n = encodedData.length; i < n; i++) {
			out.write(encodedData[i]);
		}
		out.flush();
		out.close();
	}

	private static File getAlbumFile (String user, String name) {
		File home = new File(System.getProperty("user.home"));
		return new File(new File(new File(new File(home, ".photofam"), "albums"), user == null ? "=shared" : user), name);
	}

}
