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

import net.cadrian.photofam.model.metadata.TypedMetadataKey;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Cyril ADRIAN
 */
class TypedMetadataKeyImpl<S> implements TypedMetadataKey<S> {

	private final String name;
	private final Set<S> values;

	public TypedMetadataKeyImpl (String a_name, S... a_values) {
		this(a_name, Arrays.asList(a_values));
	}

	public TypedMetadataKeyImpl (String a_name, List<S> a_values) {
		assert a_values.size() > 0;

		name = a_name;
		values = Collections.unmodifiableSet(new HashSet<S>(a_values));
	}

	@Override
	public Set<S> getValues () {
		return values;
	}

	@Override
	public String getName () {
		return name;
	}

}
