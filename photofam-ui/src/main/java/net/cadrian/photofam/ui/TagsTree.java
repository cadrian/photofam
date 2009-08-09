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
import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.album.Image;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
class TagsTree extends UIComponent {

	static final Logger log = LoggerFactory.getLogger(TagsTree.class);

	private final JTree view;
	final Tags model;

	TagsTree () {
		model = new Tags();
		view = new JTree(model);
	}

	@Override
	void init (ScreenChanges a_screen, Services services) {
		assert SwingUtilities.isEventDispatchThread();

		setPreferredSize(new Dimension(200, 400));

		JScrollPane scroll = new JScrollPane(view);
		view.setRootVisible(true);
		view.setEditable(false);
		view.setScrollsOnExpand(true);
		view.setShowsRootHandles(false);
		view.putClientProperty("JTree.lineStyle", "Horizontal");
		view.setCellRenderer(new TagsTreeCellRenderer());

		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}

	@Override
	void prepare (PanelData a_data) {
		assert a_data instanceof BrowserData;
	}

	@Override
	void showAlbum (Album a_album) {
		model.setAlbum(a_album);
	}

	@Override
	void showImage (Image a_image) {
		// does not handle images
	}

}
