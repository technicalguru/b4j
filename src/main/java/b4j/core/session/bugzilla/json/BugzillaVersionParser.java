/**
 * 
 */
package b4j.core.session.bugzilla.json;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.Version;
import b4j.core.session.bugzilla.BugzillaVersion;
import b4j.core.session.bugzilla.LazyRetriever;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses an ID list for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaVersionParser extends AbstractJsonParser implements JsonObjectParser<Iterable<Version>> {

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
		debug(json);
		List<Version> rc = new ArrayList<Version>();
		JSONArray arr = json.getJSONArray("versions");
		for (int i=0; i<arr.length(); i++) {
			JSONObject v = arr.getJSONObject(i);
			rc.add(new BugzillaVersion(v.getLong("id"), v.getString("name")));
		}
		return rc;
	}

}
