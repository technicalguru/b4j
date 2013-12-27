/**
 * 
 */
package b4j.core.session.jira;

import com.atlassian.jira.rest.client.domain.BasicPriority;

import b4j.core.Severity;

/**
 * Jira implementation of a severity.
 * <p>As Jira doesn't know about severities, this default to priorites.
 * @author ralph
 *
 */
public class JiraSeverity implements Severity {

	private BasicPriority priority;
	
	/**
	 * Constructor.
	 */
	public JiraSeverity(BasicPriority priority) {
		this.priority = priority;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return priority.getName();
	}

}
