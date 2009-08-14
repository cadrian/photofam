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
import net.cadrian.photofam.exception.PhotoFamException;
import net.cadrian.photofam.model.Album;
import net.cadrian.photofam.model.AlbumListener;
import net.cadrian.photofam.model.Image;
import net.cadrian.photofam.model.Tag;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
class AlbumsTree extends JPanel implements UIComponent, AlbumListener {

	static final Logger log = LoggerFactory.getLogger(AlbumsTree.class);

	private final JTree view;
	final Albums model;

	AlbumsTree () {
		model = new Albums();
		view = new JTree(model);
	}

	@Override
	public void init (final ScreenChanges a_screen, final AlbumDAO a_dao, final ResourceBundle a_bundle) {
		assert SwingUtilities.isEventDispatchThread();

		setPreferredSize(new Dimension(300, 400));

		JScrollPane scroll = new JScrollPane(view);
		view.setRootVisible(false);
		view.setEditable(false);
		view.setScrollsOnExpand(true);
		view.setShowsRootHandles(true);
		view.putClientProperty("JTree.lineStyle", "Horizontal");
		view.setCellRenderer(new AlbumTreeCellRenderer());

		view.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged (TreeSelectionEvent e) {
				for (Object o : e.getPath().getPath()) {
					((Albums.Node) o).onSelect(a_screen);
				}
			}
		});

		URL createAlbumImage = AlbumsTree.class.getClassLoader().getResource("img/create-album.png");

		JToolBar tools = new JToolBar();
		JButton createAlbum = new JButton(new AbstractAction(null, new ImageIcon(createAlbumImage)) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				createAlbum(a_screen, a_dao, a_bundle);
			}
		});
		createAlbum.setBorderPainted(false);
		createAlbum.setOpaque(false);
		tools.add(createAlbum);

		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
		add(tools, BorderLayout.SOUTH);

		model.setAlbums(a_dao.getAlbums());
	}

	@Override
	public void prepare (PanelData a_data) {
		assert a_data == null;
	}

	@Override
	public void showAlbum (Album a_album) {
		// nothing to do
	}

	@Override
	public void showImage (Image a_image) {
		// don't care (don't manage individual images)
	}

	@Override
	public void filterTag (Tag a_tag) {
		// don't care (don't manage tags)
	}

	void createAlbum (ScreenChanges a_screen, AlbumDAO a_dao, ResourceBundle a_bundle) {
		try {
			if (a_screen.createAlbum(AlbumsTree.this)) {
				model.setAlbums(a_dao.getAlbums());
				model.fireRootStructureChanged();
			}
		} catch (PhotoFamException pfx) {
			log.error(pfx.getMessage(), pfx);
			JOptionPane
					.showMessageDialog(AlbumsTree.this, pfx.getMessage(), a_bundle.getString("error"), JOptionPane.ERROR_MESSAGE);
		} catch (RuntimeException rx) {
			log.error(rx.getMessage(), rx);
			JOptionPane.showMessageDialog(AlbumsTree.this, rx.getMessage(), a_bundle.getString("error"), JOptionPane.ERROR_MESSAGE);
		}
	}

	// AlbumListener implementation

	@Override
	public void albumImageAdded (Album a_album, Image a_image) {
		// nothing
	}

	@Override
	public void albumNameChanged (Album a_album, String a_oldName) {
		model.fireRootStructureChanged();
	}

	@Override
	public void albumImagesAdded (Album a_album) {
		if (log.isDebugEnabled()) {
			log.debug("albumImagesAdded", new Exception("TRACE"));
		}
		model.fireRootStructureChanged();
	}

}
