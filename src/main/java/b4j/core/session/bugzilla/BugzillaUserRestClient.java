/**
 * 
 */
package b4j.core.session.bugzilla;

import java.util.Collection;

import b4j.core.User;

import com.atlassian.util.concurrent.Promise;

/**
 * Interface for user REST clients.
 * @see <a href="http://www.bugzilla.org/docs/4.4/en/html/api/Bugzilla/WebService/User.html">Bugzilla::WebService::User</a>
 * @author ralph
 * @since 2.0
 *
 */
public interface BugzillaUserRestClient {

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
	 * Logs out.
	 *
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public void logout();

	/**
	 * Retrieves information about users.
	 *
	 * @param ids IDs of users
	 * @return information about users
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<User>> getUsers(long... ids);

	/**
	 * Retrieves information about users.
	 *
	 * @param names names of users
	 * @return information about users
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<User>> getUsersByName(String... names);

	/**
	 * Retrieves information about users.
	 *
	 * @param ids IDs of users
	 * @return information about users
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<User>> getUsers(Collection<Long> ids);

	/**
	 * Retrieves information about users.
	 *
	 * @param names names of users
	 * @return information about users
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<User>> getUsersByName(Collection<String> names);

}
