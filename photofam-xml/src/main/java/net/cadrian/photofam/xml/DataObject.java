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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * @author Cyril ADRIAN
 */
public abstract class DataObject {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final XMLContext context = new XMLContext();

	private static final class LogWriter extends Writer {
		private final Writer out;
		private final StringBuilder log = new StringBuilder();

		LogWriter (Writer a_out) {
			out = a_out;
		}

		@Override
		public void write (char[] cbuf, int off, int len) throws IOException {
			out.write(cbuf, off, len);
			log.append(cbuf, off, len);
		}

		@Override
		public void close () throws IOException {
			out.close();
		}

		@Override
		public void flush () throws IOException {
			out.flush();
		}

		String getLog () {
			return log.toString();
		}
	}

	private static final class LogReader extends Reader {
		private final Reader in;
		private final StringBuilder log = new StringBuilder();

		LogReader (Reader a_in) {
			in = a_in;
		}

		@Override
		public void close () throws IOException {
			in.close();
		}

		@Override
		public int read (char[] cbuf, int off, int len) throws IOException {
			int result = in.read(cbuf, off, len);
			if (result != -1) {
				log.append(cbuf, off, result);
			}
			return result;
		}

		String getLog () {
			return log.toString();
		}

	}

	/**
	 * Serialize the object
	 * 
	 * @param out
	 *            the stream to write the object to
	 * 
	 * @throws MarshalException
	 * @throws ValidationException
	 * @throws IOException
	 */
	public void write (OutputStream out) throws MarshalException, ValidationException, IOException {
		Marshaller marshaller = context.createMarshaller();
		if (log.isDebugEnabled()) {
			LogWriter debug = new LogWriter(new OutputStreamWriter(out));
			marshaller.setWriter(debug);
			marshaller.marshal(this);
			String dlog = debug.getLog();
			if (dlog.length() > 1024) {
				dlog = dlog.substring(0, 1021) + "...";
			}
			log.debug(dlog);
		} else {
			marshaller.setWriter(new OutputStreamWriter(out));
			marshaller.marshal(this);
		}
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
		T result;
		Logger log = LoggerFactory.getLogger(clazz);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setClass(clazz);
		if (log.isDebugEnabled()) {
			LogReader debug = new LogReader(new InputStreamReader(in));
			result = (T) unmarshaller.unmarshal(new InputSource(debug));
			String dlog = debug.getLog();
			if (dlog.length() > 1024) {
				dlog = dlog.substring(0, 1021) + "...";
			}
			log.debug(dlog);
		} else {
			result = (T) unmarshaller.unmarshal(new InputSource(in));
		}
		return result;
	}

}
