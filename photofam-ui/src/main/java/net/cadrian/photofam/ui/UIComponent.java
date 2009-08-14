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
package net.cadrian.photofam.ui;

import net.cadrian.photofam.dao.AlbumDAO;
import net.cadrian.photofam.model.Album;
import net.cadrian.photofam.model.Image;
import net.cadrian.photofam.model.Tag;

import java.util.ResourceBundle;

/**
 * @author Cyril ADRIAN
 */
interface UIComponent {

	/**
	 * Component initialization (in dispatch tread)
	 * 
	 * @param a_screen
	 *            the screen
	 * @param a_dao TODO
	 * @param a_bundle
	 *            TODO
	 */
	abstract void init (ScreenChanges a_screen, AlbumDAO a_dao, ResourceBundle a_bundle);

	/**
	 * @param a_data
	 *            the data used to prepare the screen
	 */
	abstract void prepare (PanelData a_data);

	/**
	 * @param a_album
	 *            the album to show (may be <code>null</code>)
	 */
	abstract void showAlbum (Album a_album);

	/**
	 * @param a_image
	 *            the image to show (may be <code>null</code>)
	 */
	abstract void showImage (Image a_image);

	/**
	 * @param a_tag
	 *            the tag to filter
	 */
	abstract void filterTag (Tag a_tag);

}
