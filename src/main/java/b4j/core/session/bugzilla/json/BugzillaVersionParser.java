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

import b4j.core.Project;
import b4j.core.Version;
import b4j.core.session.bugzilla.BugzillaVersion;
import b4j.util.LazyRetriever;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses an ID list for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaVersionParser extends AbstractJsonParser implements JsonObjectParser<Iterable<Version>> {

	private Project project;
	
	/**
	 * Constructor.
	 */
	public BugzillaVersionParser() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public BugzillaVersionParser(LazyRetriever lazyRetriever) {
		super(lazyRetriever);
	}

	@Override
	public Iterable<Version> parse(JSONObject json) throws JSONException {
		List<Version> rc = new ArrayList<Version>();
		JSONArray arr = json.getJSONArray("versions");
		for (int i=0; i<arr.length(); i++) {
			JSONObject v = arr.getJSONObject(i);
			rc.add(new BugzillaVersion(v.getLong("id"), project, v.getString("name")));
		}
		return rc;
	}

	/**
	 * Sets the project to be assigned to new versions.
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	
}
