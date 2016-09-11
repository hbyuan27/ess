package hcm.ess.exception;

public class ServiceApplicationException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -632904653752775154L;

	private int code = -1005;

	/**
	 * Creates a new <code>ServiceApplicationException</code> with the specified
	 * detail message.
	 *
	 * @param message
	 *            the detail message
	 */
	public ServiceApplicationException(String message) {
		super(message);
	}

	/**
	 * Creates a new <code>ServiceApplicationException</code> with the specified
	 * detail message.
	 *
	 * @param message
	 *            the detail message
	 */
	public ServiceApplicationException(int code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * Creates a new <code>ServiceApplicationException</code> with the specified
	 * detail message and cause.
	 *
	 * @param message
	 *            the detail mesage
	 * @param cause
	 *            the cause
	 */
	public ServiceApplicationException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	/**
	 * Creates a new <code>ServiceApplicationException</code> with the specified
	 * detail message and cause.
	 *
	 * @param message
	 *            the detail mesage
	 * @param cause
	 *            the cause
	 */
	public ServiceApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
}
