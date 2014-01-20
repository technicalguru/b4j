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

import java.util.Date;

import b4j.core.Project;
import b4j.core.Version;

/**
 * Bugzilla implementation of a {@link Version}.
 * @author ralph
 *
 */
public class BugzillaVersion implements Version {

	private Project project;
	private Long id;
	private String name;
	private Date releaseDate;
	
	/**
	 * Constructor.
	 */
	public BugzillaVersion() {
		this(null, null, null);
	}

	/**
	 * Constructor.
	 */
	public BugzillaVersion(Long id, Project project, String name) {
		setId(id);
		setProject(project);
		setName(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProject() {
		return project;
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
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Sets the project.
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the releaseDate.
	 * @param releaseDate the releaseDate to set
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * Returns the best attribute for using in {@link #hashCode()} and {@link #equals(Object)} methods.
	 * @return {@link #id} or {@link #name}
	 */
	private Object getHashAttribute() {
		if (getId() != null) return getId();
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
		BugzillaVersion other = (BugzillaVersion) obj;
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
