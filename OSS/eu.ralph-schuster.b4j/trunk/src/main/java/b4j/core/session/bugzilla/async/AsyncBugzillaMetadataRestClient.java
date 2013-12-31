/**
 * 
 */
package b4j.core.session.bugzilla.async;

import java.net.URI;

import b4j.core.ServerInfo;
import b4j.core.session.bugzilla.BugzillaMetadataRestClient;
import b4j.core.session.bugzilla.json.BugzillaServerInfoParser;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.util.concurrent.Promise;

/**
 * The client responsible for getting metadata information.
 * @author ralph
 * @since 2.0
 *
 */
public class AsyncBugzillaMetadataRestClient extends AbstractAsyncRestClient implements BugzillaMetadataRestClient {

	private static String CGI_PARAMS[] = {
		"method", "Bugzilla.version"
	};
	
	private BugzillaServerInfoParser serverInfoParser = new BugzillaServerInfoParser();

	/**
	 * Constructor.
	 * @param client
	 */
	public AsyncBugzillaMetadataRestClient(URI baseUri, HttpClient client) {
		super(baseUri, client);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<ServerInfo> getServerInfo() {
		URI serverInfoUri = buildUri(CGI_PARAMS);
		return getAndParse(serverInfoUri, serverInfoParser);
	}

	
}
