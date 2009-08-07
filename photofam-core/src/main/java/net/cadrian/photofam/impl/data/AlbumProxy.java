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
import net.cadrian.photofam.xml.userdata.AlbumType;
import net.cadrian.photofam.xml.userdata.Child;
import net.cadrian.photofam.xml.userdata.RemovedImage;

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
public class AlbumProxy implements Album, Serializable {

	private static final long serialVersionUID = -9174042753042136594L;

	private transient RawAlbum raw;
	private String name;
	private final String rawName;
	private transient UserImpl viewer;
	private final Set<String> removed;
	private transient List<AlbumListener> listeners = new ArrayList<AlbumListener>();
	private transient List<Album> children;
	private final boolean shared;

	/**
	 * @param a_name
	 *            the album name
	 * @param a_raw
	 *            the raw album
	 * @param a_viewer
	 *            the album viewer
	 */
	public AlbumProxy (String a_name, RawAlbum a_raw, UserImpl a_viewer) {
		assert a_name != null;
		assert a_raw != null;
		assert a_viewer != null;

		name = a_name;
		rawName = a_raw.getName();
		shared = a_raw.isShared();
		removed = new HashSet<String>();
		raw = a_raw;
		viewer = a_viewer;
	}

	AlbumProxy (AlbumType a_album, String userIdentifier, String password) {
		name = a_album.getName();
		rawName = a_album.getRawName();
		shared = a_album.getShared();
		removed = new HashSet<String>();
		for (RemovedImage r : a_album.getRemovedImage()) {
			removed.add(r.getName());
		}
		children = new ArrayList<Album>();
		for (Child c : a_album.getChild()) {
			children.add(new AlbumProxy(c, userIdentifier, password));
		}
		raw = RawAlbum.find(rawName, userIdentifier, shared ? null : password);
	}

	void setViewer (UserImpl a_viewer) {
		viewer = a_viewer;
		for (Album a : getChildren()) {
			((AlbumProxy) a).setViewer(a_viewer);
		}
	}

	/**
	 * @return the rawName
	 */
	public String getRawName () {
		return rawName;
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
				result.add(viewer.getAlbum((RawAlbum) child));
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
	public String getOwner () {
		return raw.getOwner();
	}

	@Override
	public boolean isShared () {
		return shared;
	}

	@Override
	public void add (Services a_services, Image a_image) {
		if (!getOwner().equals(viewer.getIdentifier())) {
			throw new InsufficientRightsException(a_services);
		}
		raw.add(a_services, a_image);
	}

	@Override
	public void remove (Image a_image) {
		if (getOwner().equals(viewer.getIdentifier())) {
			raw.remove(a_image);
		} else {
			removed.add(a_image.getName());
		}
	}

	@Override
	public void addAlbumListener (AlbumListener a_albumListener) {
		listeners.add(a_albumListener);
		for (Album a : getChildren()) {
			a.addAlbumListener(a_albumListener);
		}
	}

	net.cadrian.photofam.xml.userdata.Album createCastorAlbum () {
		net.cadrian.photofam.xml.userdata.Album result = new net.cadrian.photofam.xml.userdata.Album();
		fillCastorAlbum(result);
		return result;
	}

	void fillCastorAlbum (AlbumType album) {
		album.setName(name);
		album.setRawName(rawName);
		album.setShared(shared);
		for (Album a : getChildren()) {
			Child c = new Child();
			((AlbumProxy) a).fillCastorAlbum(c);
			album.addChild(c);
		}
		for (String rm : removed) {
			RemovedImage ri = new RemovedImage();
			ri.setName(rm);
			album.addRemovedImage(ri);
		}
	}

}
