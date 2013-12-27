/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.IssueType;
import b4j.core.Priority;
import b4j.core.Resolution;
import b4j.core.ServerInfo;
import b4j.core.Severity;
import b4j.core.Status;

import com.atlassian.util.concurrent.Promise;

/**
 * Interface for metadata rest clients.
 * @author ralph
 * @since 2.0
 *
 */
public interface BugzillaMetadataRestClient {

	/**
	 * Retrieves from the server complete list of available issue type
	 *
	 * @return complete information about issue type resource
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	Promise<Iterable<IssueType>> getIssueTypes();

	/**
	 * Retrieves from the server complete list of available status
	 *
	 * @return complete information about status resource
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 */
	Promise<Status> getStatus();
	
	/**
	 * Retrieves from the server complete list of available priorities
	 *
	 * @return complete information about the selected priority
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	Promise<Iterable<Priority>> getPriorities();

	/**
	 * Retrieves from the server complete list of available severities
	 *
	 * @return complete information about the selected severity
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	Promise<Iterable<Severity>> getSeverities();

	/**
	 * Retrieves from the server complete information about selected resolution
	 *
	 * @return complete information about the selected resolution
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	Promise<Iterable<Resolution>> getResolutions();

	/**
	 * Retrieves information about this JIRA instance
	 *
	 * @return information about this JIRA instance
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	Promise<ServerInfo> getServerInfo();

}
