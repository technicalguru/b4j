/**
 * 
 */
package b4j.core.session.jira;

import com.atlassian.jira.rest.client.domain.BasicStatus;

import b4j.core.Status;

/**
 * Jira implementation of {@link Status}.
 * @author ralph
 *
 */
public class JiraStatus implements Status {

	private BasicStatus status;
	
	/**
	 * Constructor.
	 */
	public JiraStatus(BasicStatus status) {
		this.status = status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return status.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen() {
		String name = getName().toLowerCase();
		return name.indexOf("open") >= 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isResolved() {
		String name = getName().toLowerCase();
		return name.equals("resolved") || name.equals("closed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCancelled() {
		return false;
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
		return false;
	}

	
}
