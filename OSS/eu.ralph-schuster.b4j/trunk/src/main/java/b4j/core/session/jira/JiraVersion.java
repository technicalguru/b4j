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

import java.util.Date;

import b4j.core.AbstractBugzillaObject;
import b4j.core.Project;
import b4j.core.Version;

/**
 * Jira implementation of a {@link Project}.
 * @author ralph
 *
 */
public class JiraVersion extends AbstractBugzillaObject implements Version {

	private Project project;
	private com.atlassian.jira.rest.client.domain.Version version;
	
	/**
	 * Constructor.
	 */
	public JiraVersion(Project project, com.atlassian.jira.rest.client.domain.Version version) {
		this.project = project;
		this.version = version;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProject() {
		return project;
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

	/**
	 * Sets the project.
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	
}
