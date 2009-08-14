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

import net.cadrian.photofam.model.Album;
import net.cadrian.photofam.model.ImageFilter;
import net.cadrian.photofam.model.Tag;

import java.awt.Component;
import java.awt.Graphics;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
public class Albums implements TreeModel {

	static final Logger log = LoggerFactory.getLogger(Albums.class);

	private volatile RootNode root;
	private final List<TreeModelListener> listeners = new ArrayList<TreeModelListener>();

	private volatile Collection<Album> albums;

	static interface Node {
		String getName ();

		int getChildCount ();

		Node getChild (int i);

		int getIndexOfChild (Node a_child);

		boolean isLeaf ();

		Icon getIcon ();

		/**
		 * @param a_screen
		 */
		void onSelect (ScreenChanges a_screen);
	}

	static class TagNode implements Node {
		private final Tag tag;

		TagNode (Tag a_tag) {
			tag = a_tag;
			if (log.isDebugEnabled()) {
				log.debug("TagNode - tag: " + a_tag.getCompleteName());
			}
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
			return tag.getChildren().indexOf(((TagNode) a_child).getTag());
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
		public String toString () {
			return getName();
		}

		Tag getTag () {
			return tag;
		}

		@Override
		public void onSelect (ScreenChanges a_screen) {
			a_screen.filterTag(tag);
		}

	}

	static class AlbumNode implements Node {
		private final Album album;
		private final List<Tag> rootTags;

		AlbumNode (Album a_album) {
			album = a_album;
			rootTags = listRootTags();
			if (log.isDebugEnabled()) {
				log.debug("created AlbumNode " + a_album.getName());
			}
		}

		private List<Tag> listRootTags () {
			List<Tag> result;
			Set<Tag> roots = new TreeSet<Tag>();
			for (Tag tag : album.getAllTags()) {
				Tag p = tag.getParent();
				while (p != null) {
					tag = p;
					p = tag.getParent();
				}
				roots.add(tag);
			}
			result = new ArrayList<Tag>(roots);
			if (log.isDebugEnabled()) {
				log.debug("all tags of " + getName() + ": " + album.getAllTags());
				log.debug("root tags of " + getName() + ": " + result);
			}
			return result;
		}

		@Override
		public String getName () {
			return album.getName();
		}

		@Override
		public int getChildCount () {
			if (rootTags == null) {
				listRootTags();
			}
			int result = rootTags.size();
			if (log.isDebugEnabled()) {
				log.debug("AlbumNode " + album.getName() + ": " + (result > 1 ? result + " children" : " one child"));
			}
			return result;
		}

		@Override
		public Node getChild (int i) {
			if (rootTags == null) {
				listRootTags();
			}
			TagNode result = new TagNode(rootTags.get(i));
			if (log.isDebugEnabled()) {
				log.debug("AlbumNode " + album.getName() + ": child#" + i + " is " + result);
			}
			return result;
		}

		@Override
		public int getIndexOfChild (Node a_child) {
			if (rootTags == null) {
				listRootTags();
			}
			return rootTags.indexOf(((TagNode) a_child).getTag());
		}

		@Override
		public boolean isLeaf () {
			return getChildCount() == 0;
		}

		@Override
		public Icon getIcon () {
			URL location = RootNode.class.getClassLoader().getResource("img/album.png");
			return new ImageIcon(location);
		}

		@Override
		public String toString () {
			int n = album.getImages(ImageFilter.ALL).size();
			return getName() + " (" + n + " photo" + (n > 1 ? "s)" : ")");
		}

		Album getAlbum () {
			return album;
		}

		@Override
		public void onSelect (ScreenChanges a_screen) {
			a_screen.showAlbum(album);
		}

	}

	static class RootNode implements Node {
		private final List<Album> albums;

		RootNode (Collection<Album> a_albums) {
			albums = new ArrayList<Album>(a_albums);
			Collections.sort(albums);
			if (log.isDebugEnabled()) {
				log.debug("RootNode - albums: " + albums);
			}
		}

		@Override
		public String getName () {
			return "root";
		}

		@Override
		public int getChildCount () {
			return albums.size();
		}

		@Override
		public Node getChild (int i) {
			return new AlbumNode(albums.get(i));
		}

		@Override
		public int getIndexOfChild (Node a_child) {
			Album album = ((AlbumNode) a_child).getAlbum();
			return albums.indexOf(album);
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
		root = new RootNode(albums);
		TreeModelEvent e = new TreeModelEvent(this, new TreePath(root));
		for (TreeModelListener l : listeners) {
			l.treeStructureChanged(e);
		}
	}

}
