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

	private static BugzillaMetadataRestClient myClient;
	
	@BeforeClass
	public static void setup() throws Exception {
		AbstractRestClientTest.setup();
		myClient = client.getMetadataClient();		
	}
	
	@Test
	public void testVersion() throws Exception {
		Promise<ServerInfo> promise = myClient.getServerInfo();
		assertNotNull("No promise", promise);
		ServerInfo serverInfo = promise.get();
		assertNotNull("No response", serverInfo);
		assertNotNull("No version", serverInfo.getVersion());
	}

}
