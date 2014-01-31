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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.Attachment;
import b4j.core.Comment;
import b4j.core.Issue;

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
		client.getUserClient().login("test", "password");
	}

	@AfterClass
	public static void shutdown() throws Exception {
		client.getUserClient().logout();
	}
	
	/** Test the get method */
	@Test
	public void testGetBugs() throws Exception {
		List<String> expected = getStringList(new int[] { 2,3 });
		for (Issue issue :  myClient.getBugs(2,3).get()) {
			assertTrue("Issue "+issue.getId()+" was not expected to be delivered by get()", expected.remove(issue.getId()));
		}
		assertEquals("Not the correct issues returned by get()", 0, expected.size());
	}

	/** Test the search method */
	@Test
	public void testSearchBugs() throws Exception {
		List<String> expected = getStringList(new int[] { 2,4,7,8,9,10,11,12,15,16,17,19,20,21,22,23,24,25,26,27,28,29,30 });
		
		// Search closed, fixed bugs
		Map<String,Object> criteria = new HashMap<String, Object>();
		criteria.put("status", new String[] { "RESOLVED", "VERIFIED", "CLOSED", "UNCONFIRMED", "NEW", "ASSIGNED", "REOPENED" });
		criteria.put("product", "CSV Utility Package");
		for (Issue issue :  myClient.findBugs(criteria).get()) {
			assertTrue("Issue "+issue.getId()+" was not expected to be delivered by get()", expected.remove(issue.getId()));
		}
		assertEquals("Not the correct issues returned by get()", 0, expected.size());
	}

	private List<String> getStringList(int arr[]) {
		List<String> rc = new ArrayList<String>();
		for (int i : arr) rc.add(Integer.toString(i));
		return rc;
	}
	
	/** Test the get method */
	@Test
	public void testGetComments() throws Exception {
		List<Issue> issues = new ArrayList<Issue>();
		for (Issue issue :  myClient.getBugs(2,4,7,8,9,10,11,12,15,16,17,19,20,21,22,23,24,25,26,27,28,29,30).get()) {
			if (issue != null) issues.add(issue);
		}
		
		List<String> expected = getStringList(new int[] { 16,30,32,34,8,12,42,23,24,2,3,10,33,49,50,36,37,22,45,46,43,47,40,41,44,48,14,17,20,21,29,31,35,9,13,5,11,38,39,15,27,28 });
		for (Issue issue: issues) {
			for (Comment comment : issue.getComments()) {
				assertTrue("Comment "+comment.getId()+" was not expected to be delivered by get()", expected.remove(comment.getId()));
			}
		};
		assertEquals("Not the correct comments returned by LazyRetriever.getComments()", 0, expected.size());
	}

	/** Test the get method */
	@Test
	public void testGetAttachments() throws Exception {
		List<Issue> issues = new ArrayList<Issue>();
		for (Issue issue :  myClient.getBugs(2,4,7,8,9,10,11,12,15,16,17,19,20,21,22,23,24,25,26,27,28,29,30).get()) {
			if (issue != null) issues.add(issue);
		}
		
		List<String> expected = getStringList(new int[] { 1, 2, 3 });
		for (Issue issue: issues) {
			for (Attachment a : issue.getAttachments()) {
				assertTrue("Attachment "+a.getId()+" was not expected to be delivered by get()", expected.remove(a.getId()));
			}
		};
		assertEquals("Not the correct attachments returned by LazyRetriever.getAttachments()", 0, expected.size());
	}
}
