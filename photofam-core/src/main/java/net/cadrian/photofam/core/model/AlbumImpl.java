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
package net.cadrian.photofam.core.model;

import net.cadrian.photofam.exception.AlbumException;
import net.cadrian.photofam.model.Album;
import net.cadrian.photofam.model.AlbumListener;
import net.cadrian.photofam.model.Image;
import net.cadrian.photofam.model.ImageAlbumListener;
import net.cadrian.photofam.model.ImageFilter;
import net.cadrian.photofam.model.Tag;
import net.cadrian.photofam.xml.albumdata.AlbumData;
import net.cadrian.photofam.xml.albumdata.AlbumDataType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
public class AlbumImpl implements Album {

	private static final Logger log = LoggerFactory.getLogger(AlbumImpl.class);

	private static final Set<String> extensions = new HashSet<String>();
	static {
		extensions.add("jpg");
		extensions.add("jpeg");
		extensions.add("gif");
		extensions.add("png");
	}

	private String name;
	private final transient List<AlbumListener> albumListeners = new ArrayList<AlbumListener>();
	private final transient List<ImageAlbumListener> imageAlbumListeners = new ArrayList<ImageAlbumListener>();
	private final Map<String, ImageImpl> images = new HashMap<String, ImageImpl>();
	private final Map<String, TagImpl> allTags = new TreeMap<String, TagImpl>(new Comparator<String>() {
		@Override
		public int compare (String s1, String s2) {
			int result = s1.compareToIgnoreCase(s2);
			if (result == 0) {
				result = s1.compareTo(s2);
			}
			return result;
		}
	});

	/**
	 * @param a_name
	 *            the original album name
	 */
	public AlbumImpl (String a_name) {
		assert a_name != null;

		name = a_name;
	}

	/**
	 * @param a_albumData
	 *            the XML data
	 */
	public AlbumImpl (AlbumData a_albumData) {
		name = a_albumData.getName();
		for (net.cadrian.photofam.xml.albumdata.Image image : a_albumData.getImage()) {
			ImageImpl img = new ImageImpl(this, image);
			images.put(img.getPath(), img);
		}
	}

	@Override
	public void addAlbumListener (AlbumListener a_albumListener) {
		albumListeners.add(a_albumListener);
	}

	@Override
	public void addImageAlbumListener (ImageAlbumListener a_imageAlbumListener) {
		imageAlbumListeners.add(a_imageAlbumListener);
	}

	@Override
	public void addImages (File a_directory) {
		assert a_directory.isDirectory();
		boolean added = false;
		try {
			added = addImages(a_directory, a_directory.getCanonicalPath());
		} catch (IOException x) {
			throw new AlbumException(AlbumException.Key.InvalidAlbum, name, x);
		} finally {
			if (added) {
				fireAlbumImagesAdded(this);
			}
		}
	}

