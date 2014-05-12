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
 * Information about a user.
 * @author ralph
 *
 */
public interface User extends BugzillaObject {

	/**
	 * Returns the user ID.
	 */
	public String getId();

	/**
	 * Returns the name (readable user name).
	 */
	public String getName();

	/**
	 * Returns the real name.
	 */
	public String getRealName();

	/**
	 * Returns the team.
	 * @return the team
	 */
	public Team getTeam();
	
	/**
	 * Sets the team
	 */
	public void setTeam(Team team);
}
