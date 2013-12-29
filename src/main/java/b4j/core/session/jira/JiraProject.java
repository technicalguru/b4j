/**
 * 
 */
package b4j.core.session.jira;

import com.atlassian.jira.rest.client.domain.BasicProject;

import b4j.core.Project;

/**
 * Jira implementation of a {@link Project}.
 * @author ralph
 *
 */
public class JiraProject implements Project {

	private BasicProject project;
	
	/**
	 * Constructor.
	 */
	public JiraProject(BasicProject project) {
		this.project = project;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return project.getName();
	}

}
