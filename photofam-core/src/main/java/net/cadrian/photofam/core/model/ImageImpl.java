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

import net.cadrian.photofam.exception.AlbumDAOException;
import net.cadrian.photofam.model.Album;
import net.cadrian.photofam.model.Image;
import net.cadrian.photofam.model.Metadata;
import net.cadrian.photofam.model.Tag;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
class ImageImpl implements Image {

	private static final Logger log = LoggerFactory.getLogger(ImageImpl.class);

	private final AlbumImpl album;
	private final File file;
	private final Map<String, TagImpl> tags = new HashMap<String, TagImpl>();
	private final String format;
	private String name;
	private int rotation;
	private boolean visible;

	private transient WeakReference<java.awt.Image> imageCache;
	private transient SoftReference<java.awt.Image> thumbnailCache;
	private transient int thumbnailSize;

	private transient MetadataImpl metadata;

	/**
	 * @param a_album
	 *            the album
	 * @param a_file
	 *            the image file
	 */
	public ImageImpl (AlbumImpl a_album, File a_file) {
		assert a_album != null;
		assert a_file.exists() && !a_file.isDirectory();
		album = a_album;
		file = a_file;

		String filename = file.getName();
		int i = filename.lastIndexOf('.');
		if (i != -1) {
			name = filename.substring(0, i);
			format = filename.substring(i + 1).toLowerCase();
		} else {
			name = filename;
			format = null;
		}

		visible = true;
	}

	ImageImpl (AlbumImpl a_album, net.cadrian.photofam.xml.albumdata.Image a_image) {
		assert a_album != null;
		album = a_album;
		name = a_image.getName();
		format = a_image.getFormat();
		file = new File(a_image.getPath());
		if (log.isDebugEnabled()) {
			log.debug(a_image.getPath() + " --> " + file + " (" + file.exists() + ")");
		}
		for (String tag : a_image.getTag()) {
			tags.put(tag, a_album.getTag(tag));
		}
		rotation = a_image.getRotation();
		visible = a_image.getVisible();
	}

	@Override
	public Tag addTag (String a_tagName) {
		TagImpl result = tags.get(a_tagName);
		if (result == null) {
			result = album.getTag(a_tagName);
			tags.put(a_tagName, result);
			album.fireImageTagAdded(this, result);
		}
		return result;
	}

	@Override
	public void addTag (Tag a_tag) {
		assert album.getTag(a_tag.getCompleteName()) == a_tag;
		if (!tags.containsKey(a_tag.getCompleteName())) {
			tags.put(a_tag.getCompleteName(), (TagImpl) a_tag);
			album.fireImageTagAdded(this, a_tag);
		}
	}

	@Override
	public Album getAlbum () {
		return album;
	}

	@Override
	public String getFormat () {
		return format;
	}

	@Override
	public java.awt.Image getImage () {
		java.awt.Image result = imageCache == null ? null : imageCache.get();
		if (result == null) {
			try {
				result = ImageIO.read(file);
			} catch (IOException iox) {
				log.info("Could not load " + file, iox);
			}
			imageCache = result == null ? null : new WeakReference<java.awt.Image>(result);
		}
		return result;
	}

	@Override
	public java.awt.Image getThumbnail (int a_size) {
		java.awt.Image result = null;
		if (a_size == thumbnailSize) {
			result = thumbnailCache == null ? null : thumbnailCache.get();
		}
		if (result == null) {
			java.awt.Image image = getImage();

			if (image != null) {
				int imageWidth = image.getWidth(null);
				int imageHeight = image.getHeight(null);
				double scale;
				if (imageWidth > imageHeight) {
					scale = a_size / (double) imageWidth;
				} else {
					scale = a_size / (double) imageHeight;
				}
				int scaledWidth = (int) (scale * imageWidth);
				int scaledHeight = (int) (scale * imageHeight);
				result = image.getScaledInstance(scaledWidth, scaledHeight, java.awt.Image.SCALE_SMOOTH);
			}
			thumbnailCache = new SoftReference<java.awt.Image>(result);
			thumbnailSize = a_size;
		}
		return result;
	}

	@Override
	public Metadata getMetadata () {
		MetadataImpl result = metadata;
		if (result == null) {
			result = metadata = new MetadataImpl(file);
		}
		return result;
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public String getPath () {
		String result;
		try {
			result = file.getCanonicalPath();
		} catch (IOException x) {
			result = file.getAbsolutePath();
		}
		return result;
	}

	@Override
	public int getRotation () {
		return rotation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Collection<Tag> getTags () {
		// Those awful casts... :-(
		Collection<?> result = Collections.unmodifiableCollection(tags.values());
		return (Collection<Tag>) result;
	}

	@Override
	public boolean isVisible () {
		return visible;
	}

	@Override
	public void setVisible (boolean enable) {
		boolean wasVisible = visible;
		if (wasVisible != visible) {
			visible = enable;
			album.fireImageVisibilityChanged(this, wasVisible);
		}
	}

	@Override
	public void removeTag (Tag a_tag) {
		tags.remove(a_tag.getCompleteName());
		album.fireImageTagRemoved(this, a_tag);
	}

	@Override
	public void rotate (int a_angle) {
		int previousRotation = rotation;
		int angle = a_angle;
		while (angle < 0) {
			angle += 360;
		}
		if (angle != 0) {
			rotation += angle;
			album.fireImageRotated(this, previousRotation);
		}
	}

	@Override
	public void setName (String a_name) {
		assert a_name != null;

		String oldName = name;
		if (!oldName.equals(a_name)) {
			name = a_name;
			album.fireImageNameChanged(this, oldName);
		}
	}

	net.cadrian.photofam.xml.albumdata.Image getCastorImage () {
		net.cadrian.photofam.xml.albumdata.Image result = new net.cadrian.photofam.xml.albumdata.Image();
		result.setName(name);
		result.setFormat(format);
		result.setRotation(rotation);
		result.setVisible(visible);
		try {
			result.setPath(file.getCanonicalPath());
		} catch (IOException x) {
			throw new AlbumDAOException(AlbumDAOException.Key.MarshallProblem, x);
		}
		for (TagImpl tag : tags.values()) {
			result.addTag(tag.getCompleteName());
		}
		return result;
	}

	@Override
	public String toString () {
		return file.toString();
	}

}
