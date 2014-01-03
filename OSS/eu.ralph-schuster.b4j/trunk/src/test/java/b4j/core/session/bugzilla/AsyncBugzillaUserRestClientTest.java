/**
 * 
 */
package b4j.core.session.bugzilla;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.User;
import b4j.core.session.bugzilla.async.AsyncBugzillaUserRestClient;

import com.atlassian.util.concurrent.Promise;

/**
 * Tests the {@link b4j.core.session.bugzilla.async.AsyncBugzillaProductRestClient}.
 * @author ralph
 *
 */
public class AsyncBugzillaUserRestClientTest extends AbstractRestClientTest {

	private static AsyncBugzillaUserRestClient myClient;

	@BeforeClass
	public static void setup() throws Exception {
		AbstractRestClientTest.setup(); // Our own server has no classifications
		myClient = (AsyncBugzillaUserRestClient)client.getUserClient();		
	}

	@Test
	public void testLogin() throws Exception {
		User user = myClient.login("test", "testuser");
		assertEquals("Not the right user", "test", user.getName());
		assertEquals("Not the right user", "2", user.getId());
		testGetUsers(myClient.getUsers(1,2,3), 3);
		myClient.logout();
	}

	@Test
	public void testGetUsersByName() throws Exception {
		testGetUsers(myClient.getUsersByName("admin", "test"), 2);
	}

	@SuppressWarnings("unused")
	public void testGetUsers(Promise<Iterable<User>> promise, int expected) throws Exception {
		assertNotNull("No promise", promise);
		
		int cnt = 0;
		for (User u : promise.get()) {
			cnt++;
		}
		assertEquals("Not enough users", expected, cnt);
	}

	@Test
	public void testGetUser() throws Exception {
		Promise<Iterable<User>> promise = myClient.getUsersByName("admin");
		assertNotNull("No promise", promise);
		User p = promise.get().iterator().next();
		// name
		assertEquals("Not the right user", "admin", p.getName());
		assertEquals("Not the right user", "1", p.getId());
	}

}
