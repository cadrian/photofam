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
import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.authentication.User;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
public class UserImpl implements User {

	private static final Logger log = LoggerFactory.getLogger(UserImpl.class);

	private final UserData data;
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
			data = read(a_identifier, a_password);
		} catch (Exception x) {
			throw new AuthenticationException(a_services, x, a_identifier);
		}
		password = a_password;
	}

	@Override
	public String getIdentifier () {
		return data.getIdentifier();
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
		if (getUserDataFile(a_identifier).exists()) {
			throw new AuthenticationException(a_services, a_identifier);
		}
		UserData data = new UserData(a_identifier);
		try {
			write(data, a_password);
		} catch (Exception x) {
			throw new AuthenticationException(a_services, x, a_identifier);
		}
	}

	private static UserData read (String identifier, String password) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		File datafile = getUserDataFile(identifier);
		InputStream in = new BufferedInputStream(new FileInputStream(datafile));
		int c = in.read();
		while (c != -1) {
			out.write(c);
			c = in.read();
		}
		byte[] data = EncodeUtils.decode(password, out.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
		UserData result = (UserData) ois.readObject();
		ois.close();
		return result;
	}

	private static void write (UserData data, String password) throws Exception {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bo);
		oos.writeObject(data);
		oos.flush();
		oos.close();
		byte[] encodedData = EncodeUtils.encode(password, bo.toByteArray());
		File datafile = getUserDataFile(data.getIdentifier());
		if (!datafile.getParentFile().mkdirs()) {
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

	private static File getUserDataFile (String identifier) {
		File home = new File(System.getProperty("user.home"));
		return new File(new File(new File(home, ".photofam"), "users"), identifier);
	}

	/**
	 * @param raw
	 *            the raw album
	 * @return the shared album
	 */
	public Album getSharedAlbum (RawAlbum raw) {
		return data.getSharedAlbum(raw, this);
	}

	/**
	 * @param raw
	 *            the raw album
	 * @return the shared album
	 */
	public Album getPrivateAlbum (RawAlbum raw) {
		return data.getPrivateAlbum(raw, this);
	}

	@Override
	public Album getSharedAlbum (String name) {
		return data.getSharedAlbum(name, this);
	}

	@Override
	public Album getPrivateAlbum (String name) {
		return data.getPrivateAlbum(name, this, password);
	}

	@Override
	public void createPrivateAlbum (String a_name, File a_directory) {
		data.createPrivateAlbum(a_name, this, password, a_directory);
	}

	@Override
	public void createSharedAlbum (String a_name, File a_directory) {
		data.createSharedAlbum(a_name, this, a_directory);
	}

	@Override
	public List<String> getPrivateAlbumNames () {
		return data.getPrivateAlbumNames();
	}

	@Override
	public List<String> getSharedAlbumNames () {
		return data.getSharedAlbumNames();
	}

}
