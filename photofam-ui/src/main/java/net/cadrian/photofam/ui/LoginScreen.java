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
import net.cadrian.photofam.exception.PhotoFamException;
import net.cadrian.photofam.services.TranslationService;
import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.album.Image;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
class LoginScreen extends UIComponent {

	static final Logger log = LoggerFactory.getLogger(LoginScreen.class);

	private final JTextField login;
	private final JPasswordField password;

	LoginScreen () {
		login = new JTextField();
		password = new JPasswordField();
	}

	@Override
	void init (final ScreenChanges a_screen, Services services) {
		assert SwingUtilities.isEventDispatchThread();

		final TranslationService t = services.getTranslationService();

		JPanel loginPane = new JPanel();
		loginPane.setLayout(new GridLayout(2, 2));
		JLabel loginTitle = new JLabel(t.get("loginscreen.label.login"), JLabel.RIGHT);
		loginTitle.setLabelFor(login);
		JLabel passwordTitle = new JLabel(t.get("loginscreen.label.password"), JLabel.RIGHT);
		passwordTitle.setLabelFor(password);
		loginPane.add(loginTitle);
		loginPane.add(login);
		loginPane.add(passwordTitle);
		loginPane.add(password);

		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton loginButton = new JButton(new AbstractAction(t.get("loginscreen.button.login")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				try {
					a_screen.checkLogin(getLogin(), getPassword());
				} catch (PhotoFamException pfx) {
					log.error(pfx.getMessage(), pfx);
					JOptionPane.showMessageDialog(LoginScreen.this, pfx.getMessage(), t.get("error"), JOptionPane.ERROR_MESSAGE);
				} catch (RuntimeException rx) {
					log.error(rx.getMessage(), rx);
					JOptionPane.showMessageDialog(LoginScreen.this, rx.getMessage(), t.get("error"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonsPane.add(loginButton);
		JButton createButton = new JButton(new AbstractAction(t.get("loginscreen.button.createuser")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				a_screen.createUser(getLogin(), getPassword());
			}
		});
		buttonsPane.add(createButton);

		JLabel title = new JLabel("PhotoFam", JLabel.CENTER);

		JPanel center = new JPanel(new BorderLayout());
		center.add(title, BorderLayout.NORTH);
		center.add(loginPane, BorderLayout.CENTER);
		center.add(buttonsPane, BorderLayout.SOUTH);

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(4, 4, 4, 4);
		add(center, c);
	}

	String getLogin () {
		return login.getText();
	}

	String getPassword () {
		return new String(password.getPassword());
	}

	@Override
	void prepare (PanelData a_data) {
		assert a_data == null;

		login.requestFocusInWindow();
	}

	@Override
	void showAlbum (Album a_album) {
		assert false;
	}

	@Override
	void showImage (Image a_image) {
		assert false;
	}

}
