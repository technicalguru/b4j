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
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.Test;

import rs.baselib.io.FileFinder;
import b4j.core.Attachment;
import b4j.core.Comment;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.session.BugzillaHttpSession;

/**
 * Bugzilla Session test
 * @author ralph
 *
 */
@SuppressWarnings("deprecation")
public class BugzillaHttpSessionTest {

	private static Map<String, Map<String,String>> expectedProperties = new HashMap<String, Map<String,String>>();
	private static Map<String, Map<String,String>> expectedCommentAttachments = new HashMap<String, Map<String,String>>();
	
	static {
		addBug("3", "Create abstract class for BugzillaReportGenerator", "CLOSED", "FIXED", "P3", "enhancement");
		addBug("5", "java.lang.IllegalArgumentException: Passed in key must select exactly one node: ProxyAuthorizationCallback(0)", "CLOSED", "FIXED", "P3", "normal");
		addBug("6", "Add abstract Email Report class", "CLOSED", "FIXED", "P3", "enhancement");
		addBug("13", "JIRA session", "ASSIGNED", null, "P3", "normal");
		addBug("14", "Writing # as first column does not escape it", "CLOSED", "FIXED", "P3", "normal");
		addBug("18", "Filter searches on HttpJiraSession does not return when no issue was found", "CLOSED", "FIXED", "P3", "normal");
		addCommentAttachment("30", "49", "3");
	}
	
	private static void addBug(String id, String shortDescription, String status, String resolution, String priority, String severity) {
		Map<String,String> props = new HashMap<String, String>();
		props.put("summary", shortDescription);
		if (status != null) props.put("status.name", status);
		if (resolution != null) props.put("resolution.name", resolution);
		if (priority != null) props.put("priority.name", priority);
		if (severity != null) props.put("severity.name", severity);
		expectedProperties.put(id, props);
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
		BugzillaHttpSession session = new BugzillaHttpSession();
		session.configure(myConfig);

		session.open();
		assertTrue("Session was not opened", session.isLoggedIn());

		// Create search criteria
		DefaultSearchData searchData = new DefaultSearchData();
		searchData.add("classification", "Java Projects");

		// Perform the search
		Iterator<Issue> i = session.searchBugs(searchData, null);
		assertNotNull("No iterator returned", i);
		while (i.hasNext()) {
			Issue issue = i.next();
			String id = issue.getId();
			assertNotNull("No ID for issue record", id);
			if (expectedProperties.containsKey(id)) {
				testIssue(id, issue);
			}
			if (expectedCommentAttachments.containsKey(id)) {
				Map<String,String> commentMap = expectedCommentAttachments.get(id);
				for (Map.Entry<String, String> entry : commentMap.entrySet()) {
					Comment desc = issue.getComment(entry.getKey());
					assertNotNull("Expected comment not found", desc);
					boolean found = false;
					for (Attachment attachment : desc.getAttachments()) {
						if (entry.getValue().equals(""+attachment.getId())) {
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

	private void testIssue(String id, Issue issue) throws Exception {
		Map<String,String> props = expectedProperties.get(id);
		assertNotNull("No expected properties found", props);
		for (String key : props.keySet()) {
			assertEquals(key+" does not match:", props.get(key), PropertyUtils.getProperty(issue, key));
		}		
	}
	
	/**
	 * Does special tests.
	 * @param issue
	 * @throws Exception
	 */
	private void testSpecials(BugzillaHttpSession session, Issue issue) throws Exception {
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
