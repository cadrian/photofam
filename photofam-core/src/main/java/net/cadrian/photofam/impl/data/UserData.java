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

import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.album.AlbumListener;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Cyril ADRIAN
 */
class UserData implements Serializable, AlbumListener {

	private static final long serialVersionUID = -2628085135522290409L;

	private final String identifier;
	private final Map<String, SharedAlbum> sharedAlbums;
	private final Map<String, PrivateAlbum> privateAlbums;

	/**
	 * @param a_identifier
	 *            user identifier
	 */
	public UserData (String a_identifier) {
		identifier = a_identifier;
		sharedAlbums = new TreeMap<String, SharedAlbum>();
		privateAlbums = new TreeMap<String, PrivateAlbum>();
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier () {
		return identifier;
	}

	SharedAlbum getSharedAlbum (RawAlbum raw, UserImpl a_viewer) {
		SharedAlbum result = sharedAlbums.get(raw.getName());
		if (result != null) {
			result.attach(raw, a_viewer);
		}
		return result;
	}

	SharedAlbum createSharedAlbum (String name, UserImpl a_viewer, File a_directory) {
		RawAlbum raw = RawAlbum.findShared(name, a_viewer.getIdentifier());
		if (raw == null) {
			raw = new RawAlbum(name, a_viewer, true, a_directory);
		}
		raw.addAlbumListener(this);
		SharedAlbum result = new SharedAlbum(name);
		result.attach(raw, a_viewer);
		result.addAlbumListener(this);
		sharedAlbums.put(name, result);
		return result;
	}

	PrivateAlbum getPrivateAlbum (RawAlbum raw, UserImpl a_viewer) {
		PrivateAlbum result = privateAlbums.get(raw.getName());
		if (result != null) {
			result.attach(raw, a_viewer);
		}
		return result;
	}

	PrivateAlbum createPrivateAlbum (String name, UserImpl a_viewer, String password, File a_directory) {
		RawAlbum raw = RawAlbum.findPrivate(name, a_viewer.getIdentifier(), password);
		if (raw == null) {
			raw = new RawAlbum(name, a_viewer, true, a_directory);
		}
		raw.addAlbumListener(this);
		PrivateAlbum result = new PrivateAlbum(name);
		result.attach(raw, a_viewer);
		result.addAlbumListener(this);
		privateAlbums.put(name, result);
		return result;
	}

	@Override
	public void nameChanged (Album a_album, String a_oldName) {
		if (a_album.isShared()) {
			sharedAlbums.remove(a_oldName);
			sharedAlbums.put(a_album.getName(), (SharedAlbum) a_album);
		} else {
			privateAlbums.remove(a_oldName);
			privateAlbums.put(a_album.getName(), (PrivateAlbum) a_album);
		}
	}

	private void readObject (ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		for (SharedAlbum a : sharedAlbums.values()) {
			a.addAlbumListener(this);
		}
		for (PrivateAlbum a : privateAlbums.values()) {
			a.addAlbumListener(this);
		}
	}

	Album getPrivateAlbum (String name, UserImpl user, String password) {
		Album result = null;
		PrivateAlbum pa = privateAlbums.get(name);
		if (pa != null) {
			if (!pa.isAttached()) {
				pa.attach(RawAlbum.findPrivate(name, user.getIdentifier(), password), user);
			}
			result = pa;
		}
		return result;
	}

	Album getSharedAlbum (String name, UserImpl user) {
		Album result = null;
		SharedAlbum sa = sharedAlbums.get(name);
		if (sa != null) {
			if (!sa.isAttached()) {
				sa.attach(RawAlbum.findShared(name, user.getIdentifier()), user);
			}
			result = sa;
		}
		return result;
	}

	List<String> getPrivateAlbumNames () {
		return new ArrayList<String>(privateAlbums.keySet());
	}

	List<String> getSharedAlbumNames () {
		return new ArrayList<String>(sharedAlbums.keySet());
	}

}
