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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b4j.core.Attachment;
import b4j.core.DefaultIssue;
import b4j.core.Issue;
import b4j.core.IssueLink;
import b4j.core.LongDescription;

/**
 * Provides some useful methods for all classes.
 * @author Ralph Schuster
 *
 */
public class BugzillaUtils {

	private static Logger log = LoggerFactory.getLogger(BugzillaUtils.class);
	
	/** The Java version we are running in */
	private static String javaVersion = null;
	
	/**
	 * Returns true if version is in range of minVersion and maxVersion.
	 * Note that 1.2.0 is greater than 1.2
	 * @param minVersion - minimum required version (can be null)
	 * @param maxVersion - maximum required version (can be null)
	 * @param version - version to check
	 * @return true when version is within range
	 */
	public static boolean isCompatibleVersion(String minVersion, String maxVersion, String version) {
		if (version == null) return true;
		String vParts[] = version.split("\\.");
		String minParts[] = null;
		String maxParts[] = null;
		if (minVersion != null) minParts = minVersion.split("\\.");
		if (maxVersion != null) maxParts = maxVersion.split("\\.");
		
		// Check minimum version
		if ((minParts != null) && (compareVersion(minParts, vParts) > 0)) return false;
		// Check maximum version
		if ((maxParts != null) && (compareVersion(maxParts, vParts) < 0)) return false;
		
		return true;
	}
	
	/**
	 * Compares versions.
	 * @param v1 - version 1 divided into separate parts
	 * @param v2 - version 2 divided into separate parts
	 * @return -1 if v1 < v2, 1 if v1 > v2, 0 if v1 = v2
	 */
	public static int compareVersion(String v1[], String v2[]) {
		int maxCount = Math.max(v1.length, v2.length);
		for (int i=0; i<maxCount; i++) {
			try {
				long l1 = 0;
				if (i < v1.length) l1 = Long.parseLong(v1[i]);
				long l2 = 0;
				if (i <v2.length) l2 = Long.parseLong(v2[i]);
				if (l1 < l2) return -1;
				if (l1 > l2) return 1;
			} catch (NumberFormatException e) {
				int rc = v1[i].compareTo(v2[i]);
				if (rc != 0) return rc;
			}
		}
		
		// Usually equal here, but check remaining minor versions
		if (v1.length > v2.length) return 1;
		if (v1.length < v2.length) return -1;
		
		// Equal now
		return 0;
	}
	
	/**
	 * Makes a join of a string array.
	 * @param separator - the string to be used inbetween parts
	 * @param parts - the parts to join
	 * @return the joined string
	 */
	public static String join(String separator, String parts[]) {
		String s = "";
		for (int i=0; i<parts.length; i++) s += separator + parts[i];
		if (s.length() > 0) s = s.substring(separator.length());
		return s;
	}
	
	/**
	 * Recursively debugs objects and adds this in the string buffer.
	 * @param s string buffer to enhance
	 * @param o object to debug
	 */
	public static void debugObject(StringBuffer s, Object o) {
		if (o == null) {
			s.append("NULL");
		} else if (o instanceof Collection<?>) {
			s.append(o.getClass().getSimpleName());
			s.append('[');
			boolean isFirst = true;
			for (Object o2 : (Collection<?>)o) {
				if (!isFirst) s.append(',');
				isFirst = false;
				debugObject(s, o2);
			}
			s.append(']');
		} else if (o instanceof Map<?, ?>) {
			s.append(o.getClass().getSimpleName());
			s.append('[');
			boolean isFirst = true;
			for (Object key : ((Map<?,?>)o).keySet()) {
				if (!isFirst) s.append(',');
				isFirst = false;
				s.append(key);
				s.append('=');
				debugObject(s, ((Map<?,?>)o).get(key));
			}
			s.append(']');
		} else if (o.getClass().isArray()) {
			s.append('[');
			boolean isFirst = true;
			for (Object o2 : (Object[])o) {
				if (!isFirst) s.append(',');
				isFirst = false;
				debugObject(s, o2);
			}
			s.append(']');
		} else {
			s.append(o.getClass().getName());
			s.append('{');
			s.append(o.toString());
			s.append('}');
		}
	}

	/**
	 * Converts the object to an int.
	 * @param o object to be converted
	 * @return 0 if object is null, int value of object otherwise
	 */
	public static int getInt(Object o) {
		if (o == null) return 0;
		
		if (o instanceof Number) {
			return ((Number)o).intValue();
		}
		
		return Integer.parseInt(o.toString());
	}

	/**
	 * Converts the object to a long.
	 * @param o object to be converted
	 * @return 0 if object is null, long value of object otherwise
	 */
	public static long getLong(Object o) {
		if (o == null) return 0;
		
		if (o instanceof Number) {
			return ((Number)o).longValue();
		}
		
		return Long.parseLong(o.toString());
	}

	/**
	 * Converts the object to a double.
	 * @param o object to be converted
	 * @return 0 if object is null, double value of object otherwise
	 */
	public static double getDouble(Object o) {
		if (o == null) return 0;
		
		if (o instanceof Number) {
			return ((Number)o).longValue();
		}
		
		return Double.parseDouble(o.toString());
	}

	/**
	 * Converts the object to a boolean.
	 * @param o object to be converted
	 * @return false if object is null, boolean value of object otherwise
	 */
	public static boolean getBoolean(Object o) {
		if (o == null) return false;
		
		if (o instanceof Boolean) {
			return ((Boolean)o).booleanValue();
		}
		
		String s = o.toString().toLowerCase().trim();
		if (s.equals("1")) return true;
		if (s.equals("true")) return true;
		if (s.equals("on")) return true;
		if (s.equals("yes")) return true;
		if (s.equals("y")) return true;
		
		return false;
	}
	
