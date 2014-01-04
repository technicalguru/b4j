/**
 * 
 */
package b4j.core.session.jira;

import java.util.Date;

import b4j.core.Version;

/**
 * Jira implementation of a {@link Project}.
 * @author ralph
 *
 */
public class JiraVersion implements Version {

	private com.atlassian.jira.rest.client.domain.Version version;
	
	/**
	 * Constructor.
	 */
	public JiraVersion(com.atlassian.jira.rest.client.domain.Version version) {
		this.version = version;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return version.getName();
	}

	/**
	 * Returns the Id
	 */
	public Long getId() {
		return version.getId();
	}

	/**
	 * Returns the description.
	 */
	public String getDescription() {
		return version.getDescription();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getReleaseDate() {
		return new Date(version.getReleaseDate().getMillis());
	}

	
}
