/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.User;


/**
 * The REST client for Bugzilla.
 * @author ralph
 * @since 2.0
 *
 */
public interface BugzillaClient {

	/**
	 * Performs a user login.
	 *
	 * @param user username
	 * @param password password
	 *  
	 * @return user logged in
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @throws javax.security.auth.login.LoginException when login failed
	 * @since 2.0
	 */
	public User login(String user, String password);

	/**
	 * Returns the user that is currently logged in.
	 * @return the user (or null)
	 */
	public User getUser();

	/**
	 * Logs out.
	 *
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public void logout();

	/**
	 * Returns the Metadata client.
	 * @return client for performing operations on meta data
	 */
	public BugzillaMetadataRestClient getMetadataClient();

	/**
	 * Returns the Classification client.
	 * @return client for performing operations on classifications
	 */
	public BugzillaClassificationRestClient getClassificationClient();

	/**
	 * Returns the Product client.
	 * @return client for performing operations on products
	 */
	public BugzillaProductRestClient getProductClient();

	/**
	 * Returns the User client.
	 * @return client for performing operations on users
	 */
	public BugzillaUserRestClient getUserClient();

}
