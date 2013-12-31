/**
 * 
 */
package b4j.core.session.jira;

import java.util.Collection;
import java.util.Collections;

import com.atlassian.jira.rest.client.domain.BasicProject;

import b4j.core.Component;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return project.getKey();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return project.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getVersions() {
		// TODO 
		return Collections.emptyList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Component> getComponents() {
		// TODO
		return Collections.emptyList();
	}

	
}
