/**
 * 
 */
package b4j.core.session.bugzilla;

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.ServerInfo;

import com.atlassian.util.concurrent.Promise;

/**
 * Tests the {@link b4j.core.session.bugzilla.async.AsyncBugzillaMetadataRestClient}.
 * @author ralph
 *
 */
public class AsyncBugzillaMetadataRestClientTest extends AbstractRestClientTest {

	private static BugzillaMetadataRestClient metaClient;
	
	@BeforeClass
	public static void setup() throws Exception {
		AbstractRestClientTest.setup();
		metaClient = client.getMetadataClient();		
	}
	
	@Test
	public void testVersion() throws Exception {
		Promise<ServerInfo> promise = metaClient.getServerInfo();
		assertNotNull("No promise", promise);
		ServerInfo serverInfo = promise.get();
		assertNotNull("No response", serverInfo);
		assertNotNull("No version", serverInfo.getVersion());
	}

}
