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
