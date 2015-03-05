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
package b4j.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b4j.core.Attachment;
import b4j.core.Comment;
import b4j.core.Component;
import b4j.core.DefaultIssue;
import b4j.core.Issue;
import b4j.core.IssueLink;
import b4j.core.Version;

/**
 * Provides some useful methods for all classes.
 * @author Ralph Schuster
 *
 */
public class BugzillaUtils {

	private static Logger log = LoggerFactory.getLogger(BugzillaUtils.class);

	/**
	 * Transforms an {@link Iterable} into a {@link Collection}.
	 * @param iterable iterable to transform
	 * @return the collection
	 */
	public static <T> Collection<T> transform(Iterable<T> iterable) {
		Collection<T> rc = new ArrayList<T>();
		for (T t : iterable) {
			rc.add(t);
		}
		return rc;
	}

	/**
	 * Debugs an issue in log file.
	 * @param issue issue to be debugged
	 */
	public static void debug(Issue issue) {
		log.debug("Issue:Id="+issue.getId());
		log.debug("   ServerVersion="+issue.getServerVersion());
		log.debug("   ServerUri="+issue.getServerUri());
		log.debug("   IssueUri="+issue.getUri());
		log.debug("   ParentId="+issue.getParentId());
		log.debug("   CreationTimestamp="+issue.getCreationTimestamp());
		log.debug("   Summary="+issue.getSummary());
		debug("   Description=", issue.getDescription());
		log.debug("   UpdateTimestamp="+issue.getUpdateTimestamp());
		log.debug("   Type="+issue.getType());
		log.debug("   TypeName="+issue.getType().getName());
		log.debug("   Classification="+issue.getClassification());
		log.debug("   Project="+issue.getProject());
		for (Component c : issue.getComponents()) {
			log.debug("   Component="+c.getName());
		}
		for (Version s : issue.getFixVersions()) {
			log.debug("   FixVersion="+s);
		}
		for (Version s : issue.getPlannedVersions()) {
			log.debug("   PlannedVersion="+s);
		}
		for (Version s : issue.getAffectedVersions()) {
			log.debug("   AffectedVersion="+s);
		}
		log.debug("   Status="+issue.getStatus());
		log.debug("   Resolution="+issue.getResolution());
		log.debug("   Priority="+issue.getPriority());
		log.debug("   Severity="+issue.getSeverity());
		log.debug("   Reporter="+issue.getReporter());
		if (issue.getReporter() != null) {
			log.debug("   ReporterName="+issue.getReporter().getName());
			log.debug("   ReporterTeam="+issue.getReporter().getTeam());
		}
		log.debug("   Assignee="+issue.getAssignee());
		if (issue.getAssignee() != null) {
			log.debug("   AssigneeName="+issue.getAssignee().getName());
			log.debug("   AssigneeTeam="+issue.getAssignee().getTeam());
		}
		log.debug("   Closed="+issue.isClosed());
		log.debug("   InProgress="+issue.isInProgress());
		log.debug("   Resolved="+issue.isResolved());
		log.debug("   Cancelled="+issue.isCancelled());
		log.debug("   Duplicate="+issue.isDuplicate());
		log.debug("   Open="+issue.isOpen());
		for (Comment c : issue.getComments()) {
			debug("   comment "+c.getId()+" by "+c.getAuthor().getName()+" ("+c.getCreationTimestamp()+"): ", c.getTheText());
		}
		for (Attachment a : issue.getAttachments()) {
			log.debug("   attachment="+a.getFilename()+ "("+a.getType()+"): "+a.getDescription());
		}
		for (IssueLink link : issue.getLinks()) {
			log.debug("   link="+link.getIssueId()+" ("+link.getLinkTypeName()+")");
		}
		for (Issue i : issue.getChildren()) {
			log.debug("   child="+i.getId());
		}
		for (String key : issue.getCustomFieldNames()) {
			if (key.endsWith("_id") || key.endsWith("_name")) continue;
			if (key.equals(DefaultIssue.LAZY_RETRIEVER)) continue;
			Object value = issue.get(key);
			if (value instanceof Collection) {
				for (Object o : (Collection<?>)value) {
					log.debug("   "+key+"="+o);
				}
			} else {
				log.debug("   "+key+"="+value);
			}
		}
	}

	private static void debug(String linePrefix, String value) {
		if (value != null) {
			String l[] = value.split("\\n");
			for (String s : l) {
				log.debug(linePrefix+s);
			}
		} else {
			log.debug(linePrefix+value);
		}
	}

	/**
	 * Debugs an comment in log file.
	 * @param comment to be debugged
	 */
	public static void debug(Comment comment) {
		log.debug("Comment:Id="+comment.getId());
		log.debug("   issue="+comment.getIssueId());
		log.debug("   author="+comment.getAuthor());
		log.debug("   creationTime="+comment.getCreationTimestamp());
		log.debug("   updateAuthor="+comment.getUpdateAuthor());
		log.debug("   updateTime="+comment.getUpdateTimestamp());
		debug("   text=", comment.getTheText());
		log.debug("   attachments="+comment.getAttachmentCount());
	}

	/**
	 * Parses the date by trying various formats.
	 * @param s string to parse
	 * @return date parsed
	 * @throws ParseException when the date could not be parsed
	 */
	public static Date parseDate(String s) throws ParseException {
		// JSON format
		if (s.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z")) {
			String date = s.substring(0,10);
			String time = s.substring(11,19);
			s = date + " " + time + " UTC";
		}
		try {
			return DefaultIssue.DATETIME_WITH_SEC_TZ().parse(s);
		} catch (ParseException e) { }
		try {
			return DefaultIssue.DATETIME_WITH_SEC().parse(s);
		} catch (ParseException e) { }
		try {
			return DefaultIssue.DATETIME_WITHOUT_SEC().parse(s);
		} catch (ParseException e) { }
		try {
			return DefaultIssue.DATE().parse(s);
		} catch (ParseException e) { }
		throw new ParseException("Cannot parse date: "+s, 0);
	}
}
