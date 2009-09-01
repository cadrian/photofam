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
package net.cadrian.photofam.model.metadata;

import java.io.Serializable;

/**
 * @author Cyril ADRIAN
 */
public final class Coordinates implements Serializable {

	private static final long serialVersionUID = -1169744798898610016L;

	/**
	 * longitude
	 */
	public final double longitude;

	/**
	 * latitude
	 */
	public final double latitude;

	/**
	 * @param a_longitude
	 *            the longitude
	 * @param a_latitude
	 *            the latitude
	 */
	public Coordinates (double a_longitude, double a_latitude) {
		longitude = a_longitude;
		latitude = a_latitude;
	}

}
