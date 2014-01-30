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

import com.atlassian.jira.rest.client.domain.BasicResolution;

import b4j.core.AbstractBugzillaObject;
import b4j.core.Resolution;

/**
 * Represents resolutions from JIRA.
 * @author ralph
 *
 */
public class JiraResolution extends AbstractBugzillaObject implements Resolution {

	private BasicResolution resolution;
	
	/**
	 * Constructor.
	 */
	public JiraResolution(BasicResolution resolution) {
		this.resolution = resolution;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return resolution.getName();
	}

	// Fixed, Won't Fix, Duplicate, Incomplete, Cannot Reproduce, Done
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCancelled() {
		String name = getName().toLowerCase();
		return name.startsWith("won't fix") || name.startsWith("cannot reproduce");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDuplicate() {
		String name = getName().toLowerCase();
		return name.startsWith("duplicate");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isResolved() {
		String name = getName().toLowerCase();
		return name.startsWith("fixed") || name.startsWith("done");
	}

}
