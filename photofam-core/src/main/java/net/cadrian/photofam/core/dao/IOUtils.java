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
package net.cadrian.photofam.core.dao;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
final class IOUtils {

	private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

	private static File getPhotoFamDirectory () {
		File result;
		String photofamHome = System.getProperty("photofam.home");
		if (photofamHome != null) {
			result = new File(photofamHome);
			if (!result.exists()) {
				throw new RuntimeException(photofamHome + " does not exist");
			}
		} else {
			File home = new File(System.getProperty("user.home"));
			result = new File(home, ".photofam");
			if (!result.exists() && !result.mkdirs()) {
				throw new RuntimeException(photofamHome + " does not exist");
			}
		}
		return result;
	}

	static File getAlbumFile (String name, String ext) {
		File home = getPhotoFamDirectory();
		File result = new File(home, name + "." + ext);
		if (log.isDebugEnabled()) {
			log.debug("getAlbumFile('" + name + "') = " + result);
		}
		return result;
	}

	static List<File> getAlbumFiles (FileFilter a_filter) {
		return Arrays.asList(getPhotoFamDirectory().listFiles(a_filter));
	}

}
