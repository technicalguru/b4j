/**
 * 
 */
package b4j.core.session.bugzilla.json;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.Component;
import b4j.core.Project;
import b4j.core.session.bugzilla.BugzillaComponent;
import b4j.core.session.bugzilla.BugzillaProject;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses the product for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaProductParser extends AbstractJsonParser implements JsonObjectParser<Iterable<Project>> {

//	private BugzillaMilestoneParser milestoneParser = new BugzillaMilestoneParser(); TODO
	private BugzillaVersionParser versionParser = new BugzillaVersionParser();
	private BugzillaComponentParser componentParser = new BugzillaComponentParser();
	
	/**
	 * Constructor.
	 */
	public BugzillaProductParser() {
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
		BugzillaProject rc = new BugzillaProject(json.getString("name"));
//		for (String s : milestoneParser.parse(json)) { 
//			rc.addMilestone(s); 
//		}
		for (Component c : componentParser.parse(json)) {
			((BugzillaComponent)c).setProject(rc);
			rc.addComponents(c);
		}
		rc.setDescription(json.getString("description"));
		rc.setId(json.getString("id"));
		for (String s : versionParser.parse(json)) { 
			rc.addVersions(s); 
		}
		return rc;
	}
}
