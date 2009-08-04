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
@SuppressWarnings("serial")
public abstract class PhotoFamException extends RuntimeException {

	private final Services services;
	private final Object[] params;

	/**
	 * @param a_services
	 *            services (for message translation)
	 * @param a_params
	 *            message parameters
	 */
	public PhotoFamException (Services a_services, Object... a_params) {
		this(a_services, null, a_params);
	}

	/**
	 * @param a_services
	 *            services (for message translation)
	 * @param cause
	 *            exception cause
	 * @param a_params
	 *            message parameters
	 */
	public PhotoFamException (Services a_services, Throwable cause, Object... a_params) {
		super(cause);
		services = a_services;
		params = a_params;
	}

	@Override
	public String getMessage () {
		return services.getTranslationService().get(getClass().getName(), params);
	}

}
