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
package net.cadrian.photofam.services;

import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.authentication.User;

import java.util.List;

/**
 * @author Cyril ADRIAN
 */
public interface AlbumService {

	/**
	 * @param user
	 *            the user
	 * @return the list of shared albums, customized by the user
	 */
	List<Album> getSharedAlbums (User user);

	/**
	 * @param user
	 *            the user
	 * @return the list of private albums
	 */
	List<Album> getPrivateAlbums (User user);

}
