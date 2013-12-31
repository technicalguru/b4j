/**
 * 
 */
package b4j.core.session.bugzilla.async;

import java.io.StringWriter;
import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONWriter;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.RestClientException;
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient;

/**
 * Abstract implementation for Bugzilla REST clients.
 * @author ralph
 * @since 2.0
 *
 */
public abstract class AbstractAsyncRestClient extends AbstractAsynchronousRestClient {

	private final URI baseUri;

	/**
	 * Constructor.
	 * @param client
	 */
	public AbstractAsyncRestClient(URI baseUri, HttpClient client) {
		super(client);
		this.baseUri = baseUri;
	}

	/**
	 * Builds the URI from the base URI and the params
	 * @param ARGS URI params as simple array of { key, value, key, value }...
	 * @return the URI
	 */
	protected UriBuilder build(String ARGS[]) {
		return build(ARGS, null);
	}

	/**
	 * Builds the URI from the base URI and the params
	 * @param ARGS URI params as simple array of { key, value, key, value }...
	 * @param params method parameters to be passed
	 * @return the URI
	 */
	protected UriBuilder build(String ARGS[], String params) {
		UriBuilder builder = UriBuilder.fromUri(baseUri);
		for (int i=0; i<ARGS.length; i+= 2) {
			builder.queryParam(ARGS[i], ARGS[i+1]);
		}
		if (params != null) {
			builder.queryParam("params", params);
		}
		return builder;
	}

	/**
	 * Returns the baseUri.
	 * @return the baseUri
	 */
	public URI getBaseUri() {
		return baseUri;
	}

	protected String createParameterList(String key, Object values[]) {
		try {
			StringWriter sw = new StringWriter();
			JSONWriter w = new JSONWriter(sw);
			w.object().key(key).array();
			for (Object v : values) {
				if (v instanceof Long) {
					w.value(((Long)v).longValue());
				} else if (v instanceof Number) {
					w.value(((Number)v).doubleValue());
				} else if (v instanceof Boolean) {
					w.value(((Boolean)v).booleanValue());
				} else {
					w.value(v);
				}
			}
			w.endArray().endObject();
			return sw.toString();
		} catch (Exception e) {
			throw new RestClientException("Cannot build JSON parameters", e);
		}
	}
	
	protected String joinParameterLists(String ...params) {
		try {
			StringBuilder rc = new StringBuilder();
			rc.append('[');
			for (String v : params) {
				if (rc.length() > 1) rc.append(",");
				rc.append(v);
			}
			rc.append(']');
			return rc.toString();
		} catch (Exception e) {
			throw new RestClientException("Cannot build JSON parameters", e);
		}
	}
}
