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
package net.cadrian.photofam.ui;

import net.cadrian.photofam.services.album.Album;
import net.cadrian.photofam.services.authentication.User;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
public class Albums implements TreeModel {

	private static final Logger log = LoggerFactory.getLogger(Albums.class);

	private RootNode root;
	private final List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	private static interface Node {
		String getName ();

		int getChildCount ();

		Node getChild (int i);

		int getIndexOfChild (Node a_child);

		boolean isLeaf ();
	}

	private static class AlbumNode implements Node {
		private final Album album;

		AlbumNode (Album a_album) {
			album = a_album;
		}

		@Override
		public String getName () {
			return album.getName();
		}

		@Override
		public int getChildCount () {
			return album.getChildren().size();
		}

		@Override
		public Node getChild (int i) {
			return new AlbumNode(album.getChildren().get(i));
		}

		@Override
		public int getIndexOfChild (Node a_child) {
			return album.getChildren().indexOf(((AlbumNode) a_child).album);
		}

		@Override
		public boolean isLeaf () {
			return getChildCount() == 0;
		}

		@Override
		public String toString () {
			return getName();
		}
	}

	private static class RootNode implements Node {
		private final User user;

		RootNode (User a_user) {
			user = a_user;
		}

		@Override
		public String getName () {
			return user.getIdentifier();
		}

		@Override
		public int getChildCount () {
			return user.getSharedAlbumNames().size() + user.getPrivateAlbumNames().size();
		}

		@Override
		public Node getChild (int i) {
			Node result;
			List<String> shared = user.getSharedAlbumNames();
			if (i < shared.size()) {
				result = new AlbumNode(user.getSharedAlbum(shared.get(i)));
			} else {
				List<String> priv = user.getPrivateAlbumNames();
				result = new AlbumNode(user.getPrivateAlbum(priv.get(i - shared.size())));
			}
			return result;
		}

		@Override
		public int getIndexOfChild (Node a_child) {
			int result;
			String name = ((AlbumNode) a_child).getName();
			List<String> shared = user.getSharedAlbumNames();
			int i = shared.indexOf(name);
			if (i != -1) {
				result = i;
			} else {
				result = shared.size() + user.getPrivateAlbumNames().indexOf(name);
			}
			return result;
		}

		@Override
		public boolean isLeaf () {
			return false;
		}

		@Override
		public String toString () {
			return getName();
		}
	}

	@Override
	public void addTreeModelListener (TreeModelListener a_l) {
		listeners.add(a_l);
	}

	@Override
	public Object getChild (Object a_parent, int a_index) {
		return ((Node) a_parent).getChild(a_index);
	}

	@Override
	public int getChildCount (Object a_parent) {
		return ((Node) a_parent).getChildCount();
	}

	@Override
	public int getIndexOfChild (Object a_parent, Object a_child) {
		return ((Node) a_parent).getIndexOfChild((Node) a_child);
	}

	@Override
	public Object getRoot () {
		return root;
	}

	@Override
	public boolean isLeaf (Object a_node) {
		return ((Node) a_node).isLeaf();
	}

	@Override
	public void removeTreeModelListener (TreeModelListener a_l) {
		listeners.remove(a_l);
	}

	@Override
	public void valueForPathChanged (TreePath a_path, Object a_newValue) {
		assert false;
	}

	void setUser (User a_user) {
		if (log.isInfoEnabled()) {
			log.info("User is " + a_user.getIdentifier());
		}
		root = new RootNode(a_user);
		fireRootStructureChanged();
	}

	void fireRootStructureChanged () {
		TreeModelEvent e = new TreeModelEvent(this, new TreePath(root));
		for (TreeModelListener l : listeners) {
			l.treeStructureChanged(e);
		}
	}

}