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
import net.cadrian.photofam.exception.ConfigurationException;
import net.cadrian.photofam.services.AuthenticationService;
import net.cadrian.photofam.services.TranslationService;

/**
 * @author Cyril ADRIAN
 * 
 */
public class ServicesImpl implements Services {

	private final TranslationServiceImpl translation;
	private final AuthenticationServiceImpl authentication;

	/**
	 * Constructor
	 */
	public ServicesImpl () {
		try {
			translation = new TranslationServiceImpl();
		} catch (ConfigurationException cx) {
			throw new RuntimeException("Could not start the translation service");
		}
		authentication = new AuthenticationServiceImpl(this);
	}

	@Override
	public AuthenticationService getAuthenticationService () {
		return authentication;
	}

	@Override
	public TranslationService getTranslationService () {
		return translation;
	}

}
