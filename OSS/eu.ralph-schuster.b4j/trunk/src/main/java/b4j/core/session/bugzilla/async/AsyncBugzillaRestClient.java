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
	private HttpClient httpClient;
	private URI serverUri;
	private URI baseUri;
	
	/**
	 * Constructor.
	 */
	public AsyncBugzillaRestClient(URI serverUri, AuthenticationHandler authenticationHandler) {
		this(serverUri, new AsynchronousHttpClientFactory().createClient(serverUri, authenticationHandler));
	}

	public AsyncBugzillaRestClient(URI serverUri, HttpClient httpClient) {
		this.serverUri = serverUri;
		this.baseUri = UriBuilder.fromUri(serverUri).path("/jsonrpc.cgi").build();
		this.httpClient = httpClient;
		this.lazyRetriever = new BugzillaLazyRetriever(this);
		metadataClient = new AsyncBugzillaMetadataRestClient(this);
		classificationClient = new AsyncBugzillaClassificationRestClient(this);
		productClient = new AsyncBugzillaProductRestClient(this);
		userClient = new AsyncBugzillaUserRestClient(this);
		bugClient = new AsyncBugzillaBugRestClient(this);
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

	/**
	 * Returns the lazyRetriever.
	 * @return the lazyRetriever
	 */
	public LazyRetriever getLazyRetriever() {
		return lazyRetriever;
	}

	/**
	 * Returns the serverUri.
	 * @return the serverUri
	 */
	public URI getServerUri() {
		return serverUri;
	}

	/**
	 * Returns the baseUri.
	 * @return the baseUri
	 */
	public URI getBaseUri() {
		return baseUri;
	}

	/**
	 * Returns the httpClient.
	 * @return the httpClient
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}

	
}
