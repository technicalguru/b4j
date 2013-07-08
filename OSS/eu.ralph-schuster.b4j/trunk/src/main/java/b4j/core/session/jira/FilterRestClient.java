/**
 * 
 */
package b4j.core.session.jira;

import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.util.concurrent.Promise;

/**
 * Accesses JIRA filters.
 * @author ralph
 *
 */
public interface FilterRestClient {

	/**
	 * Search the issues from the given filter.
	 * @param filterId the filter id
	 * @return the issues from that filter
	 */
	Promise<SearchResult> search(String filterId);
	
	/**
	 * Search the issues from the given filter.
	 * @param filterId the filter id
	 * @return the issues from that filter
	 */
	Promise<SearchResult> search(String filterId, int maxResults, int startAt);
}
