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
public class BugzillaIdListParser extends AbstractJsonParser implements JsonObjectParser<Iterable<Long>> {

	/**
	 * Constructor.
	 */
	public BugzillaIdListParser() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public BugzillaIdListParser(LazyRetriever lazyRetriever) {
		super(lazyRetriever);
	}

	@Override
	public Iterable<Long> parse(JSONObject json) throws JSONException {
		List<Long> rc = new ArrayList<Long>();
		checkError(json); // Throws exception when error occurred
		JSONArray arr = getResult(json).getJSONArray("ids");
		for (int i=0; i<arr.length(); i++) {
			rc.add(arr.getLong(i));
		}
		return rc;
	}

}
