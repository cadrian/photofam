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
import net.cadrian.photofam.exception.AuthenticationException;
import net.cadrian.photofam.services.authentication.User;

import java.awt.CardLayout;
import java.awt.Container;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
public class Screen extends JFrame implements ScreenChanges {

	private static final Logger log = LoggerFactory.getLogger(Screen.class);

	private final CardLayout layout;
	private final Services services;
	private User user;

	/**
	 * @param a_services
	 *            te services
	 */
	public Screen (Services a_services) {
		layout = new CardLayout();
		services = a_services;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	/**
	 * Screen initialization (in dispatch thread)
	 */
	public void init () {
		assert SwingUtilities.isEventDispatchThread();

		Container contentPane = getContentPane();
		contentPane.setLayout(layout);

		for (ScreenPanel p : ScreenPanel.values()) {
			p.init(this, services);
			p.addTo(contentPane);
		}
		showPanel(ScreenPanel.LOGIN, null);
		pack();
	}

	@Override
	public void showPanel (ScreenPanel panel, PanelData data) {
		panel.prepare(data);
		layout.show(getContentPane(), panel.name());
	}

	@Override
	public void checkLogin (String a_login, String a_password) {
		try {
			user = services.getAuthenticationService().getUser(a_login, a_password);
			BrowserData data = new BrowserData(user);
			showPanel(ScreenPanel.BROWSER, data);
		} catch (AuthenticationException ax) {
			log.error("error while checking logging", ax);
		}
	}

	@Override
	public void createUser (String a_login, String a_password) {
		CreateUserData data = new CreateUserData(a_login, a_password);
		showPanel(ScreenPanel.CREATE_USER, data);
	}

	@Override
	public void doCreateUser (String a_login, String a_password) {
		try {
			services.getAuthenticationService().createUser(a_login, a_password);
			showPanel(ScreenPanel.LOGIN, null);
		} catch (AuthenticationException ax) {
			log.error("error while creating user " + a_login, ax);
		}
	}

	@Override
	public boolean createPrivateAlbum () {
		return createAlbum(false);
	}

	@Override
	public boolean createSharedAlbum () {
		return createAlbum(true);
	}

	private boolean createAlbum (boolean shared) {
		boolean result = false;
		AskAlbumName ask = new AskAlbumName(this, services, shared);
		ask.setVisible(true);
		String albumName = ask.getAlbumName();
		File directory = ask.getDirectory();
		if (albumName != null) {
			user.createPrivateAlbum(albumName, directory);
		}
		return result;
	}

}
