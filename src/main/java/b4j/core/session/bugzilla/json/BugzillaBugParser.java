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
import java.util.Collection;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.DefaultIssue;
import b4j.core.DefaultLink;
import b4j.core.Issue;
import b4j.core.IssueLink.Type;
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
		LazyRetriever retriever = getLazyRetriever();
		rc.set(DefaultIssue.LAZY_RETRIEVER, retriever);
		debug(json);
		rc.setType(BUG_TYPE);
		rc.set("priority_name", json.getString("priority")); if (retriever != null) retriever.registerPriority(json.getString("priority"));
		rc.set("reporter_name", json.getString("creator")); if (retriever != null) retriever.registerUser(json.getString("creator"));
		rc.setUpdateTimestamp(BugzillaUtils.parseDate(json.getString("last_change_time")));
		rc.set(Issue.CCLIST_ACCESSIBLE, json.getBoolean("is_cc_accessible"));
		rc.set(Issue.CC, getStringCollection(json.getJSONArray("cc")));
		rc.setUri(null);
		rc.set("assignee_name", json.getString("assigned_to"));  if (retriever != null) retriever.registerUser(json.getString("assigned_to"));
		rc.setId(json.getString("id"));
		rc.setCreationTimestamp(BugzillaUtils.parseDate(json.getString("creation_time")));
		rc.set(Issue.WHITEBOARD, json.getString("whiteboard"));
		rc.set(Issue.QA_CONTACT, json.getString("qa_contact"));
		// depends_on int array
		for (String otherId : getStringCollection(json.getJSONArray("depends_on"))) {
			rc.addLinks(new DefaultLink(Type.DEPENDS_ON, "Depends on", true, "Depends on other issue", otherId));
		}
		// blocks int array
		for (String otherId : getStringCollection(json.getJSONArray("blocks"))) {
			rc.addLinks(new DefaultLink(Type.UNSPECIFIED, "Blocks", true, "Blocks the other issue", otherId));
		}
		// dupe_of int
		String dupe = json.getString("dupe_of");
		if ((dupe != null) && !dupe.equals("null")) {
			rc.addLinks(new DefaultLink(Type.DUPLICATE, "Duplicate", false, "Duplicate of other issue", dupe));
		}
		rc.set("resolution_name", json.getString("resolution"));  if (retriever != null) retriever.registerResolution(json.getString("resolution"));
		rc.set("classification_name", json.getString("classification"));  if (retriever != null) retriever.registerClassification(json.getString("classification"));
		rc.set(Issue.ALIAS, json.getString("alias"));
		rc.set(Issue.OP_SYS, json.getString("op_sys"));
		rc.set("status_name", json.getString("status"));  if (retriever != null) retriever.registerStatus(json.getString("status"));
		rc.setSummary(json.getString("summary"));
		rc.set(Issue.REP_PLATFORM, json.getString("platform"));
		rc.set("severity_name", json.getString("severity")); if (retriever != null) retriever.registerSeverity(json.getString("severity"));
		rc.set("fixVersion_name", json.getString("version"));  if (retriever != null) retriever.registerVersion(json.getString("product"), json.getString("version"));
		rc.set("component_name", json.getString("component")); if (retriever != null) retriever.registerVersion(json.getString("product"), json.getString("component"));
		rc.set(Issue.REPORTER_ACCESSIBLE, json.getBoolean("is_creator_accessible"));
		rc.set("project_name", json.getString("product")); if (retriever != null) retriever.registerProject(json.getString("product"));
		rc.set(Issue.MILESTONE, json.getString("target_milestone"));
		rc.set(Issue.CONFIRMED, json.getBoolean("is_confirmed"));
		return rc;
	}
	
	protected Collection<Long> getIntCollection(JSONArray arr) throws JSONException {
		Collection<Long> rc = new ArrayList<Long>();
		for (int i=0; i<arr.length(); i++) {
			rc.add(arr.getLong(i));
		}
		return rc;
	}
	
	protected Collection<String> getStringCollection(JSONArray arr) throws JSONException {
		Collection<String> rc = new ArrayList<String>();
		for (int i=0; i<arr.length(); i++) {
			rc.add(arr.getString(i));
		}
		return rc;
	}
}
