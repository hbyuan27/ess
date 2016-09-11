package hcm.ess.exception;

public class ServiceSystemException extends RuntimeException {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3715224233155860902L;

	/**
	 * Creates a new <code>ServiceSystemException</code> with the specified
	 * detail message.
	 *
	 * @param message
	 *            the detail message
	 */
	public ServiceSystemException(String message) {
		super(message);
	}

	/**
	 * Creates a new <code>ServiceSystemException</code> with the specified
	 * detail message and cause.
	 *
	 * @param message
	 *            the detail mesage
	 * @param cause
	 *            the cause
	 */
	public ServiceSystemException(String message, Throwable cause) {
		super(message, cause);
	}
}
