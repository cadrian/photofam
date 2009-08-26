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

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * @author Cyril ADRIAN
 */
class BrowserScreen extends JPanel implements UIComponent {

	private final AlbumsTree albums;
	private final Toolbar toolbar;
	private final ImageViewer viewer;
	private final Thumbnails thumbnails;

	/**
	 * Constructor
	 */
	public BrowserScreen () {
		albums = new AlbumsTree();
		toolbar = new Toolbar();
		viewer = new ImageViewer();
		thumbnails = new Thumbnails();
	}

	@Override
	public void init (ScreenChanges a_screen, AlbumDAO a_dao, ResourceBundle a_bundle) {
		assert SwingUtilities.isEventDispatchThread();
		albums.init(a_screen, a_dao, a_bundle);
		toolbar.init(a_screen, a_dao, a_bundle);
		viewer.init(a_screen, a_dao, a_bundle);
		thumbnails.init(a_screen, a_dao, a_bundle);
		// TODO exif reading to get the photo date and other metadata

		setLayout(new BorderLayout());

		add(albums, BorderLayout.WEST);
		add(viewer, BorderLayout.CENTER);
		add(thumbnails, BorderLayout.EAST);
		add(toolbar, BorderLayout.SOUTH);
	}

	@Override
	public void prepare (PanelData a_data) {
		assert a_data == null;
	}

	@Override
	public void showAlbum (Album a_album) {
		albums.showAlbum(a_album);
		toolbar.showAlbum(a_album);
		thumbnails.showAlbum(a_album);
		viewer.showAlbum(a_album);
	}

	@Override
	public void showImage (Image a_image) {
		albums.showImage(a_image);
		toolbar.showImage(a_image);
		thumbnails.showImage(a_image);
		viewer.showImage(a_image);
	}

	@Override
	public void filterTag (Tag a_tag) {
		albums.filterTag(a_tag);
		toolbar.filterTag(a_tag);
		thumbnails.filterTag(a_tag);
		viewer.filterTag(a_tag);
	}

}
