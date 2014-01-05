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
package b4j.core.session.bugzilla;

import java.util.Collection;
import java.util.Map;

import b4j.core.Attachment;
import b4j.core.Comment;
import b4j.core.Issue;

import com.atlassian.util.concurrent.Promise;

/**
 * Interface for bug REST clients.
 * @author ralph
 * @see <a href="http://www.bugzilla.org/docs/4.4/en/html/api/Bugzilla/WebService/Bug.html">Bugzilla::WebService::Bug</a>
 * @since 2.0
 *
 */
public interface BugzillaBugRestClient {

	/**
	 * Retrieves information about bugs.
	 *
	 * @param ids IDs of bugs
	 * @return information about bugs
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Issue>> getBugs(long... ids);

	/**
	 * Retrieves information about bugs.
	 *
	 * @param ids IDs of bugs
	 * @return information about bugs
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Issue>> getBugs(Collection<Long> ids);

	/**
	 * Retrieves information about bugs.
	 *
	 * @param criteria Map of field matching criteria according to 
	 * <a href="http://www.bugzilla.org/docs/4.4/en/html/api/Bugzilla/WebService/Bug.html#search">Bugzilla Search API</a>.
	 * @return information about bugs
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Issue>> findBugs(Map<String,Object> criteria);

	/**
	 * Retrieves information about attachments.
	 *
	 * @param ids IDs of attachments
	 * @return information about attachments
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Attachment>> getAttachments(long... ids);

	/**
	 * Retrieves information about attachments.
	 *
	 * @param ids IDs of attachments
	 * @return information about attachments
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Attachment>> getAttachments(Collection<Long> ids);

	/**
	 * Retrieves information about comments.
	 *
	 * @param ids IDs of comments
	 * @return information about comments
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Comment>> getComments(long... ids);

	/**
	 * Retrieves information about comments.
	 *
	 * @param ids IDs of comments
	 * @return information about comments
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Comment>> getComments(Collection<Long> ids);


}
