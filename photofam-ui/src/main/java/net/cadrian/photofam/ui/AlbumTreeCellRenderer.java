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

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * @author Cyril ADRIAN
 * 
 */
class AlbumTreeCellRenderer extends DefaultTreeCellRenderer {

	private final Albums model;

	/**
	 * @param a_model
	 */
	public AlbumTreeCellRenderer (Albums a_model) {
		model = a_model;
	}

	@Override
	public Component getTreeCellRendererComponent (JTree a_tree, Object a_value, boolean a_selected, boolean a_expanded,
			boolean a_leaf, int a_row, boolean a_hasFocus) {
		super.getTreeCellRendererComponent(a_tree, a_value, a_selected, a_expanded, a_leaf, a_row, a_hasFocus);
		setIcon(((Albums.Node) a_value).getIcon());
		return this;
	}

}
