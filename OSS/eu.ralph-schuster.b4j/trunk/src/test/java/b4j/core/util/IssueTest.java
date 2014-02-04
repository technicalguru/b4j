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

import java.util.Collection;
import java.util.Properties;

import b4j.core.Issue;
import b4j.core.IssueLink;

/**
 * Tests the correctness of specific issues.
 * @author ralph
 *
 */
public class IssueTest extends AbstractObjectTest<Issue> {

	/**
	 * Constructor.
	 */
	public IssueTest() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Properties createComparable(Issue object) {
		Properties rc = new Properties();
		rc.setProperty("id", toString(object.getId()));
		rc.setProperty("type", toString(object.getType(), "name"));
		rc.setProperty("summary", toString(object.getSummary()));
		rc.setProperty("description", toString(object.getDescription()));
		rc.setProperty("classification", toString(object.getClassification(), "name"));
		rc.setProperty("project", toString(object.getProject(), "name"));
		rc.setProperty("components", join(object.getComponents(), "name"));
		rc.setProperty("status", toString(object.getStatus(), "name"));
		rc.setProperty("resolution", toString(object.getResolution(), "name"));
		rc.setProperty("priority", toString(object.getPriority(), "name"));
		rc.setProperty("severity", toString(object.getSeverity(), "name"));
		rc.setProperty("reporter", toString(object.getReporter(), "name"));
		rc.setProperty("creationTimestamp", toString(object.getCreationTimestamp(), "time"));
		rc.setProperty("assignee", toString(object.getAssignee(), "name"));
		rc.setProperty("updateTimestamp", toString(object.getUpdateTimestamp(), "time"));
		rc.setProperty("affectedVersions", join(object.getAffectedVersions(), "name"));
		rc.setProperty("plannedVersions", join(object.getPlannedVersions(), "name"));
		rc.setProperty("fixVersions", join(object.getFixVersions(), "name"));
		rc.setProperty("comments", join(object.getComments(), "id"));
		rc.setProperty("attachments", join(object.getAttachments(), "id"));
		rc.setProperty("parentId", toString(object.getParentId()));
		rc.setProperty("children", join(object.getChildren(), "id"));
		rc.setProperty("links", joinLinks(object.getLinks()));
		
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getId(Issue object) {
		if (object.getUri().contains("bugzilla")) return "bugzilla-"+object.getId();
		return "jira-"+object.getId();
	}

	/**
	 * Creates the string for issue links.
	 * @param links collection of links
	 * @return the string representation
	 */
	protected static String joinLinks(Collection<IssueLink> links) {
		if ((links == null) || (links.size() == 0)) return "null";
		StringBuilder rc = new StringBuilder();
		for (IssueLink link : links) {
			if (rc.length() > 0) rc.append(',');
			rc.append(link.getLinkTypeName()+":"+link.isInward()+":"+link.getIssueId());
		}
		return rc.toString();
	}
	
}