	/**
	 * Converts the object to a date.
	 * @param o object to be converted
	 * @return null if object is null, Date value of object otherwise
	 */
	public static Date getDate(Object o, DateFormat format) {
		if (o == null) return null;
		try {
			String s = o.toString().trim();
			if (s.length() == 0) return null;
			return format.parse(s);
		} catch (ParseException e) {
			log.error("Cannot parse date: "+o.toString(), e);
		}
		return null;
	}
	
	/**
	 * Returns true when the runtime is Java 6.
	 * @return true if Java 6, false otherwise.
	 */
	public static boolean isJava6() {
		if (javaVersion == null) {
			javaVersion = System.getProperty("java.specification.version");
			if (javaVersion == null) {
				log.error("Cannot determine Java version.");
				javaVersion = "unknown";
			}
		}
		return javaVersion.equals("1.6");
	}
	
	/**
	 * Debugs an issue in log file.
	 * @param issue issue to be debugged
	 */
	public static void debug(Issue issue) {
		log.debug("BugzillaVersion="+issue.getBugzillaVersion());
		log.debug("BugzillaUri="+issue.getBugzillaUri());
		log.debug("Id="+issue.getId());
		log.debug("ParentId="+issue.getParentId());
		log.debug("CreationTimestamp="+issue.getCreationTimestamp());
		log.debug("ShortDescription="+issue.getShortDescription());
		log.debug("DeltaTimestamp="+issue.getDeltaTimestamp());
		log.debug("ReporterAccessible="+issue.isReporterAccessible());
		log.debug("CcListAccessible="+issue.isCclistAccessible());
		log.debug("Type="+issue.getType());
		log.debug("TypeName="+issue.getTypeName());
		log.debug("Classification="+issue.getClassification());
		log.debug("Product="+issue.getProduct());
		log.debug("Component="+issue.getComponent());
		log.debug("Version="+issue.getVersion());
		log.debug("RepPlatform="+issue.getRepPlatform());
		log.debug("OpSys="+issue.getOpSys());
		log.debug("Link="+issue.getLink());
		log.debug("Status="+issue.getStatus());
		log.debug("Resolution="+issue.getResolution());
		log.debug("Priority="+issue.getPriority());
		log.debug("Severity="+issue.getSeverity());
		log.debug("TargetMilestone="+issue.getTargetMilestone());
		log.debug("EverConfirmed="+issue.isEverConfirmed());
		log.debug("Reporter="+issue.getReporter());
		log.debug("ReporterName="+issue.getReporterName());
		log.debug("ReporterTeam="+issue.getReporterTeam());
		log.debug("Assignee="+issue.getAssignee());
		log.debug("AssigneeName="+issue.getAssigneeName());
		log.debug("AssigneeTeam="+issue.getAssigneeTeam());
		log.debug("QaContact="+issue.getQaContact());
		log.debug("FileLocation="+issue.getFileLocation());
		log.debug("Blocked="+issue.getBlocked());
		log.debug("Closed="+issue.isClosed());
		log.debug("InProgress="+issue.isInProgress());
		log.debug("Resolved="+issue.isResolved());
		log.debug("Cancelled="+issue.isCancelled());
		log.debug("Duplicate="+issue.isDuplicate());
		log.debug("Open="+issue.isOpen());
		log.debug("Alias="+issue.getAlias());
		log.debug("Whiteboard="+issue.getWhiteboard());
		log.debug("EstimatedTime="+issue.getEstimatedTime());
		log.debug("RemainingTime="+issue.getRemainingTime());
		log.debug("ActualTime="+issue.getActualTime());
		log.debug("Deadline="+issue.getDeadline());
		Iterator<String> ccList = issue.getCcIterator();
		while (ccList.hasNext()) {
			log.debug("cc="+ccList.next());
		}
		Iterator<LongDescription> ldList = issue.getLongDescriptionIterator();
		while (ldList.hasNext()) {
			LongDescription desc = ldList.next();
			log.debug("comment="+desc.getWho()+" ("+desc.getWhen()+"): "+desc.getTheText());
		}
		Iterator<Attachment> attList = issue.getAttachmentIterator();
		while (attList.hasNext()) {
			Attachment att = attList.next();
			log.debug("attachment="+att.getFilename()+ "("+att.getType()+"): "+att.getDescription());
		}
		for (IssueLink link : issue.getLinks()) {
			log.debug("link="+link.getIssueId()+" ("+link.getLinkTypeName()+")");
		}
		
		for (IssueLink link : issue.getChildren()) {
			log.debug("child="+link.getIssueId());
		}
	}
	
	/**
	 * Parses the date by trying various formats.
	 * @param s string to parse
	 * @return date parsed
	 * @throws ParseException when the date could not be parsed
	 */
	public static Date parseDate(String s) throws ParseException {
		try {
			return DefaultIssue.DATETIME_WITH_SEC_TZ.parse(s);
		} catch (ParseException e) { }
		try {
			return DefaultIssue.DATETIME_WITH_SEC.parse(s);
		} catch (ParseException e) { }
		try {
			return DefaultIssue.DATETIME_WITHOUT_SEC.parse(s);
		} catch (ParseException e) { }
		try {
			return DefaultIssue.DATE.parse(s);
		} catch (ParseException e) { }
		throw new ParseException("Cannot parse date: "+s, 0);
	}
}
