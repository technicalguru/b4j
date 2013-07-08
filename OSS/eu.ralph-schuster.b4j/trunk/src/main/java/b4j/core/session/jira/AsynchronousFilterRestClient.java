/**
 * 
 */
package b4j.core.session.jira;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.SearchRestClient;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient;
import com.atlassian.util.concurrent.Promise;

/**
 * Manages access to filters.
 * @author ralph
 *
 */
public class AsynchronousFilterRestClient extends AbstractAsynchronousRestClient implements FilterRestClient  {

	private final SearchRestClient searchRestClient;
	private static final String FILTER_URI_PREFIX = "filter";
	private final URI filterUri;
	private final FilterJsonParser parser;
	/**
	 * Constructor.
	 */
	public AsynchronousFilterRestClient(final URI baseUri, final HttpClient client, final SearchRestClient searchRestClient) {
		super(client);
		this.searchRestClient = searchRestClient;
		this.filterUri = UriBuilder.fromUri(baseUri).path(FILTER_URI_PREFIX).build();
		this.parser = new FilterJsonParser();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<SearchResult> search(String filterId) {
		return searchRestClient.searchJql(getJql(filterId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<SearchResult> search(String filterId, int maxResults, int startAt) {
		return searchRestClient.searchJql(getJql(filterId), maxResults, startAt);
	}

	protected String getJql(String filterId) {
		Filter f = getFilter(filterId);
		return f.getJql();
	}
	
	protected Filter getFilter(String filterId) {
		try {
			final URI uri = UriBuilder.fromUri(filterUri).path(filterId).build();
			return getAndParse(uri, parser).get();
		} catch (Exception e) {
			throw new RuntimeException("Cannot load filter: "+filterId, e);
		}
	}
}
