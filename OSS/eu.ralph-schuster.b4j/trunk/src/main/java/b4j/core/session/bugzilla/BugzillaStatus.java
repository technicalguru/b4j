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
package b4j.core.session.bugzilla;

import b4j.core.Status;

/**
 * Bugzilla implementation of {@link Status}.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaStatus implements Status {

	private String status;
	
	/**
	 * Constructor.
	 */
	public BugzillaStatus(String status) {
		this.status = status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen() {
		String name = getName().toLowerCase();
		return  name.indexOf("open") >= 0 || 
				name.equals("new") || 
				name.equals("created") ||
				name.equals("unconfirmed") || 
				name.equals("assigned");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isResolved() {
		String name = getName().toLowerCase();
		return name.equals("resolved") || name.equals("verified") || name.equals("closed");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCancelled() {
		String name = getName().toLowerCase();
		return name.startsWith("cancel");
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
		String name = getName().toLowerCase();
		return name.startsWith("duplicate");
	}

	
}
