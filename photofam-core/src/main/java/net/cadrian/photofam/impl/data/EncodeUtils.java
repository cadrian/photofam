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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * @author Cyril ADRIAN
 */
public final class EncodeUtils {

	/**
	 * @param pass
	 * @param decoded
	 * @return the encoded bytes
	 * @throws Exception
	 */
	public static byte[] encode (String pass, byte[] decoded) throws Exception {
		Cipher cipher = Cipher.getInstance("DES"); // cipher is not thread safe
		cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(pass));
		return cipher.doFinal(decoded);
	}

	/**
	 * @param pass
	 * @param encoded
	 * @return the decoded bytes
	 * @throws Exception
	 */
	public static byte[] decode (String pass, byte[] encoded) throws Exception {
		Cipher cipher = Cipher.getInstance("DES");// cipher is not thread safe
		cipher.init(Cipher.DECRYPT_MODE, getSecretKey(pass));
		return cipher.doFinal(encoded);
	}

	private static SecretKey getSecretKey (String pass) throws InvalidKeyException, UnsupportedEncodingException,
			NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] passbytes = pass.getBytes("UTF8");
		if (passbytes.length < 8) {
			byte[] b = new byte[8];
			System.arraycopy(passbytes, 0, b, 0, passbytes.length);
			passbytes = b;
		}
		DESKeySpec keySpec = new DESKeySpec(passbytes);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		return keyFactory.generateSecret(keySpec);
	}

}
