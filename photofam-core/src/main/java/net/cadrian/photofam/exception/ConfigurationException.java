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
package net.cadrian.photofam.exception;

import net.cadrian.photofam.Services;

/**
 * @author Cyril ADRIAN
 */
public class ConfigurationException extends PhotoFamException {

	private static final long serialVersionUID = 2799228501694761531L;

	/**
	 * @param services
	 */
	public ConfigurationException (Services services) {
		super(services);
	}

	/**
	 * @param services
	 * @param cause
	 */
	public ConfigurationException (Services services, Throwable cause) {
		super(services, cause);
	}

}
