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
 * Information about components.
 * @author ralph
 * @since 2.0
 *
 */
public interface Component extends BugzillaObject {

	/**
	 * Returns the project for this component.
	 * 
	 * @return project of component
	 */
	public Project getProject();
	
	/**
	 * Returns the ID.
	 * 
	 * @return ID of component
	 */
	public String getId();

	/**
	 * Returns the name.
	 * 
	 * @return name of component
	 */
	public String getName();

	/**
	 * Returns the description.
	 * 
	 * @return description of component
	 */
	public String getDescription();

}
