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
import net.cadrian.photofam.model.Tag;
import net.cadrian.photofam.ui.ScreenChanges;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Icon;
import javax.swing.ImageIcon;

class TagsNode implements Node {
	private final Album album;
	private final List<Tag> rootTags;
	private final ResourceBundle bundle;

	TagsNode (Album a_album, ResourceBundle a_bundle) {
		bundle = a_bundle;
		album = a_album;
		rootTags = listRootTags();
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
		if (Albums.log.isDebugEnabled()) {
			Albums.log.debug("all tags of " + getName() + ": " + album.getAllTags());
			Albums.log.debug("root tags of " + getName() + ": " + result);
		}
		return result;
	}

	@Override
	public String getName () {
		return bundle.containsKey("node.tags") ? bundle.getString("node.tags") : "tags";
	}

	@Override
	public int getChildCount () {
		if (rootTags == null) {
			listRootTags();
		}
		int result = rootTags.size();
		return result;
	}

	@Override
	public Node getChild (int i) {
		if (rootTags == null) {
			listRootTags();
		}
		TagNode result = new TagNode(rootTags.get(i));
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
		URL location = RootNode.class.getClassLoader().getResource("img/boomy/tree/fav.png");
		return new ImageIcon(location);
	}

	@Override
	public String toString () {
		return getName();
	}

	Album getAlbum () {
		return album;
	}

	@Override
	public void onSelect (ScreenChanges a_screen) {
		a_screen.showAlbum(album);
	}

}
