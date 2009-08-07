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

import java.awt.Container;

/**
 * @author Cyril ADRIAN
 */
public enum ScreenPanel {

	/**
	 * The login screen
	 */
	LOGIN(new LoginScreen()),

	/**
	 * The create user screen
	 */
	CREATE_USER(new CreateUserScreen()),

	/**
	 * The main screen to browse photos
	 */
	BROWSER(new BrowserScreen()),

	/**
	 * The slideshow screen
	 */
	SLIDESHOW(new SlideshowScreen());

	private final UIComponent panel;

	private ScreenPanel (UIComponent a_panel) {
		panel = a_panel;
	}

	void init (Screen a_screen, Services services) {
		panel.init(a_screen, services);
	}

	void addTo (Container container) {
		container.add(name(), panel);
	}

	/**
	 * @param a_data
	 *            the data
	 */
	public void prepare (PanelData a_data) {
		panel.prepare(a_data);
	}

	void requestFocus () {
		panel.requestFocusInWindow();
	}

}
