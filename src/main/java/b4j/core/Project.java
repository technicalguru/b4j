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

import java.util.Collection;

/**
 * Information about a project or product.
 * @author ralph
 *
 */
public interface Project extends BugzillaObject {

	/**
	 * Returns the name.
	 * @return the name of the project
	 */
	public String getName();

	/**
	 * Returns the ID.
	 * @return the ID of the project
	 */
	public String getId();

	/**
	 * Returns the description.
	 * @return the description of the project
	 */
	public String getDescription();

	/**
	 * Returns the versions.
	 * @return the versions available of the project
	 */
	public Collection<Version> getVersions();
	
	/**
	 * Returns the components.
	 * @return the components of the project
	 */
	public Collection<Component> getComponents();
	
	
}
