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
package b4j.core.session.bugzilla.async;

import org.codehaus.jettison.json.JSONObject;

import b4j.core.ServerInfo;
import b4j.core.session.bugzilla.BugzillaMetadataRestClient;
import b4j.core.session.bugzilla.json.BugzillaServerInfoParser;

import com.atlassian.util.concurrent.Promise;

/**
 * The client responsible for getting metadata information.
 * @author ralph
 * @since 2.0
 *
 */
public class AsyncBugzillaMetadataRestClient extends AbstractAsyncRestClient implements BugzillaMetadataRestClient {

	private BugzillaServerInfoParser serverInfoParser;

	/**
	 * Constructor.
	 * @param mainClient reference to main REST client
	 */
	public AsyncBugzillaMetadataRestClient(AsyncBugzillaRestClient mainClient) {
		super(mainClient, "Bugzilla");
		serverInfoParser = new BugzillaServerInfoParser(getLazyRetriever());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<ServerInfo> getServerInfo() {
		return postAndParse("version", (JSONObject)null, serverInfoParser);
	}

	
}
