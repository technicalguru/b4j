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
package b4j.core.session.bugzilla.async;

import java.net.URI;

import b4j.core.session.bugzilla.BugzillaClient;
import b4j.core.session.bugzilla.BugzillaRestClientFactory;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.AuthenticationHandler;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory;

/**
 * The REST client factory.
 * @author ralph
 * @since 2.0
 *
 */
public class AsyncBugzillaRestClientFactory implements BugzillaRestClientFactory {

	@Override
	public BugzillaClient create(URI serverUri, AuthenticationHandler authenticationHandler) {
		final HttpClient httpClient = new AsynchronousHttpClientFactory().createClient(serverUri, authenticationHandler);
		return new AsyncBugzillaRestClient(serverUri, httpClient);
	}

	@Override
	public BugzillaClient createWithBasicHttpAuthentication(URI serverUri, String username, String password) {
		return create(serverUri, new BasicHttpAuthenticationHandler(username, password));
	}

	@Override
	public BugzillaClient create(URI serverUri, HttpClient httpClient) {
		return new AsyncBugzillaRestClient(serverUri, httpClient);
	}

}
