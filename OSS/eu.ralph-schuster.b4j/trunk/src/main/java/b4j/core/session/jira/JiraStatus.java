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

import com.atlassian.jira.rest.client.domain.BasicStatus;

import b4j.core.AbstractBugzillaObject;
import b4j.core.Status;

/**
 * Jira implementation of {@link Status}.
 * @author ralph
 *
 */
public class JiraStatus extends AbstractBugzillaObject implements Status {

	private BasicStatus status;
	
	/**
	 * Constructor.
	 */
	public JiraStatus(BasicStatus status) {
		this.status = status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return status.getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen() {
		String name = getName().toLowerCase();
		return name.indexOf("open") >= 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isResolved() {
		String name = getName().toLowerCase();
		return name.equals("resolved") || name.equals("closed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCancelled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isClosed() {
		String name = getName().toLowerCase();
		return name.equals("closed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDuplicate() {
		return false;
	}

	
}
