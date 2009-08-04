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

/**
 * Gathers all the callbacks to the screen for user interface changes.
 * 
 * @author Cyril ADRIAN
 */
public interface ScreenChanges {

	/**
	 * Ask to show the given panel
	 * 
	 * @param panel
	 *            the panel to show
	 * @param data
	 *            the data to send to the panel
	 */
	void showPanel (ScreenPanel panel, PanelData data);

	/**
	 * @param a_login
	 *            the login
	 * @param a_password
	 *            the password
	 */
	void checkLogin (String a_login, String a_password);

	/**
	 * @param a_login
	 * @param a_password
	 */
	void createUser (String a_login, String a_password);

	/**
	 * @param a_login
	 * @param a_password
	 */
	void doCreateUser (String a_login, String a_password);

	/**
	 * 
	 */
	boolean createSharedAlbum ();

	/**
	 * 
	 */
	boolean createPrivateAlbum ();

}