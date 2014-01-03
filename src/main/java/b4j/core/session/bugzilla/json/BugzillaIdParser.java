/**
 * 
 */
package b4j.core.session.bugzilla.json;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.core.session.bugzilla.LazyRetriever;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses an ID for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaIdParser extends AbstractJsonParser implements JsonObjectParser<Long> {

	/**
	 * Constructor.
	 */
	public BugzillaIdParser() {
		this(null);
	}

	/**
	 * Constructor.
	 */
	public BugzillaIdParser(LazyRetriever lazyRetriever) {
		super(lazyRetriever);
	}

	@Override
	public Long parse(JSONObject json) throws JSONException {
		checkError(json); // Throws exception when error occurred
		return getResult(json).getLong("id");
	}

}
