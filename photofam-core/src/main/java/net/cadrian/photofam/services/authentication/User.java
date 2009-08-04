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
package net.cadrian.photofam.services.authentication;

import net.cadrian.photofam.services.album.Album;

import java.io.File;
import java.util.List;

/**
 * @author Cyril ADRIAN
 */
public interface User {

	/**
	 * @return the user identifier
	 */
	String getIdentifier ();

	/**
	 * @param name
	 *            the name of the album
	 * @return the album
	 */
	Album getSharedAlbum (String name);

	/**
	 * @param name
	 *            the name of the new album
	 * @param directory
	 *            the directory containing the photos
	 */
	void createSharedAlbum (String name, File directory);

	/**
	 * @param name
	 *            the name of the album
	 * @return the album
	 */
	Album getPrivateAlbum (String name);

	/**
	 * @param name
	 *            the name of the new album
	 * @param directory
	 *            the directory containing the photos
	 */
	void createPrivateAlbum (String name, File directory);

	/**
	 * @return the names of the shared albums; never <code>null</code>.
	 */
	List<String> getSharedAlbumNames ();

	/**
	 * @return the names of the private albums; never <code>null</code>.
	 */
	List<String> getPrivateAlbumNames ();

}
