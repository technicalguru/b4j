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

import org.junit.BeforeClass;
import org.junit.Test;

import b4j.core.ServerInfo;

import com.atlassian.util.concurrent.Promise;

/**
 * Tests the {@link b4j.core.session.bugzilla.async.AsyncBugzillaMetadataRestClient}.
 * @author ralph
 *
 */
public class AsyncBugzillaMetadataRestClientTest extends AbstractRestClientTest {

	private static BugzillaMetadataRestClient myClient;
	
	@BeforeClass
	public static void setup() throws Exception {
		AbstractRestClientTest.setup();
		myClient = client.getMetadataClient();		
	}
	
	@Test
	public void testVersion() throws Exception {
		Promise<ServerInfo> promise = myClient.getServerInfo();
		assertNotNull("No promise", promise);
		ServerInfo serverInfo = promise.get();
		assertNotNull("No response", serverInfo);
		assertNotNull("No version", serverInfo.getVersion());
	}

}
