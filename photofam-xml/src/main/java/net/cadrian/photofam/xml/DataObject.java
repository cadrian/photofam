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
package net.cadrian.photofam.xml;

import java.io.InputStream;
import java.io.OutputStream;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.InputSource;

/**
 * @author Cyril ADRIAN
 */
public abstract class DataObject {

	private static final XMLContext context = new XMLContext();

	/**
	 * Serialize the object
	 * 
	 * @param out
	 *            the stream to write the object to
	 * 
	 * @throws MarshalException
	 * @throws ValidationException
	 */
	public void write (OutputStream out) throws MarshalException, ValidationException {
		context.createMarshaller().marshal(this);
	}

	/**
	 * Retrieve an object
	 * 
	 * @param <T>
	 *            the type of the object
	 * @param in
	 *            the stream to read the object from
	 * @param clazz
	 *            the class of the object
	 * 
	 * @return the deserialized object
	 * 
	 * @throws ValidationException
	 * @throws MarshalException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends DataObject> T read (InputStream in, Class<T> clazz) throws MarshalException, ValidationException {
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setClass(clazz);
		return (T) unmarshaller.unmarshal(new InputSource(in));
	}

}
