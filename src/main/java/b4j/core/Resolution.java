/**
 * 
 */
package b4j.core;

/**
 * Information about resolution.
 * @author ralph
 * @since 2.0
 *
 */
public interface Resolution {

	/**
	 * Returns the name.
	 */
	public String getName();

	/**
	 * Returns whether the status represents a CANCEL status.
	 * <p>An cancelled status means that the issue was processed but has been marked as abandoned.</p>
	 * @return <code>true</code> when status represents CANCEL
	 */
	public boolean isCancelled();

	/**
	 * Returns whether the status represents a DUPLICATE status.
	 * <p>An duplicate status means that there is another issue of same content.</p>
	 * @return <code>true</code> when status represents DUPLICATE
	 */
	public boolean isDuplicate();
	
	/**
	 * Returns whether the status represents a RESOLVED status.
	 * <p>An resolved status means that the issue was processed successfully and a resolution was found.</p>
	 * @return <code>true</code> when status represents RESOLVED
	 */
	public boolean isResolved();


}
