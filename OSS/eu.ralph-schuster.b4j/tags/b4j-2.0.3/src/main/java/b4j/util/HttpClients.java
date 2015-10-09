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

package b4j.util;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;

import b4j.core.session.jira.AsyncHttpClientFactory;

import com.atlassian.jira.rest.client.AuthenticationHandler;
import com.atlassian.jira.rest.client.auth.AnonymousAuthenticationHandler;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;

/**
 * Creates and configures Atlassian's and Apache's HttpClient.
 * @author ralph
 *
 */
public class HttpClients {

	private static AsyncHttpClientFactory httpClientFactory = new AsyncHttpClientFactory();
	
	/**
	 * Creates a {@link com.atlassian.httpclient.api.HttpClient} from the session params.
	 * @param uri the base URI to be used
	 * @param params the parameters to be used
	 * @return the HttpClient created
	 */
	public static com.atlassian.httpclient.api.HttpClient createAtlassianClient(URI uri, HttpSessionParams params) {
		return createAtlassianClient(uri, params, null);
	}

	/**
	 * Creates a {@link com.atlassian.httpclient.api.HttpClient} from the session params.
	 * @param uri the base URI to be used
	 * @param params the parameters to be used
	 * @param log the log to be used for debug logging
	 * @return the HttpClient created
	 */
	public static com.atlassian.httpclient.api.HttpClient createAtlassianClient(URI uri, HttpSessionParams params, Logger log) {
		return createAtlassianClient(uri, createAuthenticationHandler(params, log));
	}

	/**
	 * Creates a {@link com.atlassian.httpclient.api.HttpClient} from the session params.
	 * @param uri the base URI to be used
	 * @param authenticationHandler the authenticastion handler to be used
	 * @return the HttpClient created
	 */
	public static com.atlassian.httpclient.api.HttpClient createAtlassianClient(URI uri, AuthenticationHandler authenticationHandler) {
		if (authenticationHandler == null) authenticationHandler = new AnonymousAuthenticationHandler();
		AsyncHttpClientFactory factory = getHttpClientFactory();
		return factory.createClient(uri, authenticationHandler);
	}

	public static synchronized AsyncHttpClientFactory getHttpClientFactory() {
		return httpClientFactory;
	}
	
	/**
	 * Creates a {@link org.apache.http.client.HttpClient} from the session params.
	 * @param params the parameters to be used
	 * @return the HttpClient created
	 */
	public static org.apache.http.client.HttpClient createApacheClient(HttpSessionParams params) {
		return createApacheClient(params, null);
	}

	/**
	 * Creates a {@link org.apache.http.client.HttpClient} from the session params.
	 * @param params the parameters to be used
	 * @param log the log to be used for debug logging
	 * @return the HttpClient created
	 */
	public static org.apache.http.client.HttpClient createApacheClient(HttpSessionParams params, Logger log) {
		if (params == null) params = new HttpSessionParams();

		DefaultHttpClient httpClient = new DefaultHttpClient();
		if (params.hasProxy()) {
			HttpHost proxy = new HttpHost(params.getProxyHost(), params.getProxyPort());
			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
			if ((log != null) && log.isDebugEnabled()) {
				log.debug("Using HTTP Proxy: "+params.getProxyHost()+":"+params.getProxyPort());
			}
		}
		if (params.hasProxyAuthentication()) {
			httpClient.getCredentialsProvider().setCredentials(new AuthScope(params.getProxyHost(), params.getProxyPort()), new UsernamePasswordCredentials(params.getProxyUser(), params.getProxyPassword()));
		}
		if (params.hasAuthorization() && params.isBasicAuthentication()) {
			httpClient.getCredentialsProvider().setCredentials(new AuthScope(null, -1),  new UsernamePasswordCredentials(params.getLogin(), params.getPassword()));
			if ((log != null) && log.isDebugEnabled()) {
				log.debug("Authenticate with: "+params.getLogin());
			}
		}
		return httpClient;
	}

	/**
	 * Create an authentication handler to be used for the HTTP client configuration.
	 * @param params the parameters to be used
	 * @return a new authentication handler
	 */
	public static AuthenticationHandler createAuthenticationHandler(HttpSessionParams params) {
		return createAuthenticationHandler(params, null);
	}

	/**
	 * Create an authentication handler to be used for the HTTP client configuration.
	 * @param params the parameters to be used
	 * @param log the log to be used for debug logging
	 * @return a new authentication handler
	 */
	public static AuthenticationHandler createAuthenticationHandler(HttpSessionParams params, Logger log) {
		if (params == null) params = new HttpSessionParams();

		AuthenticationHandler proxyAuth  = null;
		AuthenticationHandler targetAuth = null;
		if (params.hasProxy()) {
			System.setProperty("http.proxyHost", params.getProxyHost());
			System.setProperty("http.proxyPort", Integer.toString(params.getProxyPort()));
			if ((log != null) && log.isDebugEnabled()) {
				log.debug("Using HTTP Proxy: "+params.getProxyHost()+":"+params.getProxyPort());
			}
			if (params.hasProxyAuthentication()) {
				proxyAuth = new ProxyAuthenticationHandler(params.getProxyUser(), params.getProxyPassword());
			}
		}
		if (params.hasAuthorization() && params.isBasicAuthentication()) {
			targetAuth = new BasicHttpAuthenticationHandler(params.getLogin(), params.getPassword());
			if ((log != null) && log.isDebugEnabled()) {
				log.debug("Authenticate with: "+params.getLogin());
			}
		}
		return new CombinedAuthenticationHandler(proxyAuth, targetAuth);
	}


}
