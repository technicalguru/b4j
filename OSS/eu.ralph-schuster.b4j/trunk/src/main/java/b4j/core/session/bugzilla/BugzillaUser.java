/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.Team;
import b4j.core.User;

/**
 * Bugzilla implementation of {@link User}.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaUser implements User {

	private String id;
	private String name;
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

	
}
