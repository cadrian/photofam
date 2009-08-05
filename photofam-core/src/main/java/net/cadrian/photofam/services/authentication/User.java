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

import net.cadrian.photofam.Services;
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
	 * 
	 * @return the album
	 */
	Album getAlbum (String name);

	/**
	 * @param services
	 *            the services
	 * @param name
	 *            the name of the new album
	 * @param directory
	 *            the directory containing the photos
	 * @param shared
	 *            <code>true</code> if the album is shared, <code>false</code> otherwise
	 */
	void createAlbum (Services services, String name, File directory, boolean shared);

	/**
	 * @return the names of the albums; never <code>null</code>.
	 */
	List<String> getAlbumNames ();

}
