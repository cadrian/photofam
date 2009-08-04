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
import net.cadrian.photofam.exception.InsufficientRightsException;
import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.album.AlbumListener;
import net.cadrian.photofam.services.album.Image;
import net.cadrian.photofam.services.album.ImageFilter;
import net.cadrian.photofam.services.authentication.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Album as viewed by a user
 * 
 * @author Cyril ADRIAN
 */
public class SharedAlbum implements Album, Serializable {

	private static final long serialVersionUID = -9174042753042136594L;

	private transient RawAlbum raw;
	private String name;
	private transient UserImpl viewer;
	private final Set<String> removed;
	private transient List<AlbumListener> listeners;
	private transient List<Album> children;

	/**
	 * @param a_name
	 *            the album name
	 */
	public SharedAlbum (String a_name) {
		assert a_name != null;

		name = a_name;
		removed = new HashSet<String>();
		listeners = new ArrayList<AlbumListener>();
	}

	void attach (RawAlbum a_raw, UserImpl a_viewer) {
		assert a_raw.isShared();
		assert a_viewer != null;

		raw = a_raw;
		viewer = a_viewer;
	}

	boolean isAttached () {
		return raw != null;
	}

	/**
	 * @param a_name
	 *            the name to set
	 */
	@Override
	public void setName (String a_name) {
		assert a_name != null;

		String old = name;
		name = a_name;
		for (AlbumListener l : listeners) {
			l.nameChanged(this, old);
		}
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public List<Album> getChildren () {
		List<Album> result = children;
		if (result == null) {
			result = new ArrayList<Album>();
			for (Album child : raw.getChildren()) {
				result.add(viewer.getSharedAlbum((RawAlbum) child));
			}
			children = result;
		}
		return result;
	}

	@Override
	public List<Image> getImages (final ImageFilter filter) {
		final Set<String> localRemoved = Collections.unmodifiableSet(removed);
		return raw.getImages(new ImageFilter() {
			@Override
			public boolean accept (Image a_image) {
				return filter.accept(a_image) && !localRemoved.contains(a_image.getName());
			}
		});
	}

	@Override
	public User getOwner () {
		return raw.getOwner();
	}

	@Override
	public boolean isShared () {
		return true;
	}

	@Override
	public void add (Services a_services, Image a_image) {
		if (getOwner() != viewer) {
			throw new InsufficientRightsException(a_services);
		}
		raw.add(a_services, a_image);
	}

	@Override
	public void remove (Image a_image) {
		if (getOwner() == viewer) {
			raw.remove(a_image);
		} else {
			removed.add(a_image.getName());
		}
	}

	@Override
	public void addAlbumListener (AlbumListener a_albumListener) {
		listeners.add(a_albumListener);
	}

}
