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

import b4j.core.Comment;
import b4j.core.DefaultComment;
import b4j.core.session.bugzilla.async.AsyncBugzillaRestClient;
import b4j.util.BugzillaUtils;

import com.atlassian.jira.rest.client.internal.json.JsonObjectParser;

/**
 * Parses the product for Bugzilla REST API.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaCommentParser extends AbstractJsonParser implements JsonObjectParser<Iterable<Comment>> {

	/**
	 * Constructor.
	 */
	public BugzillaCommentParser(AsyncBugzillaRestClient mainClient) {
		super(mainClient.getLazyRetriever());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterable<Comment> parse(JSONObject json) throws JSONException {
		List<Comment> rc = new ArrayList<Comment>();
		checkError(json); // Throws exception when error occurred
		JSONObject bugs = getResult(json).getJSONObject("bugs");
		Iterator<String> keys = bugs.keys();
		while (keys.hasNext()) {
			String issueId = keys.next();
			JSONArray comments = bugs.getJSONObject(issueId).getJSONArray("comments");
			for (int i=0; i<comments.length(); i++) {
				JSONObject c = comments.getJSONObject(i);
				rc.add(parseSingleComment(c));
			}
		}
		return rc;
	}

	public Comment parseSingleComment(JSONObject json) throws JSONException {
		DefaultComment rc = new DefaultComment(json.getString("bug_id"));
		rc.setId(json.getString("id"));
		rc.setAuthor(getLazyRetriever().getUser(json.getString("creator")));
		rc.setUpdateAuthor(getLazyRetriever().getUser(json.getString("author")));
		try {
			rc.setUpdateTimestamp(BugzillaUtils.parseDate(json.getString("time")));
			rc.setCreationTimestamp(BugzillaUtils.parseDate(json.getString("creation_time")));
		} catch (ParseException e) {
			throw new JSONException(e);
		}
		rc.setTheText(json.getString("text"));
		String s = json.getString("attachment_id");
		if ((s != null) && !s.equals("null")) rc.setAttachments(s);
		return rc;
	}

}
