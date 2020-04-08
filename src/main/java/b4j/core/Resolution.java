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
package b4j.core;

/**
 * Information about resolution.
 * @author ralph
 * @since 2.0
 *
 */
public interface Resolution extends BugzillaObject {

	/**
	 * Returns the name.
	 * @return the name of the resolution code
	 */
	public String getName();

	/**
	 * Returns whether the status represents a CANCEL status.
	 * <p>An cancelled status means that the issue was processed but has been marked as abandoned.</p>
	 * @return <code>true</code> when status represents CANCEL
	 */
	public boolean isCancelled();

	/**
	 * Returns whether the status represents a DUPLICATE status.
	 * <p>An duplicate status means that there is another issue of same content.</p>
	 * @return <code>true</code> when status represents DUPLICATE
	 */
	public boolean isDuplicate();
	
	/**
	 * Returns whether the status represents a RESOLVED status.
	 * <p>An resolved status means that the issue was processed successfully and a resolution was found.</p>
	 * @return <code>true</code> when status represents RESOLVED
	 */
	public boolean isResolved();


}
