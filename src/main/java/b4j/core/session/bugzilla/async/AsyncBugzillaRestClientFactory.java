/**
 * 
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
