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
 * @author Cyril ADRIAN
 */
class CreateUserData implements PanelData {

	private final String login;
	private final String password;

	CreateUserData (String a_login, String a_password) {
		login = a_login;
		password = a_password;
	}

	/**
	 * @return the login
	 */
	public String getLogin () {
		return login;
	}

	/**
	 * @return the password
	 */
	public String getPassword () {
		return password;
	}

}
