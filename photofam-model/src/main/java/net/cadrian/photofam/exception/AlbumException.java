package net.cadrian.photofam.exception;

/**
 * @author Cyril ADRIAN
 */
public class AlbumException extends PhotoFamException {
	private static final long serialVersionUID = 1L;

	/**
	 * @author Cyril ADRIAN
	 */
	public static enum Key implements PhotoFamExceptionKey {
		/**
		 * Invalid album
		 */
		InvalidAlbum;

		@Override
		public String getKey () {
			return AlbumException.class.getName() + "." + name();
		}
	}

	/**
	 * @param key
	 *            the message key
	 * @param albumName
	 *            the album name
	 */
	public AlbumException (Key key, String albumName) {
		super(key, albumName);
	}

	/**
	 * @param key
	 *            the message key
	 * @param albumName
	 *            the album name
	 * @param cause
	 *            the cause
	 */
	public AlbumException (Key key, String albumName, Throwable cause) {
		super(key, albumName, cause);
	}
}
