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
import net.cadrian.photofam.model.ImageFilter;
import net.cadrian.photofam.ui.ScreenChanges;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

class AlbumNode implements Node {
	private final Album album;
	private final TagsNode tags;
	private final DatesNode dates;

	AlbumNode (Album a_album, ResourceBundle a_bundle) {
		album = a_album;
		tags = new TagsNode(a_album, a_bundle);
		dates = new DatesNode(a_album, a_bundle);
	}

	@Override
	public String getName () {
		return album.getName();
	}

	@Override
	public int getChildCount () {
		return 2;
	}

	@Override
	public Node getChild (int i) {
		Node result;
		switch (i) {
		case 0:
			result = tags;
			break;
		case 1:
			result = dates;
			break;
		default:
			throw new RuntimeException("BUG: unexpected index");
		}
		return result;
	}

	@Override
	public int getIndexOfChild (Node a_child) {
		int result;
		if (a_child == tags) {
			result = 0;
		} else if (a_child == dates) {
			result = 1;
		} else {
			throw new RuntimeException("BUG: unknown child");
		}
		return result;
	}

	@Override
	public boolean isLeaf () {
		return false;
	}

	@Override
	public Icon getIcon () {
		URL location = RootNode.class.getClassLoader().getResource("img/boomy/tree/pictures.png");
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
		// no change
	}

}
