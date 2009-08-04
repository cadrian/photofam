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

/**
 * @author Cyril ADRIAN
 */
class CreateUserScreen extends UIComponent {

	private final JTextField login;
	private final JPasswordField password;
	private final JPasswordField passwordCheck;

	CreateUserScreen () {
		login = new JTextField();
		password = new JPasswordField();
		passwordCheck = new JPasswordField();
	}

	@Override
	void init (final ScreenChanges a_screen, Services services) {
		assert SwingUtilities.isEventDispatchThread();

		final TranslationService t = services.getTranslationService();

		JPanel loginPane = new JPanel();
		loginPane.setLayout(new GridLayout(3, 2));
		JLabel loginTitle = new JLabel(t.get("createuserscreen.label.login"), JLabel.RIGHT);
		loginTitle.setLabelFor(login);
		JLabel passwordTitle = new JLabel(t.get("createuserscreen.label.password"), JLabel.RIGHT);
		passwordTitle.setLabelFor(password);
		JLabel passwordCheckTitle = new JLabel(t.get("createuserscreen.label.passwordcheck"), JLabel.RIGHT);
		passwordCheckTitle.setLabelFor(passwordCheck);
		loginPane.add(loginTitle);
		loginPane.add(login);
		loginPane.add(passwordTitle);
		loginPane.add(password);
		loginPane.add(passwordCheckTitle);
		loginPane.add(passwordCheck);

		JPanel buttonsPane = new JPanel();
		buttonsPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton createButton = new JButton(new AbstractAction(t.get("createuserscreen.button.createuser")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed (ActionEvent a_e) {
				if (getPassword().equals(getPasswordCheck())) {
					try {
						a_screen.doCreateUser(getLogin(), getPassword());
					} catch (PhotoFamException pfx) {
						JOptionPane.showMessageDialog(CreateUserScreen.this, pfx.getMessage(), "", JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(CreateUserScreen.this, t.get("createuserscreen.message.passwordmismatch"), "",
							JOptionPane.ERROR_MESSAGE);
				}
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

	String getPasswordCheck () {
		return new String(passwordCheck.getPassword());
	}

	@Override
	void prepare (PanelData a_data) {
		assert a_data instanceof CreateUserData;

		CreateUserData data = (CreateUserData) a_data;
		login.setText(data.getLogin());
		password.setText(data.getPassword());
	}

}