	private boolean addImages (File dir, String rootdir) throws IOException {
		boolean result = false;
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				result = addImages(f, rootdir) || result;
			} else {
				String canonical = f.getCanonicalPath();
				String filename = f.getName();
				int i = filename.lastIndexOf('.');
				if (i != -1) {
					String ext = filename.substring(i + 1).toLowerCase();
					if (extensions.contains(ext)) {
						assert canonical.startsWith(rootdir);
						if (!images.containsKey(canonical)) {
							ImageImpl img = new ImageImpl(this, f);
							images.put(canonical, img);
							result = true;
							if (log.isDebugEnabled()) {
								log.debug("Added image: " + canonical);
							}
							fireAlbumImageAdded(img);
						}
						String canonicalTag = cleanTag(canonical.substring(rootdir.length()));
						if (canonicalTag != null) {
							images.get(canonical).addTag(getTag(canonicalTag));
							if (log.isDebugEnabled()) {
								log.debug("Added tag " + canonicalTag + " to image " + canonical);
							}
						}
						String absolute = f.getAbsolutePath();
						if (canonical != absolute) {
							if (absolute.startsWith(rootdir)) {
								// usually a symbolic link
								String absoluteTag = cleanTag(absolute.substring(rootdir.length()));
								if (absoluteTag != null) {
									images.get(canonical).addTag(getTag(absoluteTag));
									if (log.isDebugEnabled()) {
										log.debug("Added tag " + absoluteTag + " to image " + canonical);
									}
								}
							} else {
								log.warn("Strange absolute path: " + absolute + " does not start with " + rootdir);
							}
						}
					}
				}
			}
		}
		return result;
	}

	TagImpl getTag (String a_absoluteTag) {
		TagImpl result = allTags.get(a_absoluteTag);
		if (result == null) {
			String parentName = TagImpl.getParentTagName(a_absoluteTag);
			if (parentName == null) {
				result = new TagImpl(a_absoluteTag, null);
				if (log.isDebugEnabled()) {
					log.debug("New root tag: " + a_absoluteTag);
				}
			} else {
				TagImpl parent = getTag(parentName);
				result = new TagImpl(TagImpl.getShortName(a_absoluteTag), parent);
				if (log.isDebugEnabled()) {
					log.debug("New child tag: " + a_absoluteTag);
				}
			}
			allTags.put(a_absoluteTag, result);
		}
		return result;
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
				data[i] = TagImpl.TAG_NAME_SEPARATOR;
			}
		}
		return hi == lo ? null : new String(data, lo, hi - lo);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Tag> getAllTags () {
		// Those awful casts... :-(
		Collection<?> result = Collections.unmodifiableCollection(allTags.values());
		if (log.isDebugEnabled()) {
			log.debug("all tags: " + result);
		}
		return (Collection<Tag>) result;
	}

	@Override
	public List<Image> getImages (ImageFilter a_filter) {
		List<Image> result = new ArrayList<Image>();
		for (ImageImpl image : images.values()) {
			if (a_filter.accept(image)) {
				result.add(image);
			}
		}
		return Collections.unmodifiableList(result);
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public void setName (String a_name) {
		assert a_name != null;

		String oldName = name;
		if (!oldName.equals(a_name)) {
			name = a_name;
			fireAlbumNameChanged(oldName);
		}
	}

	/**
	 * @return the low-level album data
	 */
	public AlbumData createCastorAlbum () {
		AlbumData result = new AlbumData();
		fillCastorAlbum(result);
		return result;
	}

	private void fillCastorAlbum (AlbumDataType album) {
		album.setName(name);
		for (ImageImpl img : images.values()) {
			album.addImage(img.getCastorImage());
		}
	}

	// AlbumListener implementation

	void fireAlbumNameChanged (String a_oldName) {
		for (AlbumListener l : albumListeners) {
			l.albumNameChanged(this, a_oldName);
		}
	}

	void fireAlbumImageAdded (Image a_image) {
		for (AlbumListener l : albumListeners) {
			l.albumImageAdded(this, a_image);
		}
	}

	void fireAlbumImagesAdded (Album a_album) {
		for (AlbumListener l : albumListeners) {
			l.albumImagesAdded(a_album);
		}
	}

	void fireImageNameChanged (Image a_image, String a_oldName) {
		images.remove(a_oldName);
		images.put(a_image.getName(), (ImageImpl) a_image);
		for (ImageAlbumListener l : imageAlbumListeners) {
			l.imageNameChanged(a_image, a_oldName);
		}
	}

	void fireImageRotated (Image a_image, int a_oldRotation) {
		for (ImageAlbumListener l : imageAlbumListeners) {
			l.imageRotated(a_image, a_oldRotation);
		}
	}

	void fireImageTagAdded (Image a_image, Tag a_tag) {
		for (ImageAlbumListener l : imageAlbumListeners) {
			l.imageTagAdded(a_image, a_tag);
		}
	}

	void fireImageTagRemoved (Image a_image, Tag a_tag) {
		for (ImageAlbumListener l : imageAlbumListeners) {
			l.imageTagRemoved(a_image, a_tag);
		}
	}

	void fireImageVisibilityChanged (Image a_image, boolean a_wasVisible) {
		for (ImageAlbumListener l : imageAlbumListeners) {
			l.imageVisibilityChanged(a_image, a_wasVisible);
		}
	}

	// Comparable implementation

	@Override
	public int compareTo (Album other) {
		int result = getName().compareToIgnoreCase(other.getName());
		if (result == 0) {
			result = getName().compareTo(other.getName());
		}
		return result;
	}

	@Override
	public boolean equals (Object a_obj) {
		boolean result = false;
		if (a_obj instanceof Album) {
			result = compareTo((Album) a_obj) == 0;
		}
		return result;
	}

	@Override
	public int hashCode () {
		return getName().hashCode();
	}

	@Override
	public String toString () {
		return getName();
	}

}
