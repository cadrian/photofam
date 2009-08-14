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

import net.cadrian.photofam.core.model.AlbumImpl;
import net.cadrian.photofam.dao.AlbumDAO;
import net.cadrian.photofam.exception.AlbumDAOException;
import net.cadrian.photofam.model.Album;
import net.cadrian.photofam.model.AlbumListener;
import net.cadrian.photofam.model.Image;
import net.cadrian.photofam.xml.DataObject;
import net.cadrian.photofam.xml.albumdata.AlbumData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.exolab.castor.core.exceptions.CastorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
public class AlbumDAOImpl implements AlbumDAO, AlbumListener, FileFilter {

	private static final Logger log = LoggerFactory.getLogger(AlbumDAOImpl.class);

	private final Map<String, AlbumImpl> albums = new HashMap<String, AlbumImpl>();

	static final String ALBUM_EXTENSION = "pfa"; // PFA = PhotoFam Album

	/**
	 * Constructor
	 */
	public AlbumDAOImpl () {
		try {
			readAllAlbums();
		} catch (IOException x) {
			throw new AlbumDAOException(AlbumDAOException.Key.InitializationProblem, x);
		} catch (CastorException x) {
			throw new AlbumDAOException(AlbumDAOException.Key.InitializationProblem, x);
		}
	}

	@Override
	public Album createAlbum (String a_name) {
		AlbumImpl result = new AlbumImpl(a_name);
		albums.put(a_name, result);
		try {
			writeAlbum(result);
		} catch (IOException x) {
			throw new AlbumDAOException(AlbumDAOException.Key.MarshallProblem, x);
		} catch (CastorException x) {
			throw new AlbumDAOException(AlbumDAOException.Key.MarshallProblem, x);
		}
		return result;
	}

	@Override
	public Album getAlbum (String a_name) {
		AlbumImpl result = albums.get(a_name);
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<Album> getAlbums () {
		Collection<?> a = albums.values();
		return Collections.unmodifiableCollection((Collection<Album>) a);
	}

	@Override
	public void saveAlbum (Album a_album) {
		try {
			writeAlbum((AlbumImpl) a_album);
		} catch (IOException x) {
			throw new AlbumDAOException(AlbumDAOException.Key.MarshallProblem, x);
		} catch (CastorException x) {
			throw new AlbumDAOException(AlbumDAOException.Key.MarshallProblem, x);
		}
	}

	private void readAllAlbums () throws IOException, CastorException {
		for (File file : IOUtils.getAlbumFiles(this)) {
			AlbumImpl album = readAlbum(file);
			album.addAlbumListener(this);
			albums.put(album.getName(), album);
		}
	}

	private AlbumImpl readAlbum (File a_file) throws IOException, CastorException {
		AlbumImpl result = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		InputStream in = new BufferedInputStream(new FileInputStream(a_file));
		int c = in.read();
		while (c != -1) {
			out.write(c);
			c = in.read();
		}
		byte[] data = out.toByteArray();
		GZIPInputStream gzin = new GZIPInputStream(new ByteArrayInputStream(data));
		AlbumData albumData = DataObject.read(gzin, AlbumData.class);
		gzin.close();
		result = new AlbumImpl(albumData);
		return result;
	}

	private void writeAlbum (AlbumImpl a_album) throws IOException, CastorException {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		GZIPOutputStream gzout = new GZIPOutputStream(bo);
		a_album.createCastorAlbum().write(gzout);
		gzout.flush();
		gzout.close();
		byte[] encodedData = bo.toByteArray();
		File datafile = IOUtils.getAlbumFile(a_album.getName(), ALBUM_EXTENSION);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(datafile));
		for (int i = 0, n = encodedData.length; i < n; i++) {
			out.write(encodedData[i]);
		}
		out.flush();
		out.close();
	}

	// FileFilter implementation

	@Override
	public boolean accept (File a_file) {
		boolean result = false;
		String name = a_file.getName();
		int i = name.lastIndexOf('.');
		if (log.isDebugEnabled()) {
			log.debug("name: " + name);
		}
		if (i != -1) {
			String ext = name.substring(i + 1);
			if (log.isDebugEnabled()) {
				log.debug("extension: " + ext);
			}
			result = ALBUM_EXTENSION.equalsIgnoreCase(ext);
		}
		return result;
	}

	// AlbumListener implementation

	@Override
	public void albumNameChanged (Album a_album, String a_oldName) {
		albums.remove(a_album);
		albums.put(a_album.getName(), (AlbumImpl) a_album);
	}

	@Override
	public void albumImageAdded (Album a_album, Image a_image) {
		// nothing
	}

	@Override
	public void albumImagesAdded (Album a_album) {
		// nothing
	}

}
