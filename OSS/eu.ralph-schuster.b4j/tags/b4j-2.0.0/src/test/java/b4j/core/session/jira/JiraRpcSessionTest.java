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
package b4j.core.session.jira;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;
import b4j.core.Attachment;
import b4j.core.Comment;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.session.JiraRpcSession;
import b4j.core.util.CommentTest;

/**
 * Jira Session test ({@link JiraRpcSession}).
 * @author ralph
 *
 */
public class JiraRpcSessionTest {

	private static Map<String, String> expectedCommentAttachments = new HashMap<String, String>();
	private static Logger log = LoggerFactory.getLogger(JiraRpcSessionTest.class);

	static {
		addCommentAttachment("CSV-28", "http://jira.ralph-schuster.eu/rest/api/2/attachment/10100");
	}

	private static void addCommentAttachment(String id, String attachmentId) {
		expectedCommentAttachments.put(id, attachmentId);
	}

	private static JiraRpcSession session;

	@BeforeClass
	public static void setup() throws Exception {
		URL url = FileFinder.find(JiraRpcSessionTest.class, "local-test-jira-config.xml");
		if (url == null) url = FileFinder.find(JiraRpcSessionTest.class, "test-jira-config.xml");
		assertNotNull("Cannot find test-config.xml", url);
		Configuration myConfig = new XMLConfiguration(url);
		session = new JiraRpcSession();
		session.setTimeout(120, TimeUnit.SECONDS);
		session.configure(myConfig);
		session.open();
	}


	@AfterClass
	public static void cleanup() throws Exception {
		session.close();
	}

	/**
	 * Test single issue.
	 */
	@Test
	public void testOpen() throws Exception {
		assertTrue("Session was not opened", session.isLoggedIn());
	}

	/**
	 * Test JQL query.
	 */
	@Test
	public void testJql() throws Exception {
		// Create search criteria
		DefaultSearchData searchData = new DefaultSearchData();
		searchData.add("jql", "project=BFJ and status = Closed and created < \"2014-01-21\"");
		Collection<String> expectedBugs = new ArrayList<String>();
		for (int i=1; i<33; i++) expectedBugs.add("BFJ-"+i);
		expectedBugs.remove("BFJ-25");
		expectedBugs.remove("BFJ-26");
		
		// Perform the search
		Iterable<Issue> i = session.searchBugs(searchData, null);
		assertNotNull("No iterable returned", i);
		CommentTest commentTest = new CommentTest();
		for (Issue issue : i) {
			String id = issue.getId();
			assertNotNull("No ID for issue record", id);
			assertTrue("Issue "+id+" is not expected", expectedBugs.contains(id));
			expectedBugs.remove(id);

			for (Comment c : issue.getComments()) {
				commentTest.test(c);
			}
		}
		assertTrue("Some issues were not found: "+expectedBugs.toArray().toString(), expectedBugs.isEmpty());
	}


	/**
	 * Test comments.
	 */
	@Test
	public void testAttachments() throws Exception {
		DefaultSearchData searchData = new DefaultSearchData();
		for (String id : expectedCommentAttachments.keySet()) {
			searchData.add("key", id);
		}

		// Perform the search
		Iterable<Issue> i = session.searchBugs(searchData, null);
		assertNotNull("No iterator returned", i);
		for (Issue issue : i) {
			String id = issue.getId();
			assertNotNull("No ID for issue record", id);
			String attachementId = expectedCommentAttachments.get(id);
			boolean found = false;
			for (Attachment attachment : issue.getAttachments()) {
				log.debug(attachment.getId());
				if (attachementId.equals(attachment.getId())) {
					found = true;
					break;
				}
			}
			assertTrue("Expected attachment not found", found);
		}

	}

	/**
	 * Test comments.
	 */
	@Test
	public void testLoadAttachment() throws Exception {
		Issue issue = session.getIssue("CSV-23");
		assertNotNull("Cannot load issue: CSV-23", issue);
		
		BufferedReader r = new BufferedReader(new InputStreamReader(session.getAttachment(issue.getAttachment("http://jira.ralph-schuster.eu/rest/api/2/attachment/10002"))));
		String s = null;
		s = r.readLine();
		s = r.readLine();
		r.close();
		assertEquals("Attachment cannot be read", s, " * This file is part of CSV package.");
	}
	
}
