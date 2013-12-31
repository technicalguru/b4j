/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.Priority;

/**
 * Bugzilla implementation of a {@link Priority}.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaPriority implements Priority {

	private String priority;
	
	/**
	 * Constructor.
	 */
	public BugzillaPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return priority;
	}

}
