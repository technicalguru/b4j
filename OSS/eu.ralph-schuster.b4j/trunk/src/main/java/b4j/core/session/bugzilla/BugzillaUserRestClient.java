/**
 * 
 */
package b4j.core.session.bugzilla;

import java.util.Collection;

import b4j.core.User;

import com.atlassian.util.concurrent.Promise;

/**
 * Interface for user REST clients.
 * @author ralph
 * @since 2.0
 *
 */
public interface BugzillaUserRestClient {

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
