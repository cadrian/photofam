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

/**
 * @author Cyril ADRIAN
 */
public interface AlbumListener {

	/**
	 * Called when the album name changed
	 * 
	 * @param album
	 *            the modified album
	 * @param oldName
	 *            the old album name (use {@link Album#getName() album.getName()} to get the new name)
	 */
	void albumNameChanged (Album album, String oldName);

	/**
	 * Called when an image was added to the album
	 * 
	 * @param album
	 *            the modified album
	 * @param image
	 *            the added image
	 */
	void albumImageAdded (Album album, Image image);

	/**
	 * Called at the end of the {@link Album#addImages(java.io.File)} call
	 * 
	 * @param album
	 *            the modified album
	 */
	void albumImagesAdded (Album album);

}
