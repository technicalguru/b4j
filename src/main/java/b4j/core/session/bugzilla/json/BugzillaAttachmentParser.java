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
package b4j.core.session.bugzilla.json;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

import b4j.core.Attachment;
import b4j.core.DefaultAttachment;
import b4j.core.session.bugzilla.async.AsyncBugzillaRestClient;
import b4j.util.BugzillaUtils;

/**
 * Parses the product for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaAttachmentParser extends AbstractJsonParser implements JsonObjectParser<Iterable<Attachment>> {

	/**
	 * Constructor.
	 */
	public BugzillaAttachmentParser(AsyncBugzillaRestClient mainClient) {
		super(mainClient.getLazyRetriever());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<Attachment> parse(JSONObject json) throws JSONException {
		List<Attachment> rc = new ArrayList<Attachment>();
		checkError(json); // Throws exception when error occurred
		JSONObject bugs = getResult(json).getJSONObject("bugs");

		Iterator<String> keys = bugs.keys();
		while (keys.hasNext()) {
			String issueId = keys.next();
			JSONArray attachments = bugs.getJSONArray(issueId);
			for (int i=0; i<attachments.length(); i++) {
				rc.add(parseSingleAttachment(attachments.getJSONObject(i)));
			}
		}
		return rc;
	}

	public Attachment parseSingleAttachment(JSONObject json) throws JSONException {
		DefaultAttachment rc = new DefaultAttachment(json.getString("bug_id"));
		rc.setId(json.getString("id"));
		String description = "(none)";
		if (json.has("description")) {
			description = json.getString("description");
		} else if (json.has("summary")) {
			description = json.getString("summary");
		}
		rc.setDescription(description);
		rc.setFilename(json.getString("file_name"));
		rc.setContent(json.getInt("size"), json.getString("data"));
		rc.setType(json.getString("content_type"));
		try {
			rc.setDate(BugzillaUtils.parseDate(json.getString("last_change_time")));
		} catch (ParseException e) {
			throw new JSONException(e);
		}
		return rc;
	}

}
