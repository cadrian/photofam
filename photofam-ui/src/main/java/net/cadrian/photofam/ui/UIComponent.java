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
package net.cadrian.photofam.ui;

import net.cadrian.photofam.Services;

import javax.swing.JPanel;

/**
 * @author Cyril ADRIAN
 */
@SuppressWarnings("serial")
abstract class UIComponent extends JPanel {

	/**
	 * Component initialization (in dispatch tread)
	 * 
	 * @param a_screen
	 *            the screen
	 * @param a_services
	 *            the services
	 */
	abstract void init (ScreenChanges a_screen, Services a_services);

	/**
	 * @param a_data
	 *            the data used to prepare the screen
	 */
	abstract void prepare (PanelData a_data);

}
