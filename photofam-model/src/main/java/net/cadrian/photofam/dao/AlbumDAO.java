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
package net.cadrian.photofam.dao;

import net.cadrian.photofam.model.Album;

import java.util.Collection;

/**
 * @author Cyril ADRIAN
 */
public interface AlbumDAO {

	/**
	 * @param name
	 *            the album name; not <code>null</code>
	 * 
	 * @return the album if it exists, <code>null</code> otherwise
	 */
	Album getAlbum (String name);

	/**
	 * @param name
	 *            the name of the album to create, not <code>null</code>; must not already exist (i.e. {@link #getAlbum(String)}
	 *            must return <code>null</code>)
	 * @return the newly created album, never <code>null</code>
	 */
	Album createAlbum (String name);

	/**
	 * @return the list of albums; never <code>null</code> but may be empty
	 */
	Collection<Album> getAlbums ();

	/**
	 * @param a_album
	 *            the album to save
	 */
	void saveAlbum (Album a_album);

}
