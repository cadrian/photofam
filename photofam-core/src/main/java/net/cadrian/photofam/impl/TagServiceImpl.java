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
package net.cadrian.photofam.impl;

import net.cadrian.photofam.impl.data.TagImpl;
import net.cadrian.photofam.services.TagService;
import net.cadrian.photofam.services.album.Tag;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Cyril ADRIAN
 */
public class TagServiceImpl implements TagService {

	private final Map<String, TagImpl> tags = new HashMap<String, TagImpl>();

	@Override
	public Tag getTag (String a_completeName) {
		TagImpl result = tags.get(a_completeName);
		if (result == null) {
			createTag(a_completeName);
			result = tags.get(a_completeName);
		}
		return result;
	}

	@Override
	public void addTag (Tag a_tag) {
		assert !tags.containsKey(a_tag.getCompleteName());

		tags.put(a_tag.getCompleteName(), (TagImpl) a_tag);
	}

	private void createTag (String a_completeName) {
		int i = a_completeName.lastIndexOf('/');
		if (i == -1) {
			TagImpl.createTag(a_completeName, null, this);
		} else {
			String parentName = a_completeName.substring(0, i);
			TagImpl parent = tags.get(parentName);
			if (parent == null) {
				createTag(parentName);
				parent = tags.get(parentName);
			}
			String name = a_completeName.substring(i + 1);
			TagImpl.createTag(name, parent, this);
		}
	}

}
