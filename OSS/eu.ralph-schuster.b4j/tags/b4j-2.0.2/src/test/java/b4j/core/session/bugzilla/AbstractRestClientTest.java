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

import java.net.URI;
import java.net.URL;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;
import b4j.core.session.bugzilla.async.AsyncBugzillaRestClientFactory;
import b4j.util.HttpClients;
import b4j.util.HttpSessionParams;

import com.atlassian.httpclient.api.HttpClient;

/**
 * Abstract setup for tests.
 * @author ralph
 *
 */
public abstract class AbstractRestClientTest {

	protected static URL baseUrl;
	protected static URI serverUri;
	protected static HttpClient httpClient;
	protected static BugzillaClient client;
	protected static Configuration config;
	
	protected static void setup() throws Exception {
		setup(null);
	}

	protected static void setup(String bugzillaHome) throws Exception {
		URL url = FileFinder.find(AbstractRestClientTest.class, "local-test-config.xml");
		if (url == null) url = FileFinder.find(AbstractRestClientTest.class, "test-config.xml");
		assertNotNull("Cannot find test-config.xml", url);
		LoggerFactory.getLogger("b4j.core.session.bugzilla.BugzillaRestClient").debug("Using config: "+url.toString());
		XMLConfiguration config = new XMLConfiguration(url);
		if (bugzillaHome == null) bugzillaHome = config.getString("bugzilla-home");
		HttpSessionParams params = new HttpSessionParams();
		params.configure(config);
		
		baseUrl = new URL(bugzillaHome);
		
		BugzillaRestClientFactory factory = new AsyncBugzillaRestClientFactory();
		serverUri = baseUrl.toURI();
		
		httpClient = HttpClients.createAtlassianClient(serverUri, params);
		client = factory.create(serverUri, httpClient);
		
	}

}
