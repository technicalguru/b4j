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
