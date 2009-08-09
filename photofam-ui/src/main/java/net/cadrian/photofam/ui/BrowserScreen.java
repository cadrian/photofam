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

import net.cadrian.photofam.Services;
import net.cadrian.photofam.services.TranslationService;
import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.album.Image;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * @author Cyril ADRIAN
 */
class BrowserScreen extends UIComponent {

	private final AlbumsTree albums;
	private final Toolbar toolbar;
	private final ImageViewer viewer;
	private final Thumbnails thumbnails;
	private final TagsTree tags;
	private final JTabbedPane tabs;

	/**
	 * Constructor
	 */
	public BrowserScreen () {
		albums = new AlbumsTree();
		toolbar = new Toolbar();
		viewer = new ImageViewer();
		thumbnails = new Thumbnails();
		tags = new TagsTree();
		tabs = new JTabbedPane();
	}

	@Override
	void init (ScreenChanges a_screen, Services services) {
		assert SwingUtilities.isEventDispatchThread();
		albums.init(a_screen, services);
		toolbar.init(a_screen, services);
		viewer.init(a_screen, services);
		thumbnails.init(a_screen, services);
		tags.init(a_screen, services);

		setLayout(new BorderLayout());

		TranslationService t = services.getTranslationService();

		tabs.add(t.get("browserscreen.tab.albums"), albums);
		tabs.add(t.get("browserscreen.tab.tags"), tags);

		add(tabs, BorderLayout.WEST);
		add(viewer, BorderLayout.CENTER);
		add(thumbnails, BorderLayout.EAST);
		add(toolbar, BorderLayout.SOUTH);
	}

	@Override
	void prepare (PanelData a_data) {
		assert a_data instanceof BrowserData;

		albums.prepare(a_data);
		tags.prepare(a_data);

		tabs.setSelectedIndex(0);
	}

	@Override
	void showAlbum (Album a_album) {
		albums.showAlbum(a_album);
		toolbar.showAlbum(a_album);
		viewer.showAlbum(a_album);
		thumbnails.showAlbum(a_album);
		tags.showAlbum(a_album);
	}

	@Override
	void showImage (Image a_image) {
		albums.showImage(a_image);
		toolbar.showImage(a_image);
		viewer.showImage(a_image);
		thumbnails.showImage(a_image);
		tags.showImage(a_image);
	}

}
