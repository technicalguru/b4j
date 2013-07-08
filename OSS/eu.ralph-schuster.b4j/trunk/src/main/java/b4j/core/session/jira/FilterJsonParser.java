/**
 * 
 */
package b4j.core.session.jira;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.atlassian.jira.rest.client.internal.json.JsonParseUtil;

/**
 * Parses the json object as a filter.
 * @author ralph
 *
 */
public class FilterJsonParser implements JsonObjectParser<Filter> {

	@Override
	public Filter parse(JSONObject json) throws JSONException {
		BasicFilter rc = new BasicFilter();
		rc.setSelf(JsonParseUtil.parseURI(json.getString("self")));
		rc.setId(json.getString("id"));
		rc.setName(json.getString("name"));
		rc.setDescription(json.getString("description"));
		rc.setOwner(JsonParseUtil.parseBasicUser(json.getJSONObject("owner")));
		rc.setJql(json.getString("jql"));
		rc.setViewUrl(JsonParseUtil.parseURI(json.getString("viewUrl")));
		rc.setSearchUrl(JsonParseUtil.parseURI(json.getString("searchUrl")));
		rc.setFavourite(JsonParseUtil.getNestedBoolean(json, "favourite"));
		/* Missing
		sharePermissions
		sharedUsers
		subscriptions
		*/
		return rc;
	}
}
