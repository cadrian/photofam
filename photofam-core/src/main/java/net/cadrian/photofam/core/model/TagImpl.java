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
package net.cadrian.photofam.core.model;

import net.cadrian.photofam.model.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Cyril ADRIAN
 */
class TagImpl implements Tag, Serializable {

	static final char TAG_NAME_SEPARATOR = '/';

	private static final long serialVersionUID = -6306637635637593226L;

	private final String name;
	private final TagImpl parent;
	private transient Set<Tag> children;
	private transient String completeName;

	TagImpl (String a_name, TagImpl a_parent) {
		name = a_name;
		parent = a_parent;
		if (parent != null) {
			parent.addChild(this);
		}
	}

	void addChild (TagImpl child) {
		if (children == null) {
			children = new TreeSet<Tag>();
		}
		children.add(child);
	}

	@Override
	public int compareTo (Tag other) {
		int result = getCompleteName().compareToIgnoreCase(other.getCompleteName());
		if (result == 0) {
			result = getCompleteName().compareTo(other.getCompleteName());
		}
		return result;
	}

	@Override
	public boolean equals (Object a_obj) {
		boolean result;
		if (a_obj instanceof Tag) {
			result = compareTo((Tag) a_obj) == 0;
		} else {
			result = false;
		}
		return result;
	}

	@Override
	public int hashCode () {
		return getCompleteName().hashCode();
	}

	@Override
	public List<Tag> getChildren () {
		List<Tag> result;
		if (children == null) {
			result = Collections.emptyList();
		} else {
			result = new ArrayList<Tag>(children);
		}
		return result;
	}

	@Override
	public String getCompleteName () {
		String result = completeName;
		if (result == null) {
			StringBuilder buf = new StringBuilder();
			fillName(buf);
			completeName = result = buf.toString();
		}
		return result;
	}

	void fillName (StringBuilder b) {
		if (parent != null) {
			(parent).fillName(b);
			b.append(TAG_NAME_SEPARATOR);
		}
		b.append(name);
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public Tag getParent () {
		return parent;
	}

	@Override
	public String toString () {
		return '<' + getCompleteName() + '>';
	}

	/**
	 * @param a_name
	 *            the would-be tag name
	 * 
	 * @return the name of the would-be parent of the tag which name is given
	 */
	public static String getParentTagName (String a_name) {
		String result = null;
		if (a_name != null) {
			int i = a_name.lastIndexOf(TAG_NAME_SEPARATOR);
			if (i != -1) {
				result = a_name.substring(0, i);
			}
		}
		return result;
	}

	/**
	 * @param a_name
	 *            the would-be tag name
	 * 
	 * @return the short name of the would-be tag
	 */
	public static String getShortName (String a_name) {
		String result = null;
		if (a_name != null) {
			int i = a_name.lastIndexOf(TAG_NAME_SEPARATOR);
			if (i != -1) {
				result = a_name.substring(i + 1);
			} else {
				result = a_name;
			}
		}
		return result;
	}

}
