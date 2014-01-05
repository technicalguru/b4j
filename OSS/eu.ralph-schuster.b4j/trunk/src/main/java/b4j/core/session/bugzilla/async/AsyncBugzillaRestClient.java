/**
 * 
 */
package b4j.core.session.bugzilla.async;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import b4j.core.User;
import b4j.core.session.bugzilla.BugzillaBugRestClient;
import b4j.core.session.bugzilla.BugzillaClassificationRestClient;
import b4j.core.session.bugzilla.BugzillaClient;
import b4j.core.session.bugzilla.BugzillaLazyRetriever;
import b4j.core.session.bugzilla.BugzillaMetadataRestClient;
import b4j.core.session.bugzilla.BugzillaProductRestClient;
import b4j.core.session.bugzilla.BugzillaUserRestClient;
import b4j.util.LazyRetriever;

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
	private AsyncBugzillaClassificationRestClient classificationClient;
	private AsyncBugzillaProductRestClient productClient;
	private AsyncBugzillaUserRestClient userClient;
	private AsyncBugzillaBugRestClient bugClient;
	private LazyRetriever lazyRetriever;
	private User user;
	
	/**
	 * Constructor.
	 */
	public AsyncBugzillaRestClient(URI serverUri, AuthenticationHandler authenticationHandler) {
		this(serverUri, new AsynchronousHttpClientFactory().createClient(serverUri, authenticationHandler));
	}

	public AsyncBugzillaRestClient(URI serverUri, HttpClient httpClient) {
		URI baseUri = UriBuilder.fromUri(serverUri).path("/jsonrpc.cgi").build();
		lazyRetriever = new BugzillaLazyRetriever(this);
		metadataClient = new AsyncBugzillaMetadataRestClient(baseUri, httpClient, lazyRetriever);
		classificationClient = new AsyncBugzillaClassificationRestClient(baseUri, httpClient, lazyRetriever);
		productClient = new AsyncBugzillaProductRestClient(baseUri, httpClient, lazyRetriever);
		userClient = new AsyncBugzillaUserRestClient(baseUri, httpClient, lazyRetriever);
		bugClient = new AsyncBugzillaBugRestClient(baseUri, httpClient, lazyRetriever);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public User login(String user, String password) {
		this.user = userClient.login(user, password);
		return this.user;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logout() {
		userClient.logout();
		this.user = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BugzillaMetadataRestClient getMetadataClient() {
		return metadataClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BugzillaClassificationRestClient getClassificationClient() {
		return classificationClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BugzillaProductRestClient getProductClient() {
		return productClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BugzillaUserRestClient getUserClient() {
		return userClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BugzillaBugRestClient getBugClient() {
		return bugClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUser() {
		return user;
	}

	
}