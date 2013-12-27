/**
 * 
 */
package b4j.core.session.jira;

import com.atlassian.jira.rest.client.domain.BasicPriority;

import b4j.core.Priority;

/**
 * Jira implementation of a {@link Priority}.
 * @author ralph
 *
 */
public class JiraPriority implements Priority {

	private BasicPriority priority;
	
	/**
	 * Constructor.
	 */
	public JiraPriority(BasicPriority priority) {
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
