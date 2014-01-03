/**
 * 
 */
package b4j.core.session.bugzilla.async;

import java.net.URI;

import org.codehaus.jettison.json.JSONObject;

import b4j.core.ServerInfo;
import b4j.core.session.bugzilla.BugzillaMetadataRestClient;
import b4j.core.session.bugzilla.LazyRetriever;
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

	private BugzillaServerInfoParser serverInfoParser;

	/**
	 * Constructor.
	 * @param client
	 */
	public AsyncBugzillaMetadataRestClient(URI baseUri, HttpClient client, LazyRetriever lazyRetriever) {
		super(baseUri, "Bugzilla", client, lazyRetriever);
		serverInfoParser = new BugzillaServerInfoParser(lazyRetriever);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<ServerInfo> getServerInfo() {
		return postAndParse("version", (JSONObject)null, serverInfoParser);
	}

	
}
