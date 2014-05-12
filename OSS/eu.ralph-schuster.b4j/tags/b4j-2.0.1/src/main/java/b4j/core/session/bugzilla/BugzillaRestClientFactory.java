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

import java.net.URI;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.AuthenticationHandler;

/**
 * Interface for the REST client factory.
 * @author ralph
 * @since 2.0
 */
public interface BugzillaRestClientFactory {
	/**
	 * Creates an instance of JiraRestClient with default HttpClient settings.
	 *
	 * @param serverUri - URI of JIRA instance.
	 * @param authenticationHandler - requests authenticator.
	 */
	public BugzillaClient create(URI serverUri, AuthenticationHandler authenticationHandler);

	/**
	 * Creates an instance of JiraRestClient with default HttpClient settings. HttpClient will conduct a
	 * basic authentication for given credentials.
	 *
	 * @param serverUri - URI or JIRA instance.
	 * @param username - username of the user used to log in to JIRA.
	 * @param password - password of the user used to log in to JIRA.
	 */
	public BugzillaClient createWithBasicHttpAuthentication(URI serverUri, String username, String password);

	/**
	 * Creates an instance of JiraRestClient with given Atlassian HttpClient.
	 * Please note, that this client has to be fully configured to do the request authentication.
	 *
	 * @param serverUri - URI of JIRA instance.
	 * @param httpClient - instance of Atlassian HttpClient.
	 */
	public BugzillaClient create(URI serverUri, HttpClient httpClient);

}
