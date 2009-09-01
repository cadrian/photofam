package net.cadrian.photofam.exception;

import java.io.File;

/**
 * @author Cyril ADRIAN
 */
public class ImageException extends PhotoFamException {
	private static final long serialVersionUID = 1L;

	/**
	 * @author Cyril ADRIAN
	 */
	public static enum Key implements PhotoFamExceptionKey {
		/**
		 * Invalid image
		 */
		InvalidImage,

		/**
		 * Invalid image metadata
		 */
		InvalidMetadata;

		@Override
		public String getKey () {
			return ImageException.class.getName() + "." + name();
		}
	}

	/**
	 * @param key
	 *            the message key
	 * @param image
	 *            the image file
	 */
	public ImageException (Key key, File image) {
		super(key, image.getPath());
	}

	/**
	 * @param key
	 *            the message key
	 * @param image
	 *            the image file
	 * @param cause
	 *            the cause
	 */
	public ImageException (Key key, File image, Throwable cause) {
		super(key, cause, image.getPath());
	}
}
