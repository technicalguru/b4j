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

import b4j.core.Component;
import b4j.core.session.bugzilla.BugzillaComponent;
import b4j.util.LazyRetriever;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses an ID list for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaComponentParser extends AbstractJsonParser implements JsonObjectParser<Iterable<Component>> {

	/**
	 * Constructor.
	 */
	public BugzillaComponentParser() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public BugzillaComponentParser(LazyRetriever lazyRetriever) {
		super(lazyRetriever);
	}

	@Override
	public Iterable<Component> parse(JSONObject json) throws JSONException {
		List<Component> rc = new ArrayList<Component>();
		JSONArray arr = json.getJSONArray("components");
		for (int i=0; i<arr.length(); i++) {
			JSONObject js = arr.getJSONObject(i);
			BugzillaComponent c = new BugzillaComponent(js.getString("name"));
			c.setDescription(js.getString("description"));
			c.setId(js.getString("id"));
			rc.add(c);
		}
		return rc;
	}

}
