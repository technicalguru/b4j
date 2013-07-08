/**
 * 
 */
package b4j.core.session;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b4j.core.Attachment;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;

/**
 * Bugzilla Session test
 * @author ralph
 *
 */
public class XmlRpcJiraSessionTest {

	private static Map<String, Map<String,String>> expectedProperties = new HashMap<String, Map<String,String>>();
	private static Map<String, String> expectedCommentAttachments = new HashMap<String, String>();
	private static Logger log = LoggerFactory.getLogger(XmlRpcJiraSessionTest.class);

	static {
		addBug("BFJ-1", "Create abstract class for BugzillaReportGenerator");
		addBug("BFJ-2", "java.lang.IllegalArgumentException: Passed in key must select exactly one node: ProxyAuthorizationCallback(0)");
		addBug("BFJ-3", "Add abstract Email Report class");
		addBug("BFJ-4", "JIRA session");
		addBug("BFJ-5", "Writing # as first column does not escape it");
		addBug("BFJ-6", "Filter searches on HttpJiraSession does not return when no issue was found");
		addCommentAttachment("CSV-28", "http://jira.ralph-schuster.eu/rest/api/2/attachment/10100");
	}

	private static void addBug(String id, String shortDescription) {
		Map<String,String> props = new HashMap<String, String>();
		props.put("shortDescription", shortDescription);
		expectedProperties.put(id, props);
	}

	private static void addCommentAttachment(String id, String attachmentId) {
		expectedCommentAttachments.put(id, attachmentId);
	}

	private XmlRpcJiraSession session;

	@Before
	public void setup() throws Exception {
		URL url = getClass().getResource("/test-jira-config.xml");
		assertNotNull("Cannot find test-config.xml", url);
		Configuration myConfig = new XMLConfiguration(url);
		session = new XmlRpcJiraSession();
		session.configure(myConfig);
		session.open();
	}


	@After
	public void cleanup() throws Exception {
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
	 * Test single issue.
	 */
	@Test
	public void testGetIssue() throws Exception {
		// Search a simple bug
		Issue issue = session.getIssue("BFJ-1");
		assertNotNull(issue);
	}

	/**
	 * Test JQL query.
	 */
	@Test
	public void testJql() throws Exception {
		// Create search criteria
		DefaultSearchData searchData = new DefaultSearchData();
		searchData.add("jql", "project=BFJ");

		// Perform the search
		Iterator<Issue> i = session.searchBugs(searchData, null);
		assertNotNull("No iterator returned", i);
		while (i.hasNext()) {
			Issue issue = i.next();
			String id = issue.getId();
			assertNotNull("No ID for issue record", id);
			log.debug("Jira issue found: "+id);
			if (expectedProperties.containsKey(id)) {
				testIssue(id, issue);
			}
		}
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
		Iterator<Issue> i = session.searchBugs(searchData, null);
		assertNotNull("No iterator returned", i);
		while (i.hasNext()) {
			Issue issue = i.next();
			String id = issue.getId();
			assertNotNull("No ID for issue record", id);
			log.debug("Jira issue found: "+id);
			String attachementId = expectedCommentAttachments.get(id);
			Iterator<Attachment> i2 = issue.getAttachmentIterator();
			boolean found = false;
			while (i2.hasNext()) {
				Attachment attachment = i2.next();
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
	
	private void testIssue(String id, Issue issue) throws Exception {
		Map<String,String> props = expectedProperties.get(id);
		assertNotNull("No expected properties found", props);
		for (String key : props.keySet()) {
			assertEquals(key+" does ot match:", PropertyUtils.getProperty(issue, key), props.get(key));
		}
		testSpecials(issue);
	}

	/**
	 * Does special tests.
	 * @param issue
	 * @throws Exception
	 */
	private void testSpecials(Issue issue) throws Exception {
		// Special bug with timestamps
		if (issue.getId().equals("BFJ-1")) {
			assertEquals("Timestamp parsed invalid", 1371679896000L, issue.getDeltaTimestamp().getTime() );
			assertEquals("Comment timestamp parsed invalid", 1354571018000L, issue.getLongDescription("10043").getWhen().getTime());
		}
	}
}
