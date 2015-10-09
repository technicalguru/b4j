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
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.session.bugzilla.async.AsyncBugzillaUserRestClient.LoginToken;
import b4j.core.session.bugzilla.json.JSONUtils;
import b4j.util.LazyRetriever;

import com.atlassian.httpclient.api.Response;
import com.atlassian.httpclient.api.ResponsePromise;
import com.atlassian.jira.rest.client.RestClientException;
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient;
import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.atlassian.util.concurrent.Promise;

/**
 * Abstract implementation for Bugzilla REST clients.
 * @author ralph
 * @since 2.0
 *
 */
public abstract class AbstractAsyncRestClient extends AbstractAsynchronousRestClient {

	private AsyncBugzillaRestClient mainClient;
	private String webService;
	
	/**
	 * Constructor.
	 * @param mainClient reference to main client
	 * @param webService the web service to be addressed
	 */
	public AbstractAsyncRestClient(AsyncBugzillaRestClient mainClient, String webService) {
		super(mainClient.getHttpClient());
		this.mainClient = mainClient;
		this.webService = webService;
	}

	/**
	 * Performs a POST call wit givebn parameters.
	 * @param method method to be called
	 * @param params parameters
	 * @param parser the response parser 
	 * @return the response promise
	 */
	protected <T> Promise<T> postAndParse(String method, Map<String,Object> params, JsonObjectParser<T> parser) {
		try {
			JSONObject obj = JSONUtils.convert(params);
			return postAndParse(method, obj, parser);
		} catch (JSONException e) {
			throw new RestClientException("Cannot post", e);
		}
	}

	/**
	 * Performs a POST call wit givebn parameters.
	 * @param method method to be called
	 * @param params parameters
	 * @param parser the response parser 
	 * @return the response promise
	 */
	protected <T> Promise<T> postAndParse(String method, JSONObject params, JsonObjectParser<T> parser) {
		try {
			JSONObject entity = new JSONObject();
			entity.put("method", webService+"."+method);
			entity.put("id", 1);
			JSONArray pArray = new JSONArray();
			String loginToken = getLoginToken();
			if (loginToken != null) {
				if (params == null) params = new JSONObject();
				params.put("token", loginToken);
				pArray.put(params);
			} else {
				if ((params != null) && (params.length() > 0)) pArray.put(params);
			}
			entity.put("params", pArray);
			URI uri = UriBuilder.fromUri(getBaseUri()).build();
			return postAndParse(uri, entity, parser);
		} catch (JSONException e) {
			throw new RestClientException("Cannot post", e);
		}
	}

	/**
	 * Retrieves the current login token.
	 * @return the token or {@code null} if not present
	 * @since 2.0.3
	 */
	protected String getLoginToken() {
		LoginToken token = getMainClient().getLoginToken();
		if (token != null) return token.getToken();
		return null;
	}
	/**
	 * Returns the baseUri.
	 * @return the baseUri
	 */
	public URI getBaseUri() {
		return getMainClient().getBaseUri();
	}

	/**
	 * Returns the mainClient.
	 * @return the mainClient
	 */
	public AsyncBugzillaRestClient getMainClient() {
		return mainClient;
	}

	/**
	 * Returns the response object from the POST request
	 * @param uri URI to be called
	 * @param entity parameters to be sent
	 * @return the Response object
	 * @throws Exception when a problem occurred
	 */
	protected Response getPostResponse(URI uri, JSONObject entity) throws Exception {
		ResponsePromise responsePromise = client().newRequest(uri)
				.setEntity(entity.toString())
				.setContentType("application/json")
				.post();
		call(responsePromise);
		Response response = responsePromise.get();
		return response;
	}

	/**
	 * Issues a simple GET request.
	 * @param uri URI to be called
	 * @throws Exception when a problem occurred
	 */
	protected Promise<Void> get(URI uri) {
		ResponsePromise responsePromise = client().newRequest(uri).get();
		return call(responsePromise);
	}

	/**
	 * Returns the lazyRetriever.
	 * @return the lazyRetriever
	 */
	public LazyRetriever getLazyRetriever() {
		return getMainClient().getLazyRetriever();
	}

}
