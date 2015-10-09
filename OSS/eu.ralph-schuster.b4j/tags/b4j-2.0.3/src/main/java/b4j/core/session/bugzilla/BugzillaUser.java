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

import b4j.core.AbstractBugzillaObject;
import b4j.core.Team;
import b4j.core.User;

/**
 * Bugzilla implementation of {@link User}.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaUser extends AbstractBugzillaObject implements User {

	private String id;
	private String name;
	private String realName;
	private Team team;
	
	/**
	 * Constructor.
	 */
	public BugzillaUser() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	public Team getTeam() {
		return team;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * Returns the realName.
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * Sets the realName.
	 * @param realName the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
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
		BugzillaUser other = (BugzillaUser) obj;
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
