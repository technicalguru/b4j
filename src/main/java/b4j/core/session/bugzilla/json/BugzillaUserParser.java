/**
 * 
 */
package b4j.core.session.bugzilla.json;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.User;
import b4j.core.session.bugzilla.BugzillaUser;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses the login call response.
 * @author ralph
 *
 */
public class BugzillaUserParser extends AbstractJsonParser implements JsonObjectParser<Iterable<User>> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<User> parse(JSONObject json) throws JSONException {
		List<User> rc = new ArrayList<User>();
		checkError(json); // Throws exception when error occurred
		JSONArray users = getResult(json).getJSONArray("users");
		for (int i=0; i<users.length(); i++) {
			JSONObject obj = users.getJSONObject(i);
			BugzillaUser u = new BugzillaUser();
			u.setId(obj.getString("id"));
			u.setName(obj.getString("name"));
			rc.add(u);
		}
		
		return rc;
	}

	
}
