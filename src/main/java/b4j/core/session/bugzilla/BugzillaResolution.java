/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.Resolution;

/**
 * Represents resolutions from JIRA.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaResolution implements Resolution {

	private String resolution;
	
	/**
	 * Constructor.
	 */
	public BugzillaResolution(String resolution) {
		this.resolution = resolution;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return resolution;
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
	public boolean isDuplicate() {
		String name = getName().toLowerCase();
		return name.startsWith("duplicate");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isResolved() {
		String name = getName().toLowerCase();
		return name.equals("resolved") || name.equals("verified") || name.equals("closed") || name.startsWith("cancel");
	}

}
