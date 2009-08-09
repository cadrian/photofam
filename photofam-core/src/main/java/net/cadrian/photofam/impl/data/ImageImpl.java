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
import net.cadrian.photofam.services.TagService;
import net.cadrian.photofam.services.album.Image;
import net.cadrian.photofam.services.album.Tag;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
public class ImageImpl implements Image, Serializable {

	private static final Logger log = LoggerFactory.getLogger(ImageImpl.class);

	private static final long serialVersionUID = -426568095022116242L;

	private final File file;
	private final String format;
	private String name;
	private final Set<Tag> tags;

	private transient java.awt.Image data;

	/**
	 * @param a_file
	 *            the image file
	 */
	public ImageImpl (File a_file) {
		assert a_file != null;

		if (log.isInfoEnabled()) {
			log.info("Loading " + a_file.getPath());
		}

		file = a_file;
		name = a_file.getName();
		int i = name.lastIndexOf('.');
		format = name.substring(i + 1);

		tags = new TreeSet<Tag>();
	}

	ImageImpl (Services services, net.cadrian.photofam.xml.rawalbum.Image a_image) {
		file = new File(a_image.getPath());
		name = a_image.getName();
		format = a_image.getFormat();

		tags = new TreeSet<Tag>();

		TagService t = services.getTagService();
		for (String tag : a_image.getTag()) {
			tags.add(t.getTag(tag));
		}
	}

	@Override
	public java.awt.Image getImage () {
		java.awt.Image result = data;
		if (result == null) {
			try {
				result = ImageIO.read(file);
			} catch (IOException iox) {
				throw new RuntimeException(iox);
			}
			data = result;
		}
		return result;
	}

	@Override
	public String getFormat () {
		return format;
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public void setName (String a_name) {
		assert a_name != null;

		name = a_name;
	}

	@Override
	public String getPath () {
		try {
			return file.getCanonicalPath();
		} catch (IOException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public List<Tag> getTags () {
		return new ArrayList<Tag>(tags);
	}

	net.cadrian.photofam.xml.rawalbum.Image getCastorImage () {
		net.cadrian.photofam.xml.rawalbum.Image result = new net.cadrian.photofam.xml.rawalbum.Image();
		result.setFormat(getFormat());
		result.setName(getName());
		result.setPath(getPath());
		for (Tag tag : tags) {
			result.addTag(tag.getCompleteName());
		}
		return result;
	}

	@Override
	public void addTag (Tag a_tag) {
		tags.add(a_tag);
	}

	@Override
	public void removeTag (Tag a_tag) {
		tags.remove(a_tag);
	}

	@Override
	public int getRotation () {
		// TODO Auto-generated method stub
		return 0;
	}

}
