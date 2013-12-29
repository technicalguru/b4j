/**
 * 
 */
package b4j.core;

/**
 * Information about a user.
 * @author ralph
 *
 */
public interface User {

	/**
	 * Returns the user ID.
	 */
	public String getId();

	/**
	 * Returns the name (readable user name).
	 */
	public String getName();

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
