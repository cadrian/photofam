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

import net.cadrian.photofam.model.Tag;
import net.cadrian.photofam.ui.ScreenChanges;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

class TagNode implements Node {
	private final Tag tag;

	TagNode (Tag a_tag) {
		tag = a_tag;
		if (Albums.log.isDebugEnabled()) {
			Albums.log.debug("TagNode - tag: " + a_tag.getCompleteName());
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
		URL location = RootNode.class.getClassLoader().getResource("img/boomy/tree/fav.png");
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
