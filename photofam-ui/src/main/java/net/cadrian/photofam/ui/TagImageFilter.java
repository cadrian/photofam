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

import net.cadrian.photofam.model.Image;
import net.cadrian.photofam.model.ImageFilter;
import net.cadrian.photofam.model.Tag;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Cyril ADRIAN
 */
final class TagImageFilter implements ImageFilter {
	private final Tag tag;

	TagImageFilter (Tag a_tag) {
		tag = a_tag;
	}

	@Override
	public boolean accept (Image a_image) {
		boolean result = false;
		Collection<Tag> tags = a_image.getTags();
		Set<Tag> cache = new HashSet<Tag>();
		Iterator<Tag> i = tags.iterator();
		while (!result && i.hasNext()) {
			Tag imageTag = i.next();
			while (!result && imageTag != null && !cache.contains(imageTag)) {
				result = tag.equals(imageTag);
				if (!result) {
					cache.add(imageTag);
					imageTag = imageTag.getParent();
				}
			}
		}
		return result;
	}
}
