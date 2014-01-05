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
package b4j.core.session.bugzilla.json;

import java.net.URI;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.DefaultServerInfo;
import b4j.core.ServerInfo;
import b4j.util.LazyRetriever;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.atlassian.jira.rest.client.internal.json.JsonParseUtil;

/**
 * Parses the server info for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaServerInfoParser extends AbstractJsonParser implements JsonObjectParser<ServerInfo> {

	/**
	 * Constructor.
	 */
	public BugzillaServerInfoParser() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public BugzillaServerInfoParser(LazyRetriever lazyRetriever) {
		super(lazyRetriever);
	}

	@Override
	public ServerInfo parse(JSONObject json) throws JSONException {
		checkError(json); // Throws exception when error occurred
		URI baseUri = JsonParseUtil.parseURI(json.getString("id"));
		String version = getResult(json).getString("version");
		return new DefaultServerInfo(baseUri, version);
	}

}
