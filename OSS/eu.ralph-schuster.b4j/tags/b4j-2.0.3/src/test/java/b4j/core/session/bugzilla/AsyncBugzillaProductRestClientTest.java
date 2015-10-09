/*
 * This file is part of Bugzilla for Java.
 *
 *  Bugzilla for Java is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  Bugzilla for Java is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with Bugzilla for Java.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
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
	public void testGetProductsById() throws Exception {
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

	@Test
	public void testGetProductsByName() throws Exception {
		Promise<Iterable<Project>> promise = myClient.getProductsByName("B4J - Bugzilla for Java");
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
