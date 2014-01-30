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
import b4j.core.Team;
import b4j.core.User;

import com.atlassian.jira.rest.client.domain.BasicUser;

/**
 * Jira implementation of a {@link User}.
 * @author ralph
 *
 */
public class JiraUser extends AbstractBugzillaObject implements User {

	private BasicUser user;
	private Team team;
	
	/**
	 * Constructor.
	 */
	public JiraUser(BasicUser user) {
		this.user = user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return user.getDisplayName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return user.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Team getTeam() {
		return team;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTeam(Team team) {
		this.team = team;
	}

	
}
