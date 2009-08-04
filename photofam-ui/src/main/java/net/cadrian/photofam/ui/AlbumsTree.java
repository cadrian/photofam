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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

/**
 * @author Cyril ADRIAN
 */
class AlbumsTree extends UIComponent {

	private final JTree view;
	private final Albums model;

	AlbumsTree () {
		model = new Albums();
		view = new JTree(model);
	}

	@Override
	void init (final ScreenChanges a_screen, Services services) {
		assert SwingUtilities.isEventDispatchThread();

		setPreferredSize(new Dimension(200, 400));

		JScrollPane scroll = new JScrollPane(view);
		view.setRootVisible(true);
		view.setEditable(false);
		view.setScrollsOnExpand(true);
		view.setShowsRootHandles(true);

		URL addPrivateImage = AlbumsTree.class.getClassLoader().getResource("img/private-album.png");
		URL addSharedImage = AlbumsTree.class.getClassLoader().getResource("img/shared-album.png");

		JToolBar tools = new JToolBar();
		JButton addPrivate = new JButton(new AbstractAction(null, new ImageIcon(addPrivateImage)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				if (a_screen.createPrivateAlbum()) {
					model.fireRootStructureChanged();
				}
			}
		});
		JButton addShared = new JButton(new AbstractAction(null, new ImageIcon(addSharedImage)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				if (a_screen.createSharedAlbum()) {
					model.fireRootStructureChanged();
				}
			}
		});
		tools.add(addPrivate);
		tools.add(addShared);

		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
		add(tools, BorderLayout.SOUTH);
	}

	@Override
	void prepare (PanelData a_data) {
		assert a_data instanceof BrowserData;
		model.setUser(((BrowserData) a_data).getUser());
	}

}
