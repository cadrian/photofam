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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Cyril ADRIAN
 */
class AskAlbumName extends JDialog {

	private String albumName;
	private File directory;

	AskAlbumName (Screen parent, Services services, boolean a_shared) {
		super(parent, true);
		Container c = getContentPane();
		TranslationService t = services.getTranslationService();
		String shared = a_shared ? "shared" : "private";
		setTitle(t.get("askalbumname.title." + shared));
		JPanel ask = new JPanel(new GridLayout(2, 2));
		final JTextField albumNameField = new JTextField();
		JLabel labelName = new JLabel(t.get("askalbumname.label.albumname." + shared));
		labelName.setLabelFor(albumNameField);
		ask.add(labelName);
		ask.add(albumNameField);
		final JTextField directoryField = new JTextField();
		JLabel labelDirectory = new JLabel(t.get("askalbumname.label.directory." + shared));
		labelDirectory.setLabelFor(directoryField);
		ask.add(labelDirectory);
		ask.add(directoryField);
		JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton ok = new JButton(new AbstractAction(t.get("ok")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				setAlbumName(albumNameField.getText());
				setDirectory(new File(directoryField.getText()));
				AskAlbumName.this.setVisible(false);
			}
		});
		JButton cancel = new JButton(new AbstractAction(t.get("cancel")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				setAlbumName(null);
				setDirectory(null);
				AskAlbumName.this.setVisible(false);
			}
		});
		buttons.add(ok);
		buttons.add(cancel);

		c.setLayout(new BorderLayout());
		c.add(ask, BorderLayout.CENTER);
		c.add(buttons, BorderLayout.SOUTH);

		pack();
	}

	/**
	 * @return the albumName
	 */
	public String getAlbumName () {
		return albumName;
	}

	/**
	 * @return the directory
	 */
	public File getDirectory () {
		return directory;
	}

	void setAlbumName (String a_albumName) {
		albumName = a_albumName;
	}

	void setDirectory (File a_directory) {
		directory = a_directory;
	}

}
