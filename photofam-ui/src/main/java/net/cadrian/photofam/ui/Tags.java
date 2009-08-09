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
import net.cadrian.photofam.services.album.Tag;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
public class Tags implements TreeModel {

	private static final Logger log = LoggerFactory.getLogger(Tags.class);

	private RootNode root;
	private final List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	static interface Node {
		String getName ();

		int getChildCount ();

		Node getChild (int i);

		int getIndexOfChild (Node a_child);

		boolean isLeaf ();

		Icon getIcon ();

		Tag getTag ();
	}

	static class TagNode implements Node {
		private final Tag tag;

		TagNode (Tag a_tag) {
			tag = a_tag;
		}

		@Override
		public String getName () {
			return tag.getName();
		}

		@Override
		public int getChildCount () {
			return tag.getChildren().size();
		}

		@Override
		public Node getChild (int i) {
			return new TagNode(tag.getChildren().get(i));
		}

		@Override
		public int getIndexOfChild (Node a_child) {
			return tag.getChildren().indexOf(a_child.getTag());
		}

		@Override
		public boolean isLeaf () {
			return getChildCount() == 0;
		}

		@Override
		public Icon getIcon () {
			URL location = RootNode.class.getClassLoader().getResource("img/tag.png");
			return new ImageIcon(location);
		}

		@Override
		public Tag getTag () {
			return tag;
		}

		@Override
		public String toString () {
			return getName();
		}

	}

	static class RootNode implements Node {
		private final Album album;
		private final List<Tag> tags;

		RootNode (Album a_album) {
			album = a_album;
			List<Tag> allTags = album.getAllTags();
			Set<Tag> rootTags = new TreeSet<Tag>();
			for (Tag tag : allTags) {
				if (tag.getParent() == null) {
					rootTags.add(tag);
				}
			}
			tags = new ArrayList<Tag>(rootTags);
		}

		@Override
		public String getName () {
			return album.getName();
		}

		@Override
		public int getChildCount () {
			return tags.size();
		}

		@Override
		public Node getChild (int i) {
			Node result = new TagNode(tags.get(i));
			return result;
		}

		@Override
		public int getIndexOfChild (Node a_child) {
			Tag tag = ((TagNode) a_child).getTag();
			return tags.indexOf(tag);
		}

		@Override
		public boolean isLeaf () {
			return false;
		}

		@Override
		public Icon getIcon () {
			URL location;
			if (album.isShared()) {
				location = RootNode.class.getClassLoader().getResource("img/shared-album.png");
			} else {
				location = RootNode.class.getClassLoader().getResource("img/private-album.png");
			}
			return new ImageIcon(location);
		}

		@Override
		public Tag getTag () {
			return null;
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

	void setAlbum (Album a_album) {
		if (log.isInfoEnabled()) {
			log.info("Album is " + a_album.getName());
		}
		root = new RootNode(a_album);
		fireRootStructureChanged();
	}

	void fireRootStructureChanged () {
		TreeModelEvent e = new TreeModelEvent(this, new TreePath(root));
		for (TreeModelListener l : listeners) {
			l.treeStructureChanged(e);
		}
	}

	/**
	 * @param a_path
	 * @return
	 */
	public Tag getTag (TreePath a_path) {
		return ((Node) a_path.getLastPathComponent()).getTag();
	}

}
