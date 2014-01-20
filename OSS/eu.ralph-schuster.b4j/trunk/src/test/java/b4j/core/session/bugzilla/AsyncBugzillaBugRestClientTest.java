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

import java.util.Iterator;

import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.Issue;
import b4j.core.Project;
import b4j.util.BugzillaUtils;

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
			BugzillaUtils.debug(issue);
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
