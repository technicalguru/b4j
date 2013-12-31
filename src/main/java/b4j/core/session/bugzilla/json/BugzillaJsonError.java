/**
 * 
 */
package b4j.core.session.bugzilla.json;

/**
 * Error object of a Bugzilla JSON call.
 * @author ralph
 *
 */
public class BugzillaJsonError {

	private String code;
	private String message;
	
	/**
	 * Constructor.
	 */
	public BugzillaJsonError(String code, String message) {
		setCode(code);
		setMessage(message);
	}

	/**
	 * Returns the code.
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Returns the message.
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets the message.
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getCode()+" - "+getMessage();
	}

	
}
