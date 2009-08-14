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

import java.awt.Dimension;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Cyril ADRIAN
 */
class Thumbnails extends JPanel implements UIComponent {

	@Override
	public void init (ScreenChanges a_screen, AlbumDAO a_dao, ResourceBundle a_bundle) {
		assert SwingUtilities.isEventDispatchThread();

		setPreferredSize(new Dimension(200, 400));
	}

	@Override
	public void prepare (PanelData a_data) {
		assert a_data == null;
	}

	@Override
	public void showAlbum (Album a_album) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showImage (Image a_image) {
		// TODO Auto-generated method stub

	}

	@Override
	public void filterTag (Tag a_tag) {
		// TODO Auto-generated method stub

	}

}
