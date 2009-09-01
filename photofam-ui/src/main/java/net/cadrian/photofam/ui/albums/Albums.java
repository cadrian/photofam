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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
class Albums implements TreeModel {

	static final Logger log = LoggerFactory.getLogger(Albums.class);

	private volatile RootNode root;
	private final List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	private volatile Collection<Album> albums;

	private ResourceBundle bundle;

	public void setBundle (ResourceBundle a_bundle) {
		bundle = a_bundle;
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

	void setAlbums (Collection<Album> a_albums) {
		albums = a_albums;
		fireRootStructureChanged();
	}

	void fireRootStructureChanged () {
		root = new RootNode(albums, bundle);
		TreeModelEvent e = new TreeModelEvent(this, new TreePath(root));
		for (TreeModelListener l : listeners) {
			l.treeStructureChanged(e);
		}
	}

}
