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

import com.atlassian.jira.rest.client.domain.BasicPriority;

import b4j.core.AbstractBugzillaObject;
import b4j.core.Severity;

/**
 * Jira implementation of a severity.
 * <p>As Jira doesn't know about severities, this default to priorites.
 * @author ralph
 *
 */
public class JiraSeverity extends AbstractBugzillaObject implements Severity {

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
