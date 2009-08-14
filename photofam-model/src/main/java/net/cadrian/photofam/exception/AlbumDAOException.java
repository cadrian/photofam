package net.cadrian.photofam.exception;

/**
 * @author Cyril ADRIAN
 */
public class AlbumDAOException extends PhotoFamException {
	private static final long serialVersionUID = 1L;

	/**
	 * @author Cyril ADRIAN
	 */
	public static enum Key implements PhotoFamExceptionKey {
		/**
		 * Initialization Problem
		 */
		InitializationProblem,
		/**
		 * Marshalling problem
		 */
		MarshallProblem;

		@Override
		public String getKey () {
			return AlbumDAOException.class.getName() + "." + name();
		}
	}

	/**
	 * @param key
	 *            the message key
	 */
	public AlbumDAOException (Key key) {
		super(key);
	}

	/**
	 * @param key
	 *            the message key
	 * @param cause
	 *            the cause
	 */
	public AlbumDAOException (Key key, Throwable cause) {
		super(key, cause);
	}
}
