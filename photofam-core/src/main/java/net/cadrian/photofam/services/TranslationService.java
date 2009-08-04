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

import java.util.Locale;

/**
 * @author Cyril ADRIAN
 */
public interface TranslationService {

	/**
	 * @param key
	 *            the translation key
	 * @param args
	 *            the translation parameter values
	 * 
	 * @return the translated value using the jvm locale
	 */
	String get (String key, Object... args);

	/**
	 * @param locale
	 *            the locale of the message to return
	 * @param key
	 *            the translation key
	 * @param args
	 *            the translation parameter values
	 * 
	 * @return the translated value using the given locale
	 */
	String get (Locale locale, String key, Object... args);

}
