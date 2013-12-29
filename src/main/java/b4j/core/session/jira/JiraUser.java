/**
 * 
 */
package b4j.core.session.jira;

import b4j.core.Team;
import b4j.core.User;

import com.atlassian.jira.rest.client.domain.BasicUser;

/**
 * Jira implementation of a {@link User}.
 * @author ralph
 *
 */
public class JiraUser implements User {

	private BasicUser user;
	private Team team;
	
	/**
	 * Constructor.
	 */
	public JiraUser(BasicUser user) {
		this.user = user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return user.getDisplayName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return user.getName();
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
