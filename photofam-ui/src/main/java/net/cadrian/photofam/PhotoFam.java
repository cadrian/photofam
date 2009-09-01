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

import net.cadrian.photofam.core.dao.AlbumDAOImpl;
import net.cadrian.photofam.dao.AlbumDAO;
import net.cadrian.photofam.ui.Screen;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
public class PhotoFam {

	private static final Logger log = LoggerFactory.getLogger(PhotoFam.class);

	/**
	 * @param args
	 *            command line arguments
	 * 
	 * @throws Exception
	 *             if an error occurs during startup
	 */
	public static void main (String... args) {
		try {
			setNativeLAF();
			AlbumDAO dao = new AlbumDAOImpl();
			log.debug("Default locale: " + Locale.getDefault());
			final ResourceBundle bundle = ResourceBundle.getBundle("photofam");
			final Screen screen = new Screen(dao, bundle);
			SwingUtilities.invokeLater(new Runnable() {
				public void run () {
					screen.init();
					screen.setVisible(true);
				}
			});
		} catch (Exception x) {
			log.error("Fatal error", x);
			log.error("Classpath: " + System.getProperty("java.class.path"));
		}
	}

	private static void setNativeLAF () throws Exception {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException x) {
			throw x;
		} catch (InstantiationException x) {
			throw x;
		} catch (IllegalAccessException x) {
			throw x;
		} catch (UnsupportedLookAndFeelException x) {
			x.printStackTrace(); // ignored
		}
	}

}
