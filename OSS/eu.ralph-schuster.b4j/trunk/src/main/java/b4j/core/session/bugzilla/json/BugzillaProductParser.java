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
import b4j.core.Project;
import b4j.core.Version;
import b4j.core.session.bugzilla.BugzillaComponent;
import b4j.core.session.bugzilla.BugzillaProject;
import b4j.util.LazyRetriever;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses the product for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaProductParser extends AbstractJsonParser implements JsonObjectParser<Iterable<Project>> {

//	private BugzillaMilestoneParser milestoneParser = new BugzillaMilestoneParser(); TODO
	private BugzillaVersionParser versionParser;
	private BugzillaComponentParser componentParser;
	
	/**
	 * Constructor.
	 */
	public BugzillaProductParser() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public BugzillaProductParser(LazyRetriever lazyRetriever) {
		super(lazyRetriever);
		versionParser = new BugzillaVersionParser(lazyRetriever);
		componentParser = new BugzillaComponentParser(lazyRetriever);
	}

	@Override
	public Iterable<Project> parse(JSONObject json) throws JSONException {
		List<Project> rc = new ArrayList<Project>();
		checkError(json); // Throws exception when error occurred
		JSONArray arr = getResult(json).getJSONArray("products");
		for (int i=0; i<arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			rc.add(parseSingle(obj));
		}
		return rc;
	}

	public Project parseSingle(JSONObject json) throws JSONException {
		LazyRetriever retriever = getLazyRetriever();
		BugzillaProject rc = new BugzillaProject(json.getString("name"));
//		for (String s : milestoneParser.parse(json)) { 
//			rc.addMilestone(s); 
//		}
		for (Component c : componentParser.parse(json)) {
			((BugzillaComponent)c).setProject(rc);
			if (retriever != null) retriever.registerComponent(c);
			rc.addComponents(c);
		}
		rc.setDescription(json.getString("description"));
		rc.setId(json.getString("id"));
		versionParser.setProject(rc);
		for (Version version : versionParser.parse(json)) { 
			if (retriever != null) retriever.registerVersion(version);
			rc.addVersions(version);
		}
		return rc;
	}
}
