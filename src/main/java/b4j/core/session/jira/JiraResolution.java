/**
 * 
 */
package b4j.core.session.jira;

import com.atlassian.jira.rest.client.domain.BasicResolution;

import b4j.core.Resolution;

/**
 * Represents resolutions from JIRA.
 * @author ralph
 *
 */
public class JiraResolution implements Resolution {

	private BasicResolution resolution;
	
	/**
	 * Constructor.
	 */
	public JiraResolution(BasicResolution resolution) {
		this.resolution = resolution;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return resolution.getName();
	}

	// Fixed, Won't Fix, Duplicate, Incomplete, Cannot Reproduce, Done
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCancelled() {
		String name = getName().toLowerCase();
		return name.startsWith("won't fix") || name.startsWith("cannot reproduce");
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
		return name.startsWith("fixed") || name.startsWith("done");
	}

}
