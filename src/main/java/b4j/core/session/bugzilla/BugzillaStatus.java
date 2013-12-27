/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.Status;

/**
 * Bugzilla implementation of {@link Status}.
 * @author ralph
 *
 */
public class BugzillaStatus implements Status {

	private String status;
	
	/**
	 * Constructor.
	 */
	public BugzillaStatus(String status) {
		this.status = status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen() {
		String name = getName().toLowerCase();
		return  name.indexOf("open") >= 0 || 
				name.equals("new") || 
				name.equals("created") ||
				name.equals("unconfirmed") || 
				name.equals("assigned");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isResolved() {
		String name = getName().toLowerCase();
		return name.equals("resolved") || name.equals("verified") || name.equals("closed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCancelled() {
		String name = getName().toLowerCase();
		return name.startsWith("cancel");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isClosed() {
		String name = getName().toLowerCase();
		return name.equals("closed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDuplicate() {
		String name = getName().toLowerCase();
		return name.startsWith("duplicate");
	}

	
}
