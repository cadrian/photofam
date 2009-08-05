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
import net.cadrian.photofam.exception.DuplicateAlbumException;
import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.album.AlbumListener;
import net.cadrian.photofam.xml.userdata.UserData;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Cyril ADRIAN
 */
class UserInfo implements Serializable, AlbumListener {

	private static final long serialVersionUID = -2628085135522290409L;

	private final String identifier;
	private final Map<String, AlbumProxy> byName;
	private final Map<String, AlbumProxy> byRawName;

	/**
	 * @param a_identifier
	 *            user identifier
	 */
	public UserInfo (String a_identifier) {
		identifier = a_identifier;
		byName = new TreeMap<String, AlbumProxy>();
		byRawName = new HashMap<String, AlbumProxy>();
	}

	public UserInfo (UserData data) {
		this(data.getIdentifier());
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier () {
		return identifier;
	}

	AlbumProxy getAlbum (RawAlbum raw, UserImpl a_viewer) {
		AlbumProxy result = byRawName.get(raw.getName());
		if (result != null && !result.isAttached()) {
			result.attach(raw, a_viewer);
		}
		return result;
	}

	AlbumProxy createAlbum (Services services, String name, UserImpl a_viewer, String password, File a_directory) {
		boolean shared = password == null;
		String rawName;
		try {
			rawName = a_directory.getCanonicalPath().replace(File.separatorChar, '!');
		} catch (IOException x) {
			throw new RuntimeException("BUG", x);
		}
		if (byRawName.get(rawName) != null) {
			throw new DuplicateAlbumException(services);
		}
		RawAlbum raw = RawAlbum.find(rawName, a_viewer.getIdentifier(), password);
		if (raw == null) {
			raw = new RawAlbum(rawName, a_viewer.getIdentifier(), shared, a_directory);
		}
		raw.addAlbumListener(this);
		AlbumProxy result = new AlbumProxy(name, rawName, shared);
		result.attach(raw, a_viewer);
		result.addAlbumListener(this);
		byName.put(name, result);
		byRawName.put(rawName, result);
		return result;
	}

	@Override
	public void nameChanged (Album a_album, String a_oldName) {
		byName.remove(a_oldName);
		byName.put(a_album.getName(), (AlbumProxy) a_album);
	}

	private void readObject (ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		for (AlbumProxy a : byName.values()) {
			a.addAlbumListener(this);
		}
	}

	Album getAlbum (String rawName, UserImpl user, String password) {
		Album result = null;
		AlbumProxy proxy = byName.get(rawName);
		if (proxy != null) {
			if (!proxy.isAttached()) {
				proxy.attach(RawAlbum.find(rawName, user.getIdentifier(), proxy.isShared() ? null : password), user);
			}
			result = proxy;
		}
		return result;
	}

	List<String> getAlbumNames () {
		return new ArrayList<String>(byName.keySet());
	}

}
