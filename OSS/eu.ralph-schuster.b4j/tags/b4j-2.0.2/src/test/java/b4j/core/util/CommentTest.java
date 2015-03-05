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

package b4j.core.util;

import java.util.Properties;

import b4j.core.Comment;

/**
 * Tests the correctness of specific issues.
 * @author ralph
 *
 */
public class CommentTest extends AbstractObjectTest<Comment> {

	/**
	 * Constructor.
	 */
	public CommentTest() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Properties createComparable(Comment object) {
		Properties rc = new Properties();
		rc.setProperty("id", toString(object.getId()));
		rc.setProperty("text", toString(object.getTheText()));
		rc.setProperty("author", toString(object.getAuthor(), "name"));
		rc.setProperty("creationTimestamp", toString(object.getCreationTimestamp(), "time"));
		rc.setProperty("updateAuthor", toString(object.getUpdateAuthor(), "name"));
		rc.setProperty("updateTimestamp", toString(object.getUpdateTimestamp(), "time"));
		rc.setProperty("attachments", join(object.getAttachments(), null));
		rc.setProperty("issueId", toString(object.getIssueId()));
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getId(Comment object) {
		if ((object.getIssueId() != null) && object.getIssueId().matches("\\d+")) return "bugzilla-"+object.getId();
		return "jira-"+object.getId();
	}

}
