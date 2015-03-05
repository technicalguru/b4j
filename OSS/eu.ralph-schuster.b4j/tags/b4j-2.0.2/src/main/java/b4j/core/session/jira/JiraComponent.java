/*
 * This file is part of Bugzilla for Java.
 *
 *  Bugzilla for Java is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  Bugzilla for Java is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with Bugzilla for Java.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package b4j.core.session.jira;

import b4j.core.AbstractBugzillaObject;
import b4j.core.Component;
import b4j.core.Project;

import com.atlassian.jira.rest.client.domain.BasicComponent;

/**
 * Jira implementation of a {@link Component}.
 * @author ralph
 *
 */
public class JiraComponent extends AbstractBugzillaObject implements Component {

	private BasicComponent component;
	private Project project;
	
	/**
	 * Constructor.
	 */
	public JiraComponent(BasicComponent component) {
		this(null, component);
	}

	/**
	 * Constructor.
	 */
	public JiraComponent(Project project, BasicComponent component) {
		this.component = component;
		this.project = project;
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
