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

	@Test
	public void testGetClassificationByName() throws Exception {
		Promise<Iterable<Classification>> promise = myClient.getClassificationsByName("Java Projects");
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
