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
import net.cadrian.photofam.model.Image;
import net.cadrian.photofam.model.ImageFilter;
import net.cadrian.photofam.model.Metadata;
import net.cadrian.photofam.model.metadata.TypedMetadataKey;
import net.cadrian.photofam.ui.ScreenChanges;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * @author Cyril ADRIAN
 */
public class YearNode implements Node {

	private static final DateFormat YEARS = new SimpleDateFormat("yyyy");
	private static final DateFormat MONTHS = new SimpleDateFormat("MM");
	private static final DateFormat MONTH_NAMES = new SimpleDateFormat("MMMM");

	private final String year;
	private final List<MonthNode> children;
	private final ResourceBundle bundle;

	/**
	 * @param a_year
	 * @param a_album
	 * @param a_bundle
	 */
	public YearNode (String a_year, Album a_album, ResourceBundle a_bundle) {
		year = a_year;
		Map<Integer, String> months = new TreeMap<Integer, String>();
		for (Image img : a_album.getImages(ImageFilter.ALL)) {
			Metadata metadata = img.getMetadata();
			if (metadata != null) {
				TypedMetadataKey<Date> dateKey = metadata.getDate();
				if (dateKey != null) {
					Date date = dateKey.getValues().iterator().next();
					if (a_year.equals(YEARS.format(date))) {
						months.put(Integer.decode(MONTHS.format(date)), MONTH_NAMES.format(date));
					}
				}
			}
		}
		children = new ArrayList<MonthNode>(months.size());
		for (Map.Entry<Integer, String> month : months.entrySet()) {
			children.add(new MonthNode(a_year, month.getKey().toString(), month.getValue(), a_album, a_bundle));
		}
		bundle = a_bundle;
	}

	@Override
	public Node getChild (int i) {
		return children.get(i);
	}

	@Override
	public int getChildCount () {
		return children.size();
	}

	@Override
	public Icon getIcon () {
		URL location = RootNode.class.getClassLoader().getResource("img/boomy/tree/clock.png");
		return new ImageIcon(location);
	}

	@Override
	public int getIndexOfChild (Node a_child) {
		return children.indexOf(a_child);
	}

	@Override
	public String getName () {
		return year;
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
