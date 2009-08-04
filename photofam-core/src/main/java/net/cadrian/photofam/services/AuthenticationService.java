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
package net.cadrian.photofam.services;

import net.cadrian.photofam.exception.AuthenticationException;
import net.cadrian.photofam.services.authentication.User;

/**
 * @author Cyril ADRIAN
 */
public interface AuthenticationService {

	/**
	 * @param identifier
	 *            the user identifier
	 * @param password
	 *            the user password
	 * @return the user, never <code>null</code>.
	 * 
	 * @throws AuthenticationException
	 *             if the user cannot be authenticated
	 */
	User getUser (String identifier, String password) throws AuthenticationException;

	/**
	 * Create a new user
	 * 
	 * @param identifier
	 *            the user identifier
	 * @param password
	 *            the user password
	 */
	void createUser (String identifier, String password);

}
