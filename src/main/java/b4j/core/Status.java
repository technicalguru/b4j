/**
 * 
 */
package b4j.core;

/**
 * Information about an issue status.
 * @author ralph
 * @since 2.0
 *
 */
public interface Status {

	/**
	 * Returns the name.
	 */
	public String getName();

	/**
	 * Returns whether the status represents an OPEN status.
	 * <p>An open status means that the issue is new and was not yet processed.</p>
	 * @return <code>true</code> when status represents OPEN
	 */
	public boolean isOpen();

	/**
	 * Returns whether the status represents a RESOLVED status.
	 * <p>An resolved status means that the issue was processed successfully and a resolution was found.</p>
	 * @return <code>true</code> when status represents RESOLVED
	 */
	public boolean isResolved();

	/**
	 * Returns whether the status represents a CANCEL status.
	 * <p>An cancelled status means that the issue was processed but has been marked as abandoned.</p>
	 * @return <code>true</code> when status represents CANCEL
	 */
	public boolean isCancelled();

	/**
	 * Returns whether the status represents a CLOSED status.
	 * <p>An closed status means that there is nothing more to be done for the issue.</p>
	 * @return <code>true</code> when status represents CLOSED
	 */
	public boolean isClosed();

	/**
	 * Returns whether the status represents a DUPLICATE status.
	 * <p>An duplicate status means that there is another issue of same content.</p>
	 * @return <code>true</code> when status represents DUPLICATE
	 */
	public boolean isDuplicate();
	
}
