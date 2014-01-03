/**
 * 
 */
package b4j.core.session.bugzilla.async;

import java.net.URI;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.session.bugzilla.json.JSONUtils;

import com.atlassian.httpclient.api.HttpClient;
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

	private final URI baseUri;
	private String webService;

	/**
	 * Constructor.
	 * @param client
	 */
	public AbstractAsyncRestClient(URI baseUri, String webService, HttpClient client) {
		super(client);
		this.baseUri = baseUri;
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
			if ((params != null) && (params.length() > 0)) pArray.put(params);
			entity.put("params", pArray);
			return postAndParse(UriBuilder.fromUri(getBaseUri()).build(), entity, parser);
		} catch (JSONException e) {
			throw new RestClientException("Cannot post", e);
		}
	}

	/**
	 * Returns the baseUri.
	 * @return the baseUri
	 */
	public URI getBaseUri() {
		return baseUri;
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
	 * Returns the HTTP client object.
	 * @return the client object
	 */
	public HttpClient getHttpClient() {
		return client();
	}
}
