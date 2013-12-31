/**
 * 
 */
package b4j.core.session.bugzilla.async;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import b4j.core.session.bugzilla.BugzillaMetadataRestClient;
import b4j.core.session.bugzilla.BugzillaClient;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.AuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory;

/**
 * Asynchronous REST client on Bugzilla.
 * @author ralph
 * @since 2.0
 *
 */
public class AsyncBugzillaRestClient implements BugzillaClient {

	private AsyncBugzillaMetadataRestClient metadataClient;
	
	/**
	 * Constructor.
	 */
	public AsyncBugzillaRestClient(URI serverUri, AuthenticationHandler authenticationHandler) {
		this(serverUri, new AsynchronousHttpClientFactory().createClient(serverUri, authenticationHandler));
	}

	public AsyncBugzillaRestClient(URI serverUri, HttpClient httpClient) {
		URI baseUri = UriBuilder.fromUri(serverUri).path("/jsonrpc.cgi").build();

		metadataClient = new AsyncBugzillaMetadataRestClient(baseUri, httpClient);
	}
	
	@Override
	public BugzillaMetadataRestClient getMetadataClient() {
		return metadataClient;
	}

}
