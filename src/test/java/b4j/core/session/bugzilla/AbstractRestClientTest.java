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

import rs.baselib.io.FileFinder;
import b4j.core.session.bugzilla.async.AsyncBugzillaRestClientFactory;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.AuthenticationHandler;
import com.atlassian.jira.rest.client.auth.AnonymousAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory;

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
		URL url = FileFinder.find("test-config.xml");
		assertNotNull("Cannot find test-config.xml", url);
		Configuration config = new XMLConfiguration(url);
		if (bugzillaHome == null) bugzillaHome = config.getString("bugzilla-home");
		baseUrl = new URL(bugzillaHome);
		
		BugzillaRestClientFactory factory = new AsyncBugzillaRestClientFactory();
		serverUri = baseUrl.toURI();
		
		AuthenticationHandler authenticationHandler = new AnonymousAuthenticationHandler();
		//new BasicHttpAuthenticationHandler(login, password);
		httpClient = new AsynchronousHttpClientFactory().createClient(serverUri, authenticationHandler);
		client = factory.create(serverUri, httpClient);
	}

}
