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
package net.cadrian.photofam.services.album;

/**
 * @author Cyril ADRIAN
 */
public interface ImageFilter {

	/**
	 * @param image
	 *            the image to filter
	 * 
	 * @return <code>true</code> if the image should be shown; <code>false</code> otherwise.
	 */
	boolean accept (Image image);

}
