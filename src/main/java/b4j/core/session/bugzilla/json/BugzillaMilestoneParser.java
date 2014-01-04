/**
 * 
 */
package b4j.core.session.bugzilla.json;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.util.LazyRetriever;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses an ID list for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaMilestoneParser extends AbstractJsonParser implements JsonObjectParser<Iterable<String>> {

	/**
	 * Constructor.
	 */
	public BugzillaMilestoneParser() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public BugzillaMilestoneParser(LazyRetriever lazyRetriever) {
		super(lazyRetriever);
	}

	@Override
	public Iterable<String> parse(JSONObject json) throws JSONException {
		List<String> rc = new ArrayList<String>();
		JSONArray arr = json.getJSONArray("milestones");
		for (int i=0; i<arr.length(); i++) {
			JSONObject milestone = arr.getJSONObject(i);
			rc.add(milestone.getString("name"));
		}
		return rc;
	}

}
