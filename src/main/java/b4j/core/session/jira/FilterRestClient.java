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
