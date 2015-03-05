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

package b4j.core.session;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import b4j.util.HttpClients;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.httpclient.api.factory.HttpClientOptions;
import com.atlassian.jira.rest.client.AuthenticationHandler;


/**
 * A base class for authorized sessions using {@link HttpClient}.
 * @author ralph
 * @since 2.0
 *
 */
public abstract class AbstractAtlassianHttpClientSession extends AbstractHttpSession {

	private static HttpClient httpClient;
	private AuthenticationHandler authenticationHandler;

	/**
	 * Constructor.
	 */
	public AbstractAtlassianHttpClientSession() {
	}

	/**
	 * Returns a read-to-use asynchronous HTTP client based on Atlassian's {@link HttpClient}.
	 * <p>The method will create a new {@link HttpClient} based on the given {@link URI} when none was created yet.
	 * Otherwise the existing client will be returned.</p>
	 * @param uri URI base URI to be used
	 * @return the client already configured with authentication.
	 */
	protected HttpClient getHttpClient(URI uri) {
		if (httpClient == null) {
			AuthenticationHandler handler = getAuthenticationHandler();
			if (handler == null) {
				httpClient = HttpClients.createAtlassianClient(uri, getHttpSessionParams(), getLog());
			} else {
				httpClient = HttpClients.createAtlassianClient(uri, handler);
			}
		}
		return httpClient;
	}

	/**
	 * Returns the client options.
	 * @return options options to be configured
	 */
	public HttpClientOptions getHttpClientOptions() {
		return HttpClients.getHttpClientFactory().getHttpClientOptions();
	}
	
	/**
	 * Sets the timeout value for connections.
	 * This method must be called before calling {@link #open()}.
	 * @param timeout timeout value
	 * @param timeUnit timeout unit
	 */
	public void setTimeout(int timeout, TimeUnit timeUnit) {
		HttpClientOptions options = getHttpClientOptions();
		options.setConnectionTimeout(timeout, timeUnit);
		options.setRequestTimeout(timeout, timeUnit);
		options.setSocketTimeout(timeout, timeUnit);
	}
	
	/**
	 * Returns a read-to-use asynchronous HTTP client based on Atlassian's {@link HttpClient}.
	 * <p>The method will return <code>null</code> when no {@link HttpClient} was created yet.</p>
	 * @return the client already configured with authentication.
	 * @see #getHttpClient(URI)
	 */
	protected HttpClient getHttpClient() {
		return httpClient;
	}
	
	/**
	 * Returns the authentication handler to be used for the HTTP client.
	 * @return the authentication handler
	 */
	protected AuthenticationHandler getAuthenticationHandler() {
		if (authenticationHandler == null) {
			authenticationHandler = HttpClients.createAuthenticationHandler(getHttpSessionParams(), getLog());
		}
		return authenticationHandler;
	}

	/**
	 * Sets a custom {@link HttpClient} to be used.
	 * @param httpClient the http client to set
	 */
	public static void setHttpClient(HttpClient httpClient) {
		AbstractAtlassianHttpClientSession.httpClient = httpClient;
	}

	/**
	 * Sets a custom {@link AuthenticationHandler} to be used.
	 * @param authenticationHandler the authentication handler to set
	 */
	public void setAuthenticationHandler(AuthenticationHandler authenticationHandler) {
		this.authenticationHandler = authenticationHandler;
	}
	
	
}
