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
