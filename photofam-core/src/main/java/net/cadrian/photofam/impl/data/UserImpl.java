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
package net.cadrian.photofam.impl.data;

import net.cadrian.photofam.Services;
import net.cadrian.photofam.exception.AuthenticationException;
import net.cadrian.photofam.exception.UnexpectedException;
import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.authentication.User;
import net.cadrian.photofam.xml.DataObject;
import net.cadrian.photofam.xml.userdata.UserData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
public class UserImpl implements User {

	private static final Logger log = LoggerFactory.getLogger(UserImpl.class);

	private final UserInfo info;
	private final String password;

	/**
	 * @param a_services
	 *            the services
	 * @param a_identifier
	 *            the user identifier
	 * @param a_password
	 *            the user password, to decrypt the user vault
	 * 
	 * @throws AuthenticationException
	 */
	public UserImpl (Services a_services, String a_identifier, String a_password) throws AuthenticationException {
		try {
			info = read(a_services, this, a_identifier, a_password);
		} catch (Exception x) {
			throw new AuthenticationException(a_services, x, a_identifier);
		}
		if (info == null) {
			throw new AuthenticationException(a_services, a_identifier);
		}
		password = a_password;
	}

	@Override
	public String getIdentifier () {
		return info.getIdentifier();
	}

	/**
	 * @param a_services
	 *            the services
	 * @param a_identifier
	 *            the user identifier
	 * @param a_password
	 *            the user password, to encrypt the user vault
	 * 
	 * @throws AuthenticationException
	 *             if the user cannot be created
	 */
	public static void create (Services a_services, String a_identifier, String a_password) throws AuthenticationException {
		if (IOUtils.getUserDataFile(a_identifier).exists()) {
			throw new AuthenticationException(a_services, a_identifier);
		}
		UserInfo info = new UserInfo(a_identifier);
		try {
			write(info, a_password);
		} catch (Exception x) {
			throw new AuthenticationException(a_services, x, a_identifier);
		}
	}

	private static UserInfo read (Services services, UserImpl a_user, String identifier, String password) throws Exception {
		UserInfo result = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File datafile = IOUtils.getUserDataFile(identifier);
		if (datafile.exists()) {
			InputStream in = new BufferedInputStream(new FileInputStream(datafile));
			int c = in.read();
			while (c != -1) {
				out.write(c);
				c = in.read();
			}
			byte[] data = IOUtils.decode(password, out.toByteArray());
			GZIPInputStream gzin = new GZIPInputStream(new ByteArrayInputStream(data));
			UserData userData = DataObject.read(gzin, UserData.class);
			gzin.close();
			result = new UserInfo(services, userData, a_user, password);
		}
		return result;
	}

	private static void write (UserInfo info, String password) throws Exception {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		GZIPOutputStream gzout = new GZIPOutputStream(bo);
		info.createUserData().write(gzout);
		gzout.flush();
		gzout.close();
		byte[] encodedData = password == null ? bo.toByteArray() : IOUtils.encode(password, bo.toByteArray());
		File datafile = IOUtils.getUserDataFile(info.getIdentifier());
		if (!datafile.getParentFile().exists() && !datafile.getParentFile().mkdirs()) {
			String msg = "Cannot create directory " + datafile.getParent();
			log.error(msg);
			throw new IOException(msg);
		}
		OutputStream out = new BufferedOutputStream(new FileOutputStream(datafile));
		for (int i = 0, n = encodedData.length; i < n; i++) {
			out.write(encodedData[i]);
		}
		out.flush();
		out.close();
	}

	Album getAlbum (RawAlbum raw) {
		return info.getAlbum(raw);
	}

	@Override
	public Album getAlbum (String name) {
		return info.getAlbum(name);
	}

	@Override
	public void createAlbum (Services services, String a_name, File a_directory, boolean a_shared) {
		info.createAlbum(services, a_name, this, a_shared ? null : password, a_directory);
		try {
			write(info, password);
		} catch (Exception x) {
			throw new UnexpectedException(services, x);
		}
	}

	@Override
	public List<String> getAlbumNames () {
		return info.getAlbumNames();
	}

}
