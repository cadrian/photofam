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
import net.cadrian.photofam.model.AlbumListener;
import net.cadrian.photofam.model.Image;
import net.cadrian.photofam.model.Tag;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
public class Screen extends JFrame implements ScreenChanges {

	private static final Logger log = LoggerFactory.getLogger(Screen.class);

	private File lastDirectory;
	private final CardLayout layout;
	final AlbumDAO dao;
	private final ResourceBundle bundle;

	/**
	 * @param a_dao
	 *            the Album DAO
	 * @param a_bundle
	 *            the translation bundle
	 */
	public Screen (AlbumDAO a_dao, ResourceBundle a_bundle) {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		layout = new CardLayout();
		dao = a_dao;
		bundle = a_bundle;
		lastDirectory = new File(System.getProperty("user.home"));
		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened (WindowEvent a_e) {
				// nothing
			}

			@Override
			public void windowIconified (WindowEvent a_e) {
				// nothing
			}

			@Override
			public void windowDeiconified (WindowEvent a_e) {
				// nothing
			}

			@Override
			public void windowDeactivated (WindowEvent a_e) {
				// nothing
			}

			@Override
			public void windowClosing (WindowEvent a_e) {
				for (Album a : dao.getAlbums()) {
					dao.saveAlbum(a);
				}
				System.exit(0);
			}

			@Override
			public void windowClosed (WindowEvent a_e) {
				// never called
			}

			@Override
			public void windowActivated (WindowEvent a_e) {
				// nothing
			}
		});
	}

	/**
	 * Screen initialization (in dispatch thread)
	 */
	public void init () {
		assert SwingUtilities.isEventDispatchThread();

		setTitle("PhotoFam");

		Container contentPane = getContentPane();
		contentPane.setLayout(layout);

		for (ScreenPanel p : ScreenPanel.values()) {
			p.init(this, dao, bundle);
			p.addTo(contentPane);
		}
		showPanel(ScreenPanel.BROWSER, null);
		pack();
	}

	@Override
	public void showPanel (ScreenPanel panel, PanelData data) {
		panel.prepare(data);
		layout.show(getContentPane(), panel.name());
		panel.requestFocus();
	}

	@Override
	public boolean createAlbum (AlbumListener a_listener) {
		boolean result = false;

		JFileChooser fileChooser = new JFileChooser(lastDirectory);
		fileChooser.setDialogTitle(bundle.getString("screen.createalbum.title"));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);

		int ret = fileChooser.showDialog(this, bundle.getString("screen.createalbum.button.create"));
		if (ret == JFileChooser.APPROVE_OPTION) {
			final File directory = fileChooser.getSelectedFile();
			String albumName = directory.getName();
			final Album album = dao.createAlbum(albumName);
			album.addAlbumListener(a_listener);
			SwingUtilities.invokeLater(new Runnable() {
				public void run () {
					try {
						Screen.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						album.addImages(directory);
					} finally {
						Screen.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				}
			});
			lastDirectory = directory.getParentFile();
			result = true;
		}
		return result;
	}

	@Override
	public void showAlbum (Album a_album) {
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (log.isInfoEnabled()) {
				if (a_album == null) {
					log.info("Showing no album");
				} else {
					log.info("Showing album: " + a_album.getName());
				}
			}
			for (ScreenPanel p : ScreenPanel.values()) {
				p.showAlbum(a_album);
			}
		} finally {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	@Override
	public void showImage (Image a_image) {
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (log.isInfoEnabled()) {
				if (a_image == null) {
					log.info("Showing no image");
				} else {
					log.info("Showing image: " + a_image.getName());
				}
			}
			for (ScreenPanel p : ScreenPanel.values()) {
				p.showImage(a_image);
			}
		} finally {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	@Override
	public void filterTag (Tag a_tag) {
		try {
			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			if (log.isInfoEnabled()) {
				if (a_tag == null) {
					log.info("Filtering no tag");
				} else {
					log.info("Filtering tag: " + a_tag.getCompleteName());
				}
			}
			for (ScreenPanel p : ScreenPanel.values()) {
				p.filterTag(a_tag);
			}
		} finally {
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

}
