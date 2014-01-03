/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.ServerInfo;

import com.atlassian.util.concurrent.Promise;

/**
 * Interface for metadata rest clients.
 * @see <a href="http://www.bugzilla.org/docs/4.4/en/html/api/Bugzilla/WebService/Bugzilla.html">Bugzilla::WebService::Bugzilla</a>
 * @author ralph
 * @since 2.0
 *
 */
public interface BugzillaMetadataRestClient {

	/**
	 * Retrieves information about this Bugzilla instance
	 *
	 * @return information about this Bugzilla instance
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	Promise<ServerInfo> getServerInfo();

}
