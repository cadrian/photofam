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

import net.cadrian.photofam.services.TranslationService;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Cyril ADRIAN
 */
class TranslationServiceImpl implements TranslationService {

	TranslationServiceImpl () {
		// nothing
	}

	@Override
	public String get (String a_key, Object... a_args) {
		return get(Locale.getDefault(), a_key, a_args);
	}

	@Override
	public String get (Locale a_locale, String a_key, Object... a_args) {
		String format = ResourceBundle.getBundle("photofam", a_locale).getString(a_key);
		return MessageFormat.format(format, a_args);
	}

}
