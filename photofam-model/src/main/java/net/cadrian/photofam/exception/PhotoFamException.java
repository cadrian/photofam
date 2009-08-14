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

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author Cyril ADRIAN
 */
@SuppressWarnings("serial")
public abstract class PhotoFamException extends RuntimeException {

	private final PhotoFamExceptionKey key;
	private final Object[] params;
	private transient ResourceBundle bundle;

	/**
	 * @param a_key
	 *            the message key
	 * @param a_params
	 *            message parameters
	 */
	public PhotoFamException (PhotoFamExceptionKey a_key, Object... a_params) {
		this(a_key, null, a_params);
	}

	/**
	 * @param a_key
	 *            the message key
	 * @param cause
	 *            exception cause
	 * @param a_params
	 *            message parameters
	 */
	public PhotoFamException (PhotoFamExceptionKey a_key, Throwable cause, Object... a_params) {
		super(cause);
		key = a_key;
		params = a_params != null ? a_params : new Object[0];
	}

	/**
	 * @param a_bundle
	 *            the bundle to set
	 */
	public void setBundle (ResourceBundle a_bundle) {
		bundle = a_bundle;
	}

	@Override
	public String getMessage () {
		String format;
		if (bundle != null) {
			format = bundle.getString(key.getKey());
		} else {
			format = getDefaultFormat(key.getKey(), params.length);
		}
		return MessageFormat.format(format, params);
	}

	private static String getDefaultFormat (String a_key, int n) {
		StringBuilder b = new StringBuilder(a_key).append('[');
		for (int i = 0; i < n; i++) {
			if (i > 0) {
				b.append(", ");
			}
			b.append('{').append(i).append('}');
		}
		return b.append(']').toString();
	}

}
