/**
 * 
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
import b4j.core.session.bugzilla.LazyRetriever;
import b4j.util.BugzillaUtils;

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
		debug(json);
		rc.setType(BUG_TYPE);
		//rc.setPriority(priorities.get(json.getString("priority")));
		//rc.setReporter(json.getString("creator"));
		rc.setUpdateTimestamp(BugzillaUtils.parseDate(json.getString("last_change_time")));
		rc.set(Issue.CCLIST_ACCESSIBLE, json.getBoolean("is_cc_accessible"));
		//rc.setCcs(null); string array
		rc.setUri(null);
		//rc.setAssignee(json.getString("assigned_to"));
		rc.setId(json.getString("id"));
		rc.setCreationTimestamp(BugzillaUtils.parseDate(json.getString("creation_time")));
		rc.set(Issue.WHITEBOARD, json.getString("whiteboard"));
		rc.set(Issue.QA_CONTACT, json.getString("qa_contact"));
		// depends_on int array
		// blocks int array
		// dupe_of int
		//rc.setResolution(resolutions.get(json.getString("resolution")));
		//rc.setClassification(classifications.get(json.getString("classification")));
		//rc.setAlias(json.getString("alias"));
		rc.set(Issue.OP_SYS, json.getString("op_sys"));
		//rc.setStatus(status.get(json.getString("status")));
		rc.setSummary(json.getString("summary"));
		rc.set(Issue.REP_PLATFORM, json.getString("platform"));
		//rc.setSeverity(severities.get(json.getString("severity")));
		//rc.addFixVersions(json.getString("version"));
		// component string
		rc.set(Issue.REPORTER_ACCESSIBLE, json.getBoolean("is_creator_accessible"));
		//rc.setProject(projects.get(json.getString("product")));
		//rc.addPlannedVersions(json.getString("target_milestone"));
		rc.set(Issue.CONFIRMED, json.getBoolean("is_confirmed"));
		return rc;
	}
}
