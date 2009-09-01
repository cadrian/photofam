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
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cyril ADRIAN
 */
class DatesNode implements Node {

	private static final Logger log = LoggerFactory.getLogger(DatesNode.class);

	private static final DateFormat YEARS = new SimpleDateFormat("yyyy");

	private final ResourceBundle bundle;

	private final List<YearNode> children;

	DatesNode (Album a_album, ResourceBundle a_bundle) {
		Set<Integer> years = new TreeSet<Integer>();
		for (Image img : a_album.getImages(ImageFilter.ALL)) {
			Metadata metadata = img.getMetadata();
			if (metadata != null) {
				TypedMetadataKey<Date> dateKey = metadata.getDate();
				if (dateKey != null) {
					for (Date date : dateKey.getValues()) {
						if (log.isDebugEnabled()) {
							log.debug("Image: " + img.getPath() + " - Date: " + date);
						}
						years.add(Integer.decode(YEARS.format(date)));
					}
				}
			}
		}
		children = new ArrayList<YearNode>(years.size());
		for (Integer year : years) {
			children.add(new YearNode(year.toString(), a_album, a_bundle));
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
		return bundle.containsKey("node.dates") ? bundle.getString("node.dates") : "Dates";
	}

	@Override
	public String toString () {
		return getName();
	}

	@Override
	public boolean isLeaf () {
		return false;
	}

	@Override
	public void onSelect (ScreenChanges a_screen) {
		assert false;
	}

}
