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
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Test;

import rs.baselib.io.FileFinder;
import b4j.core.Comment;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.session.BugzillaRpcSession;
import b4j.core.util.IssueTest;

/**
 * Bugzilla Session test
 * @author ralph
 *
 */
public class BugzillaRpcSessionTest {

	private static Map<String, Map<String,String>> expectedCommentAttachments = new HashMap<String, Map<String,String>>();
	
	static {
		addCommentAttachment("30", "49", "3");
	}
	
	private static void addCommentAttachment(String id, String commentId, String attachmentId) {
		Map<String,String> commentMap = expectedCommentAttachments.get(id);
		if (commentMap == null) {
			commentMap = new HashMap<String, String>();
			expectedCommentAttachments.put(id, commentMap);
		}
		commentMap.put(commentId, attachmentId);
	}
	
	/**
	 * Test search cycle with B4J Bugzilla.
	 */
	@Test
	public void testSession() throws Exception {
		URL url = FileFinder.find(getClass(), "local-test-config.xml");
		if (url == null) url = FileFinder.find(getClass(), "test-config.xml");
		assertNotNull("Cannot find test-config.xml", url);
		Configuration myConfig = new XMLConfiguration(url);
		BugzillaRpcSession session = new BugzillaRpcSession();
		session.configure(myConfig);

		session.open();
		assertTrue("Session was not opened", session.isLoggedIn());

		// Create search criteria
		DefaultSearchData searchData = new DefaultSearchData();
		searchData.add("product", "B4J - Bugzilla for Java");
		searchData.add("product", "CSV Utility Package");
		searchData.add("product", "IceScrum Stylesheets");
		searchData.add("product", "Templating");

		// Perform the search
		Iterable<Issue> i = session.searchBugs(searchData, null);
		assertNotNull("No iterable returned", i);
		IssueTest issueTest = new IssueTest();
		for (Issue issue : i) {
			String id = issue.getId();
			assertNotNull("No ID for issue record", id);
			//issueTest.save("src/test/resources", issue);
			issueTest.test(issue);
			if (expectedCommentAttachments.containsKey(id)) {
				Map<String,String> commentMap = expectedCommentAttachments.get(id);
				for (Map.Entry<String, String> entry : commentMap.entrySet()) {
					Comment desc = issue.getComment(entry.getKey());
					assertNotNull("Expected comment not found", desc);
					boolean found = false;
					for (String attachment : desc.getAttachments()) {
						if (entry.getValue().equals(""+attachment)) {
							found = true;
							break;
						}
					}
					assertTrue("Expected attachment not found", found);
				}
				expectedCommentAttachments.remove(id);
			}
			testSpecials(session, issue);
		}
		assertTrue("Comments not found", expectedCommentAttachments.isEmpty());
		
		// Close the session again
		session.close();
	}

	/**
	 * Does special tests.
	 * @param issue
	 * @throws Exception
	 */
	private void testSpecials(BugzillaRpcSession session, Issue issue) throws Exception {
		// Special bug with timestamps
		if (issue.getId().equals("3")) {
			assertEquals("Timestamp parsed invalid", 1345485240000L, issue.getUpdateTimestamp().getTime());
			assertEquals("Comment timestamp parsed invalid", 1244567618000L, issue.getComment("4").getWhen().getTime());
		}
		
		// Check attachment retrieval
		if (issue.getId().equals("30")) {
			BufferedReader r = new BufferedReader(new InputStreamReader(session.getAttachment(issue.getAttachment("3"))));
			String s = null;
			s = r.readLine();
			s = r.readLine();
			r.close();
			assertEquals("Attachment cannot be read", " * This file is part of CSV package.", s);
		}
		
	}
}
