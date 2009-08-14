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
package net.cadrian.photofam.model;

import java.util.Collection;

/**
 * @author Cyril ADRIAN
 */
public interface Image {

	/**
	 * @return the image album, never <code>null</code>
	 */
	Album getAlbum ();

	/**
	 * @return the image name; never <code>null</code>.
	 */
	String getName ();

	/**
	 * @param name
	 *            the new name of the image
	 */
	void setName (String name);

	/**
	 * @return the path to the image
	 */
	String getPath ();

	/**
	 * @return the tags of the image; never <code>null</code> (but may be empty).
	 */
	Collection<Tag> getTags ();

	/**
	 * @return the format of the image; never <code>null</code>.
	 */
	String getFormat ();

	/**
	 * @return the data of the image; never <code>null</code>.
	 */
	java.awt.Image getImage ();

	/**
	 * @param a_tagName
	 *            the complete name of the tag to add
	 * 
	 * @return the newly added tag
	 */
	Tag addTag (String a_tagName);

	/**
	 * @param a_tag
	 *            the tag to add
	 */
	void addTag (Tag a_tag);

	/**
	 * @param a_tag
	 *            the tag to remove
	 */
	void removeTag (Tag a_tag);

	/**
	 * @return the rotation angle (in degrees) - usually 0, 90, 180, 270
	 */
	int getRotation ();

	/**
	 * @param a_angle
	 *            the rotation angle (in degrees)
	 */
	void rotate (int a_angle);

	/**
	 * @return <code>true</code> if the image is visible, <code>false</code> otherwise
	 */
	boolean isVisible ();

	/**
	 * @param enable
	 */
	void setVisible (boolean enable);

}
