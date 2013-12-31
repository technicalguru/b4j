/**
 * 
 */
package b4j.core.session.bugzilla.json;

import java.net.URI;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.DefaultServerInfo;
import b4j.core.ServerInfo;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;
import com.atlassian.jira.rest.client.internal.json.JsonParseUtil;

/**
 * Parses the server info for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaServerInfoParser extends AbstractJsonParser implements JsonObjectParser<ServerInfo> {

	/**
	 * Constructor.
	 */
	public BugzillaServerInfoParser() {
	}

	@Override
	public ServerInfo parse(JSONObject json) throws JSONException {
		checkError(json); // Throws exception when error occurred
		URI baseUri = JsonParseUtil.parseURI(json.getString("id"));
		String version = getResult(json).getString("version");
		return new DefaultServerInfo(baseUri, version);
	}

}
