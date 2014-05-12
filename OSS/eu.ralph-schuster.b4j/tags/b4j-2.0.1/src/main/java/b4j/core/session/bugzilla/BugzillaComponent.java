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

import rs.baselib.lang.HashCodeUtil;
import b4j.core.AbstractBugzillaObject;
import b4j.core.Component;
import b4j.core.Project;

/**
 * Bugzilla implementation of {@link Component}.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaComponent extends AbstractBugzillaObject implements Component {

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
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, getClass());
		result = HashCodeUtil.hash(result, getProject());
		result = HashCodeUtil.hash(result, getName());
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
		
		if (getProject() == null) {
			if (other.getProject() != null) return false;
		} else if (!getProject().equals(other.getProject())) return false;
		
		if (getName() == null) {
			if (other.getName() != null) return false;
		} else if (!getName().equals(other.getName())) return false;
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
