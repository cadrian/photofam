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
package net.cadrian.photofam.services.album;


import java.util.List;

/**
 * @author Cyril ADRIAN
 */
public interface Image {

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
	 * @return the tags of the image; never <code>null</code> (but may be empty).
	 */
	List<Tag> getTags ();

	/**
	 * @return the format of the image; never <code>null</code>.
	 */
	String getFormat ();

	/**
	 * @return the data of the image; never <code>null</code>.
	 */
	java.awt.Image getImage ();

}
