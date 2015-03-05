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
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import rs.baselib.io.FileFinder;
import b4j.core.Issue;
import b4j.core.session.BugzillaRpcSession;
import b4j.core.util.IssueTest;

/**
 * Test {@link b4j.core.session.BugzillaRpcSession BugzillaRpcSession} for known issues.
 * Due to nature of the RPC implementation, the test takes around 3.5 secs per issue, currently 
 * ~ 120 seconds in total.
 * @author ralph
 *
 */
@RunWith(Parameterized.class)
public class BugzillaRpcIssueTest {

	private static BugzillaRpcSession session;
	private static IssueTest issueTest = new IssueTest();

	@BeforeClass
	public static void setup() throws Exception {
		URL url = FileFinder.find(BugzillaRpcIssueTest.class, "local-test-config.xml");
		if (url == null) url = FileFinder.find(BugzillaRpcIssueTest.class, "test-config.xml");
		assertNotNull("Cannot find test-config.xml", url);
		Configuration myConfig = new XMLConfiguration(url);
		session = new BugzillaRpcSession();
		session.configure(myConfig);

		session.open();
		assertTrue("Session was not opened", session.isLoggedIn());
	}


	@AfterClass
	public static void cleanup() throws Exception {
		session.close();
	}

	@Parameters
	public static Collection<Object[]> data() throws Exception {
		Collection<Object[]> data = new ArrayList<Object[]>();
		for (String s : issueTest.getTestables(null)) {
			if (s.startsWith("bugzilla-")) data.add(new Object[] { s.substring(9) });
		}
		return data;
	}
	
	private String id;
	
	/**
	 * Constructs the test with given parameters.
	 */
	public BugzillaRpcIssueTest(String id) {
		this.id = id;
	}
	
	/**
	 * Test single issue.
	 */
	@Test
	public void test() throws Exception {
		// Search a simple bug
		Issue issue = session.getIssue(id);
		assertNotNull(issue);
		issueTest.test(issue);
	}

	
}
