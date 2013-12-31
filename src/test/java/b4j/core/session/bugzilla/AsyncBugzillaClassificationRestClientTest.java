/**
 * 
 */
package b4j.core.session.bugzilla;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.Classification;

import com.atlassian.util.concurrent.Promise;

/**
 * Tests the {@link b4j.core.session.bugzilla.async.AsyncBugzillaMetadataRestClient}.
 * @author ralph
 *
 */
public class AsyncBugzillaClassificationRestClientTest extends AbstractRestClientTest {

	private static BugzillaClassificationRestClient myClient;
	
	@BeforeClass
	public static void setup() throws Exception {
		AbstractRestClientTest.setup(); //"https://bugzilla.mozilla.org"); // Our own server has no classifications
		myClient = client.getClassificationClient();		
	}
	
	@Test
	public void testGetClassification() throws Exception {
		Promise<Iterable<Classification>> promise = myClient.getClassifications(2);
		assertNotNull("No promise", promise);
		int cnt = 0;
		for (Classification c : promise.get()) {
			assertNotNull(c.getId());
			assertNotNull(c.getName());
			cnt++;
		}
		assertEquals("Not enough classifications", 1, cnt);
	}

}
