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
public class InsufficientRightsException extends PhotoFamException {

	private static final long serialVersionUID = 5325772216761783425L;

	/**
	 * @param services
	 */
	public InsufficientRightsException (Services services) {
		super(services);
	}

	/**
	 * @param services
	 * @param cause
	 */
	public InsufficientRightsException (Services services, Throwable cause) {
		super(services, cause);
	}

}
