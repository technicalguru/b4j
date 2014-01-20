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

import b4j.core.Component;
import b4j.core.Project;

/**
 * Bugzilla implementation of {@link Component}.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaComponent implements Component {

	private String id;
	private String name;
	private String description;
	private BugzillaProject project;
	
	/**
	 * Constructor.
	 */
	public BugzillaComponent(String name) {
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProject() {
		return project;
	}

	/**
	 * Sets the project.
	 * @param project the project to set
	 */
	public void setProject(BugzillaProject project) {
		this.project = project;
	}

	/**
	 * Returns the best attribute for using in {@link #hashCode()} and {@link #equals(Object)} methods.
	 * @return {@link #id} or {@link #name}
	 */
	private Object getHashAttribute() {
		if (getId() != null) return getHashAttribute();
		return getName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getHashAttribute() == null) ? 0 : getHashAttribute().hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		BugzillaComponent other = (BugzillaComponent) obj;
		if (getHashAttribute() == null) {
			if (other.getHashAttribute() != null) return false;
		} else if (!getHashAttribute().equals(other.getHashAttribute())) return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getName();
	}


	
}
