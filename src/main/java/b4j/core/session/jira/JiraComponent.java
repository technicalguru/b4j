/**
 * 
 */
package b4j.core.session.jira;

import b4j.core.Component;
import b4j.core.Project;

import com.atlassian.jira.rest.client.domain.BasicComponent;

/**
 * Jira implementation of a {@link Component}.
 * @author ralph
 *
 */
public class JiraComponent implements Component {

	private BasicComponent component;
	private Project project;
	
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return component.getId().toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return component.getDescription();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProject() {
		return project;
	}

	/**
	 * Sets the project.
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	
}
