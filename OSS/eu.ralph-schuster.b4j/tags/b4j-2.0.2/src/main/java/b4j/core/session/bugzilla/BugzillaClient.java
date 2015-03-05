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


	/**
	 * Returns the Bug client.
	 * @return client for performing operations on bugs
	 */
	public BugzillaBugRestClient getBugClient();

}
