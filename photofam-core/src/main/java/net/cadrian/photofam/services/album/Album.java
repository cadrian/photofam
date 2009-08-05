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

import net.cadrian.photofam.Services;

import java.util.List;

/**
 * @author Cyril ADRIAN
 */
public interface Album {

	/**
	 * @return the album name
	 */
	String getName ();

	/**
	 * @param name
	 *            the new album name
	 */
	void setName (String name);

	/**
	 * @return the name of owner of the album; never <code>null</code>.
	 */
	String getOwner ();

	/**
	 * @return <code>true</code> if the album may be displayed by other users; <code>false</code> otherwise.
	 */
	boolean isShared ();

	/**
	 * @return the child albums; never <code>null</code>.
	 */
	List<Album> getChildren ();

	/**
	 * @param filter
	 *            the image filter
	 * 
	 * @return the images in the album; never <code>null</code>.
	 */
	List<Image> getImages (ImageFilter filter);

	/**
	 * @param image
	 *            the image to remove
	 */
	void remove (Image image);

	/**
	 * @param a_services
	 *            TODO
	 * @param image
	 *            the image to add
	 */
	void add (Services a_services, Image image);

	/**
	 * @param albumListener
	 *            the listener to add
	 */
	void addAlbumListener (AlbumListener albumListener);

}
