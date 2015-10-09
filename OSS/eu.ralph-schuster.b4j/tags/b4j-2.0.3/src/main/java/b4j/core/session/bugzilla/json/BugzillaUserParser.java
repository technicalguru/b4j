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

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.User;
import b4j.core.session.bugzilla.BugzillaUser;
import b4j.util.LazyRetriever;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses the login call response.
 * @author ralph
 *
 */
public class BugzillaUserParser extends AbstractJsonParser implements JsonObjectParser<Iterable<User>> {

	/**
	 * Constructor.
	 */
	public BugzillaUserParser() {
		this(null);
	}
	
	/**
	 * Constructor.
	 */
	public BugzillaUserParser(LazyRetriever lazyRetriever) {
		super(lazyRetriever);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<User> parse(JSONObject json) throws JSONException {
		List<User> rc = new ArrayList<User>();
		checkError(json); // Throws exception when error occurred
		JSONArray users = getResult(json).getJSONArray("users");
		for (int i=0; i<users.length(); i++) {
			JSONObject obj = users.getJSONObject(i);
			BugzillaUser u = new BugzillaUser();
			u.setId(obj.getString("id"));
			u.setName(obj.getString("name"));
			u.setRealName(obj.getString("real_name"));
			rc.add(u);
		}
		
		return rc;
	}

	
}
