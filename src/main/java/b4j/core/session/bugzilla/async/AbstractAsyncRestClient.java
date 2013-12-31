/**
 * 
 */
package b4j.core.session.bugzilla.async;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.atlassian.httpclient.api.HttpClient;
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
	protected URI buildUri(String ARGS[]) {
		UriBuilder builder = UriBuilder.fromUri(baseUri);
		for (int i=0; i<ARGS.length; i+= 2) {
			builder.queryParam(ARGS[i], ARGS[i+1]);
		}
		return builder.build();
	}

	/**
	 * Returns the baseUri.
	 * @return the baseUri
	 */
	public URI getBaseUri() {
		return baseUri;
	}
	
	
}
