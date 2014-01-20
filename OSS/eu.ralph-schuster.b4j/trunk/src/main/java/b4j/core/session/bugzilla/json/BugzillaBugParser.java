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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.DefaultIssue;
import b4j.core.Issue;
import b4j.core.IssueType;
import b4j.core.session.bugzilla.BugzillaIssueType;
import b4j.util.BugzillaUtils;
import b4j.util.LazyRetriever;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses the product for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaBugParser extends AbstractJsonParser implements JsonObjectParser<Iterable<Issue>> {

	private static final IssueType BUG_TYPE = new BugzillaIssueType("bug");
	
	/**
	 * Constructor.
	 */
	public BugzillaBugParser() {
		this(null);
	}
	
	/**
	 * Constructor.
	 */
	public BugzillaBugParser(LazyRetriever lazyRetriever) {
		super(lazyRetriever);
	}

	@Override
	public Iterable<Issue> parse(JSONObject json) throws JSONException {
		List<Issue> rc = new ArrayList<Issue>();
		checkError(json); // Throws exception when error occurred
		JSONArray arr = getResult(json).getJSONArray("bugs");
		for (int i=0; i<arr.length(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			try {
				rc.add(parseSingleBug(obj));
			} catch (ParseException e) {
				throw new JSONException(e);
			}
		}
		return rc;
	}

	public Issue parseSingleBug(JSONObject json) throws JSONException, ParseException {
		DefaultIssue rc = new DefaultIssue();
		rc.set(DefaultIssue.LAZY_RETRIEVER, getLazyRetriever());
		//debug(json);
		rc.setType(BUG_TYPE);
		rc.set("priority_name", json.getString("priority"));
		rc.set("reporter_name", json.getString("creator"));
		rc.setUpdateTimestamp(BugzillaUtils.parseDate(json.getString("last_change_time")));
		rc.set(Issue.CCLIST_ACCESSIBLE, json.getBoolean("is_cc_accessible"));
		//rc.setCcs(null); string array
		rc.setUri(null);
		rc.set("assignee_name", json.getString("assigned_to"));
		rc.setId(json.getString("id"));
		rc.setCreationTimestamp(BugzillaUtils.parseDate(json.getString("creation_time")));
		rc.set(Issue.WHITEBOARD, json.getString("whiteboard"));
		rc.set(Issue.QA_CONTACT, json.getString("qa_contact"));
		// depends_on int array
		// blocks int array
		// dupe_of int
		rc.set("resolution_name", json.getString("resolution"));
		rc.set("classification_name", json.getString("classification"));
		rc.set(Issue.ALIAS, json.getString("alias"));
		rc.set(Issue.OP_SYS, json.getString("op_sys"));
		rc.set("status_name", json.getString("status"));
		rc.setSummary(json.getString("summary"));
		rc.set(Issue.REP_PLATFORM, json.getString("platform"));
		rc.set("severity_name", json.getString("severity"));
		rc.set("fixVersion_name", json.getString("version"));
		rc.set("component_name", json.getString("component"));
		rc.set(Issue.REPORTER_ACCESSIBLE, json.getBoolean("is_creator_accessible"));
		rc.set("project_name", json.getString("product"));
		rc.set(Issue.MILESTONE, json.getString("target_milestone"));
		rc.set(Issue.CONFIRMED, json.getBoolean("is_confirmed"));
		return rc;
	}
}
