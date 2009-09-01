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
package net.cadrian.photofam.ui.albums;

import net.cadrian.photofam.model.Album;
import net.cadrian.photofam.ui.ScreenChanges;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.Icon;

class RootNode implements Node {
	private final List<AlbumNode> children;
	private final ResourceBundle bundle;

	RootNode (Collection<Album> a_albums, ResourceBundle a_bundle) {
		bundle = a_bundle;
		children = new ArrayList<AlbumNode>(a_albums.size());
		for (Album album : a_albums) {
			children.add(new AlbumNode(album, a_bundle));
		}
		Collections.sort(children, new Comparator<AlbumNode>() {
			@Override
			public int compare (AlbumNode a1, AlbumNode a2) {
				return a1.getName().compareTo(a2.getName());
			}
		});
		if (Albums.log.isDebugEnabled()) {
			Albums.log.debug("RootNode - albums: " + a_albums);
		}
	}

	@Override
	public String getName () {
		return bundle.containsKey("node.root") ? bundle.getString("node.root") : "root";
	}

	@Override
	public int getChildCount () {
		return children.size();
	}

	@Override
	public Node getChild (int i) {
		return children.get(i);
	}

	@Override
	public int getIndexOfChild (Node a_child) {
		return children.indexOf(a_child);
	}

	@Override
	public boolean isLeaf () {
		return false;
	}

	@Override
	public Icon getIcon () {
		return new Icon() {

			@Override
			public void paintIcon (Component a_c, Graphics a_g, int a_x, int a_y) {
				// nothing
			}

			@Override
			public int getIconWidth () {
				return 0;
			}

			@Override
			public int getIconHeight () {
				return 0;
			}
		};
	}

	@Override
	public String toString () {
		return getName();
	}

	@Override
	public void onSelect (ScreenChanges a_screen) {
		assert false;
	}

}
