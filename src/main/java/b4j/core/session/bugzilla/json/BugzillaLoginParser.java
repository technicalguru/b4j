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

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.session.bugzilla.async.AsyncBugzillaUserRestClient.LoginToken;
import b4j.util.LazyRetriever;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses a login response from Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaLoginParser extends AbstractJsonParser implements JsonObjectParser<LoginToken> {

	/**
	 * Constructor.
	 */
	public BugzillaLoginParser() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public BugzillaLoginParser(LazyRetriever lazyRetriever) {
		super(lazyRetriever);
	}

	@Override
	public LoginToken parse(JSONObject json) throws JSONException {
		checkError(json); // Throws exception when error occurred
		JSONObject obj = getResult(json);
		return new LoginToken(obj.getLong("id"), obj.getString("token"));
	}

}
