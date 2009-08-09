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
package net.cadrian.photofam.impl.data;

import net.cadrian.photofam.services.TagService;
import net.cadrian.photofam.services.album.Tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Cyril ADRIAN
 */
public class TagImpl implements Tag, Serializable, Comparable<TagImpl> {

	private static final long serialVersionUID = -6306637635637593226L;

	private final String name;
	private final TagImpl parent;
	private transient Set<Tag> children;

	/**
	 * Add a tag to the tag service
	 * 
	 * @param a_name
	 *            the name of the tag
	 * @param a_parent
	 *            the parent of the tag
	 * @param a_tagService
	 *            the tag service
	 */
	public static void createTag (String a_name, Tag a_parent, TagService a_tagService) {
		a_tagService.addTag(new TagImpl(a_name, (TagImpl) a_parent));
	}

	private TagImpl (String a_name, TagImpl a_parent) {
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
	public int compareTo (TagImpl other) {
		int result;
		if (other.parent == parent) {
			result = 0;
		} else if (parent == null) {
			result = 1;
		} else if (other.parent == null) {
			result = -1;
		} else {
			result = parent.compareTo(other.parent);
		}
		if (result == 0) {
			result = name.compareTo(other.name);
		}
		return result;
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
		StringBuilder result = new StringBuilder();
		fillName(result);
		return result.toString();
	}

	void fillName (StringBuilder b) {
		if (parent != null) {
			(parent).fillName(b);
			b.append('/');
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

}
