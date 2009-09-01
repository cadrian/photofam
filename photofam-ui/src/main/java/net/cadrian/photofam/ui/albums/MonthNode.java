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

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author Cyril ADRIAN
 * 
 */
public class MonthNode implements Node {

	private final String year;
	private final String monthName;

	/**
	 * @param a_year
	 * @param a_month
	 * @param a_monthName
	 * @param a_album
	 * @param a_bundle
	 */
	public MonthNode (String a_year, String a_month, String a_monthName, Album a_album, ResourceBundle a_bundle) {
		year = a_year;
		monthName = a_monthName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node getChild (int a_i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChildCount () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Icon getIcon () {
		URL location = RootNode.class.getClassLoader().getResource("img/boomy/tree/clock.png");
		return new ImageIcon(location);
	}

	@Override
	public int getIndexOfChild (Node a_child) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName () {
		return monthName;
	}

	@Override
	public boolean isLeaf () {
		return false;
	}

	@Override
	public void onSelect (ScreenChanges a_screen) {
		// TODO Auto-generated method stub

	}

}
