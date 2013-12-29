/**
 * 
 */
package b4j.core.session.jira;

import b4j.core.Component;

import com.atlassian.jira.rest.client.domain.BasicComponent;

/**
 * Jira implementation of a {@link Component}.
 * @author ralph
 *
 */
public class JiraComponent implements Component {

	private BasicComponent component;
	
	/**
	 * Constructor.
	 */
	public JiraComponent(BasicComponent component) {
		this.component = component;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return component.getName();
	}

}
