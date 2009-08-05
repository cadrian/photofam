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

import net.cadrian.photofam.services.album.Tag;
import net.cadrian.photofam.xml.rawalbum.Parent;
import net.cadrian.photofam.xml.rawalbum.TagType;

import java.io.Serializable;

/**
 * @author Cyril ADRIAN
 */
class TagImpl implements Tag, Serializable {

	private static final long serialVersionUID = -6306637635637593226L;

	private final String name;
	private final Tag parent;

	/**
	 * @param a_name
	 *            name
	 * @param a_parent
	 *            parent
	 */
	public TagImpl (String a_name, Tag a_parent) {
		name = a_name;
		parent = a_parent;
	}

	TagImpl (TagType a_tag) {
		name = a_tag.getName();
		Parent p = a_tag.getParent();
		parent = p == null ? null : new TagImpl(p);
	}

	@Override
	public String getCompleteName () {
		StringBuilder result = new StringBuilder();
		fillName(result);
		return result.toString();
	}

	void fillName (StringBuilder b) {
		if (parent != null) {
			((TagImpl) parent).fillName(b);
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

	net.cadrian.photofam.xml.rawalbum.Tag getCastorTag () {
		net.cadrian.photofam.xml.rawalbum.Tag result = new net.cadrian.photofam.xml.rawalbum.Tag();
		fillCastorTag(result);
		return result;
	}

	void fillCastorTag (TagType tag) {
		tag.setName(name);
		if (parent != null) {
			Parent p = new Parent();
			((TagImpl) parent).fillCastorTag(p);
			tag.setParent(p);
		}
	}

}
