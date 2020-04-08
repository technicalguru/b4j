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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.Attachment;
import b4j.core.Comment;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.session.BugzillaHttpSession;
import b4j.core.util.CommentTest;
import b4j.core.util.IssueTest;
import rs.baselib.io.FileFinder;
import rs.baselib.util.CommonUtils;

/**
 * Bugzilla Session test
 * @author ralph
 *
 */
public class BugzillaHttpSessionTest {

	private static BugzillaHttpSession session;
	
	@BeforeClass
	public static void setup() throws Exception {
		URL url = FileFinder.find(BugzillaHttpSessionTest.class, "local-test-config.xml");
		if (url == null) url = FileFinder.find(BugzillaHttpSessionTest.class, "test-config.xml");
		assertNotNull("Cannot find test-config.xml", url);
		Configuration myConfig = new XMLConfiguration(url);
		session = new BugzillaHttpSession();
		session.configure(myConfig);

		session.open();
		assertTrue("Session was not opened", session.isLoggedIn());
	}
	
	@AfterClass
	public static void cleanup() throws Exception {
		// Close the session again
		session.close();
	}
	
	
	/**
	 * Test search cycle with B4J Bugzilla.
	 */
	@Test
	public void testSession() throws Exception {

		// Create search criteria
		DefaultSearchData searchData = new DefaultSearchData();
		searchData.add("classification", "Java Projects");
		Collection<String> expectedBugs = new ArrayList<String>();
		for (int i=2; i<36; i++) expectedBugs.add(Integer.toString(i));

		// Perform the search
		Iterable<Issue> i = session.searchBugs(searchData, null);
		assertNotNull("No iterator returned", i);
		IssueTest issueTest = new IssueTest();
		CommentTest commentTest = new CommentTest();
		for (Issue issue : i) {
			String id = issue.getId();
			assertNotNull("No ID for issue record", id);
			assertTrue("Issue "+id+" is not expected", expectedBugs.contains(id));
			expectedBugs.remove(id);
			
			//issueTest.save(issue);
			issueTest.test(issue);
			for (Comment c : issue.getComments()) {
				//commentTest.save(c);
				commentTest.test(c);
			}
			testSpecials(session, issue);
		}
		assertTrue("Some issues were not found "+CommonUtils.join(", ", expectedBugs.toArray(new String[0])), expectedBugs.isEmpty());
		
	}

	/**
	 * Does special tests.
	 * @param issue
	 * @throws Exception
	 */
	private void testSpecials(BugzillaHttpSession session, Issue issue) throws Exception {		
		// Check attachment retrieval
		if (issue.getId().equals("30")) {
			Attachment attchmt = issue.getAttachment("3");
			assertNotNull("No attachment found", attchmt);
			BufferedReader r = new BufferedReader(new InputStreamReader(session.getAttachment(attchmt), StandardCharsets.UTF_8));
			String s = null;
			s = r.readLine();
			s = r.readLine();
			r.close();
			assertEquals("Attachment cannot be read", " * This file is part of CSV package.", s);
		}
		
	}
}
