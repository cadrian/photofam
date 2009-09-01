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

import net.cadrian.photofam.ui.ScreenChanges;

import javax.swing.Icon;

interface Node {
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