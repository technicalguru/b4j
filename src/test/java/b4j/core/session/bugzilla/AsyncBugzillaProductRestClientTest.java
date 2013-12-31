/**
 * 
 */
package b4j.core.session.bugzilla;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.Project;

import com.atlassian.util.concurrent.Promise;

/**
 * Tests the {@link b4j.core.session.bugzilla.async.AsyncBugzillaProductRestClient}.
 * @author ralph
 *
 */
public class AsyncBugzillaProductRestClientTest extends AbstractRestClientTest {

	private static BugzillaProductRestClient myClient;

	@BeforeClass
	public static void setup() throws Exception {
		AbstractRestClientTest.setup(); // Our own server has no classifications
		myClient = client.getProductClient();		
	}

	@Test
	public void testGetSelectableProducts() throws Exception {
		testGetProducts(myClient.getSelectableProducts(), 4);
	}

	@Test
	public void testGetEnterableProducts() throws Exception {
		testGetProducts(myClient.getEnterableProducts(), 4);
	}

	@Test
	public void testGetAcessibleProducts() throws Exception {
		testGetProducts(myClient.getAccessibleProducts(), 4);
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

	@Test
	public void testGetProducts() throws Exception {
		Promise<Iterable<Project>> promise = myClient.getProducts(4);
		assertNotNull("No promise", promise);
		Project p = promise.get().iterator().next();
		// name
		assertEquals("Not the right product", "B4J - Bugzilla for Java", p.getName());
		// 3 components
		assertEquals("Not enough components", 3, p.getComponents().size());
		// 4 versions
		assertEquals("Not enough components", 4, p.getVersions().size());
		//assertNotNull(p.getId());
		//assertNotNull(p.getName());
	}

}
