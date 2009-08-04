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
package net.cadrian.photofam.impl;

import net.cadrian.photofam.Services;
import net.cadrian.photofam.exception.AuthenticationException;
import net.cadrian.photofam.impl.data.UserImpl;
import net.cadrian.photofam.services.AuthenticationService;
import net.cadrian.photofam.services.authentication.User;

/**
 * @author Cyril ADRIAN
 * 
 */
class AuthenticationServiceImpl implements AuthenticationService {

	private final Services services;

	AuthenticationServiceImpl (Services a_services) {
		services = a_services;
	}

	@Override
	public User getUser (String a_identifier, String a_password) throws AuthenticationException {
		return new UserImpl(services, a_identifier, a_password);
	}

	@Override
	public void createUser (String a_identifier, String a_password) {
		UserImpl.create(services, a_identifier, a_password);
	}

}
