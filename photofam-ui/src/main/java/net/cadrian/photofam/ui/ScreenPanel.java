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

import java.awt.Container;
import java.util.ResourceBundle;

import javax.swing.JComponent;

/**
 * @author Cyril ADRIAN
 */
public enum ScreenPanel {

	/**
	 * The main screen to browse photos
	 */
	BROWSER(new BrowserScreen()),

	/**
	 * The slideshow screen
	 */
	SLIDESHOW(new SlideshowScreen());

	private final UIComponent panel;

	private ScreenPanel (UIComponent a_panel) {
		panel = a_panel;
	}

	void init (Screen a_screen, AlbumDAO a_dao, ResourceBundle a_bundle) {
		panel.init(a_screen, a_dao, a_bundle);
	}

	void addTo (Container container) {
		container.add(name(), (JComponent) panel);
	}

	/**
	 * @param a_data
	 *            the data
	 */
	public void prepare (PanelData a_data) {
		panel.prepare(a_data);
	}

	void requestFocus () {
		((JComponent) panel).requestFocusInWindow();
	}

	/**
	 * @param a_album
	 */
	public void showAlbum (Album a_album) {
		panel.showAlbum(a_album);
	}

	/**
	 * @param a_image
	 */
	public void showImage (Image a_image) {
		panel.showImage(a_image);
	}

	/**
	 * @param a_tag
	 */
	public void filterTag (Tag a_tag) {
		panel.filterTag(a_tag);
	}

}
