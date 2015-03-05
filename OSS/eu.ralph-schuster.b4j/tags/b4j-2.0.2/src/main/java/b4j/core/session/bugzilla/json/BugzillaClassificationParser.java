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

import b4j.core.Classification;
import b4j.core.DefaultClassification;
import b4j.util.LazyRetriever;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses the classification for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaClassificationParser extends AbstractJsonParser implements JsonObjectParser<Iterable<Classification>> {

	/**
	 * Constructor.
	 */
	public BugzillaClassificationParser() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public BugzillaClassificationParser(LazyRetriever lazyRetriever) {
		super(lazyRetriever);
	}

	@Override
	public Iterable<Classification> parse(JSONObject json) throws JSONException {
		List<Classification> rc = new ArrayList<Classification>();
		checkError(json); // Throws exception when error occurred
		JSONArray arr = getResult(json).getJSONArray("classifications");
		for (int i=0; i<arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			rc.add(parseSingle(obj));
		}
		return rc;
	}

	public Classification parseSingle(JSONObject json) throws JSONException {
		return new DefaultClassification(json.getString("id"), json.getString("name"), json.getString("description"));
	}
}
