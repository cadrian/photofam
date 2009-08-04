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
package net.cadrian.photofam;

import net.cadrian.photofam.impl.ServicesImpl;
import net.cadrian.photofam.ui.Screen;

import javax.swing.SwingUtilities;

/**
 * @author Cyril ADRIAN
 */
public class PhotoFam {

	/**
	 * @param args
	 *            command line arguments
	 * 
	 * @throws Exception
	 *             if an error occurs during startup
	 */
	public static void main (String... args) throws Exception {
		Services services = new ServicesImpl();
		final Screen screen = new Screen(services);
		SwingUtilities.invokeLater(new Runnable() {
			public void run () {
				screen.init();
				screen.setVisible(true);
			}
		});
	}

}
