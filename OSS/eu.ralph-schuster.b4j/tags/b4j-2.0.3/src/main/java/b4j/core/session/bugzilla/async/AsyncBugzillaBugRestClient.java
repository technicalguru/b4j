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
package b4j.core.session.bugzilla.async;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import b4j.core.Attachment;
import b4j.core.Comment;
import b4j.core.Issue;
import b4j.core.session.bugzilla.BugzillaBugRestClient;
import b4j.core.session.bugzilla.json.BugzillaAttachmentParser;
import b4j.core.session.bugzilla.json.BugzillaBugParser;
import b4j.core.session.bugzilla.json.BugzillaCommentParser;

import com.atlassian.util.concurrent.Promise;

/**
 * The client responsible for getting metadata information.
 * @author ralph
 * @since 2.0
 *
 */
public class AsyncBugzillaBugRestClient extends AbstractAsyncRestClient implements BugzillaBugRestClient {

	private BugzillaBugParser bugParser;
	private BugzillaCommentParser commentParser;
	private BugzillaAttachmentParser attachmentParser;
	
	/**
	 * Constructor.
	 * @param mainClient main rest client reference
	 */
	public AsyncBugzillaBugRestClient(AsyncBugzillaRestClient mainClient) {
		super(mainClient, "Bug");
		bugParser = new BugzillaBugParser(mainClient);
		commentParser = new BugzillaCommentParser(mainClient);
		attachmentParser = new BugzillaAttachmentParser(mainClient);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Issue>> getBugs(long... ids) {
		List<Long> l = new ArrayList<Long>();
		for (int i=0; i<ids.length; i++) l.add(ids[i]);
		return getBugs(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Issue>> getBugs(Collection<Long> ids) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return postAndParse("get", params, bugParser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Issue>> findBugs(Map<String, Object> criteria) {
		return postAndParse("search", criteria, bugParser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Attachment>> getAttachments(String... issueIds) {
		Collection<String> coll = new ArrayList<String>();
		for (String i : issueIds) coll.add(i);
		return getAttachments(coll);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Attachment>> getAttachments(Collection<String> issueIds) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", issueIds);
		return postAndParse("attachments", params, attachmentParser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Comment>> getComments(String... issueIds) {
		Collection<String> coll = new ArrayList<String>();
		for (String i : issueIds) coll.add(i);
		return getComments(coll);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Comment>> getComments(Collection<String> issueIds) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", issueIds);
		return postAndParse("comments", params, commentParser);
	}

	protected Collection<Long> getIds(Collection<Issue> issues) {
		Collection<Long> rc = new ArrayList<Long>();
		for (Issue issue : issues) {
			rc.add(Long.parseLong(issue.getId()));
		}
		return rc;
	}

}
