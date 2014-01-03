/**
 * 
 */
package b4j.core.session.bugzilla;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.Issue;
import b4j.core.Project;

import com.atlassian.util.concurrent.Promise;

/**
 * Tests the {@link b4j.core.session.bugzilla.async.AsyncBugzillaBugRestClient}.
 * @author ralph
 *
 */
public class AsyncBugzillaBugRestClientTest extends AbstractRestClientTest {

	private static BugzillaBugRestClient myClient;

	@BeforeClass
	public static void setup() throws Exception {
		AbstractRestClientTest.setup(); // Our own server has no classifications
		myClient = client.getBugClient();		
	}

	@Test
	public void testGetBugs() throws Exception {
		Iterator<Issue> i = myClient.getBugs(2,3).get().iterator();
		while (i.hasNext()) {
			Issue issue = i.next();
		}
	}


	@SuppressWarnings("unused")
	public void testGetProducts(Promise<Iterable<Project>> promise, int expected) throws Exception {
		assertNotNull("No promise", promise);
		
		int cnt = 0;
		for (Project p : promise.get()) {
			cnt++;
		}
		assertEquals("Not enough products", expected, cnt);
	}

}
