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
package b4j.core;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that represents a Bugzilla bug record.
 * Usually instances are delivered by a Session search query.
 * @author Ralph Schuster
 *
 */
public class DefaultIssue implements Issue {

	/**
	 * Formatter and Parser for XML-retrieved dates from Bugzilla.
	 * Format is yyyy-MM-dd HH:mm:ss
	 */
	public static final SimpleDateFormat DATETIME_WITH_SEC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * Formatter and Parser for XML-retrieved dates from Bugzilla.
	 * Format is yyyy-MM-dd HH:mm
	 */
	public static final SimpleDateFormat DATETIME_WITHOUT_SEC = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	/**
	 * Formatter and Parser for XML-retrieved dates from Bugzilla.
	 * Format is yyyy-MM-dd
	 */
	public static final SimpleDateFormat DATE = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * The standard severities used in Bugzilla.
	 * Currently this is (in order): "blocker", "critical", "major", "normal",
	 * "minor", "trivial" and "enhancement"
	 */
	public static final String SEVERITIES[] = new String[] {
		"blocker",
		"critical",
		"major",
		"normal",
		"minor",
		"trivial",
		"enhancement"
	};
	
	private String bugzillaVersion;
	private String bugzillaUri;
	private String id;
	private String link;
	private String parentId;
	private Date creationTimestamp;
	private String shortDescription;
	private Date deltaTimestamp;
	private boolean reporterAccessible;
	private boolean cclistAccessible;
	private long typeId;
	private String typeName;
	private String classification;
	private String product;
	private String component;
	private String version;
	private String repPlatform;
	private String opSys;
	private String status;
	private String resolution;
	private String priority;
	private String severity;
	private String targetMilestone;
	private boolean everConfirmed;
	private String reporter;
	private String reporterName;
	private String reporterTeam;
	private String assignee;
	private String assigneeName;
	private String assigneeTeam;
	private String qaContact;
	private List<LongDescription> longDescriptions;
	private String fileLocation;
	private List<String> cc;
	private List<Attachment> attachments;
	private long blocked;
	private Map<String,List<String>> customFields;
	private String alias;
	private String whiteboard;
	private double estimatedTime;
	private double remainingTime;
	private double actualTime;
	private Date deadline;
	private List<IssueLink> links;
	private List<IssueLink> children;

	/**
	 * Default Constructor.
	 */
	public DefaultIssue() {
		longDescriptions = new ArrayList<LongDescription>();
		cc = new ArrayList<String>();
		attachments = new ArrayList<Attachment>();
		customFields = new HashMap<String, List<String>>();
		creationTimestamp = new Date(0);
		deltaTimestamp = new Date(0);
		deadline = new Date(0);
		links = new ArrayList<IssueLink>();
		children = new ArrayList<IssueLink>();
	}

	
	/**
	 * Returns the version of Bugzilla this bug was retrieved from.
	 * @return Bugzilla software version or null if unknown
	 */
	@Override
	public String getBugzillaVersion() {
		return bugzillaVersion;
	}


	/**
	 * Sets the Bugzilla version.
	 * @param bugzillaVersion - version to be set
	 */
	@Override
	public void setBugzillaVersion(String bugzillaVersion) {
		this.bugzillaVersion = bugzillaVersion;
	}


	/**
	 * Returns the URI representing the Bugzilla instance this
	 * bug was retrieved from.
	 * @return URI of Bugzilla instance or null
	 */
	@Override
	public String getBugzillaUri() {
		return bugzillaUri;
	}


	/**
	 * Sets the Bugzilla instance URI
	 * @param bugzillaUri - the URI to set
	 */
	@Override
	public void setBugzillaUri(String bugzillaUri) {
		this.bugzillaUri = bugzillaUri;
	}


	/**
	 * Returns the Bugzilla bug ID.
	 * @return the bug ID
	 */
	@Override
	public String getId() {
		return id;
	}


	/**
	 * Sets the Bugzilla bug ID
	 * @param id - the ID to set
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}


	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	/**
	 * Returns the timestamp when this bug was created.
	 * @return the timestamp of creation
	 */
	@Override
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}


	/**
	 * Sets the time of bug creation.
	 * @param creationTimestamp - the timestamp to set
	 */
	@Override
	public void setCreationTimestamp(Date creationTimestamp) {
		if (creationTimestamp != null)
			this.creationTimestamp.setTime(creationTimestamp.getTime());
		else
			this.creationTimestamp.setTime(0);
	}


	/**
	 * Returns the summary of the nug.
	 * @return the short description or summary.
	 */
	@Override
	public String getShortDescription() {
		return shortDescription;
	}


	/**
	 * Sets the summary or short description of this bug.
	 * @param shortDescription - the summary to set
	 */
	@Override
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}


	/**
	 * Returns the timestamp when this bug was last changed.
	 * @return timestamp of last change
	 */
	@Override
	public Date getDeltaTimestamp() {
		return deltaTimestamp;
	}


	/**
	 * Sets the timestamp of last change.
	 * @param deltaTimestamp - the timestamp to set
	 */
	@Override
	public void setDeltaTimestamp(Date deltaTimestamp) {
		if (deltaTimestamp != null)
			this.deltaTimestamp.setTime(deltaTimestamp.getTime());
		else
			this.deltaTimestamp.setTime(getCreationTimestamp().getTime());
	}


	/**
	 * @return the reporterAccessible
	 */
	@Override
	public boolean isReporterAccessible() {
		return reporterAccessible;
	}


	/**
	 * @param reporterAccessible - the reporterAccessible to set
	 */
	@Override
	public void setReporterAccessible(boolean reporterAccessible) {
		this.reporterAccessible = reporterAccessible;
	}


	/**
	 * @return the cclistAccessible
	 */
	@Override
	public boolean isCclistAccessible() {
		return cclistAccessible;
	}


	/**
	 * @param cclistAccessible - the cclistAccessible to set
	 */
	@Override
	public void setCclistAccessible(boolean cclistAccessible) {
		this.cclistAccessible = cclistAccessible;
	}


	/**
	 * Returns the Bugzilla ID of the classification. 
	 * @return the classification ID
	 */
	@Override
	public long getType() {
		return typeId;
	}


	/**
	 * Sets the ID of the classification.
	 * @param typeId - the classification ID to set
	 */
	@Override
	public void setType(long typeId) {
		this.typeId = typeId;
	}


	/**
	 * @return the type name
	 */
	@Override
	public String getTypeName() {
		if (typeName != null) return typeName;
		return ""+getType();
	}


	/**
	 * @param typeName the type name to set
	 */
	@Override
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	/**
	 * Returns the product name for this bug.
	 * @return product name
	 */
	@Override
	public String getProduct() {
		return product;
	}


	/**
	 * Sets the product name for this bug.
	 * @param product - the product name to set
	 */
	@Override
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * Returns the classification name for this bug.
	 * @return classification name
	 */
	@Override
	public String getClassification() {
		return classification;
	}


	/**
	 * Sets the classification name for this bug.
	 * @param classification - the classification name to set
	 */
	@Override
	public void setClassification(String classification) {
		this.classification = classification;
	}



	/**
	 * Returns the component name for this bug.
	 * @return the component name
	 */
	@Override
	public String getComponent() {
		return component;
	}


	/**
	 * Sets the component name for this bug.
	 * @param component - the component name to set
	 */
	@Override
	public void setComponent(String component) {
		this.component = component;
	}


	/**
	 * Returns the version of the product for this bug.
	 * Please do not mix with {@link #getBugzillaVersion()}.
	 * @return the version of the product
	 */
	@Override
	public String getVersion() {
		return version;
	}


	/**
	 * Sets the product version for this bug.
	 * Please do not mix with {@link #setBugzillaVersion(String)}.
	 * @param version - the product version to set
	 */
	@Override
	public void setVersion(String version) {
		this.version = version;
	}


	/**
	 * Returns the reporter's platform.
	 * @return the reporter's platform
	 */
	@Override
	public String getRepPlatform() {
		return repPlatform;
	}


	/**
	 * Sets the reporter's platform.
	 * @param repPlatform - the platform to set
	 */
	@Override
	public void setRepPlatform(String repPlatform) {
		this.repPlatform = repPlatform;
	}


	/**
	 * Returns the operating system for this bug.
	 * @return the operating system
	 */
	@Override
	public String getOpSys() {
		return opSys;
	}


	/**
	 * Sets the operating system for this bug.
	 * @param opSys - the operating system to set
	 */
	@Override
	public void setOpSys(String opSys) {
		this.opSys = opSys;
	}


	/**
	 * Returns the status of this bug.
	 * @return the status
	 */
	@Override
	public String getStatus() {
		return status;
	}


	/**
	 * Sets the status of this bug.
	 * @param status - the status to set
	 */
	@Override
	public void setStatus(String status) {
		this.status = status;
	}


	/**
	 * Returns the resolution status of this bug.
	 * @return the resolution status
	 */
	@Override
	public String getResolution() {
		return resolution;
	}


	/**
	 * Sets the resolution status.
	 * @param resolution - the resolution to set
	 */
	@Override
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}


	/**
	 * Returns the priority of this bug.
	 * @return the priority
	 */
	@Override
	public String getPriority() {
		return priority;
	}


	/**
	 * Sets the priority for this bug.
	 * @param priority - the priority to set
	 */
	@Override
	public void setPriority(String priority) {
		this.priority = priority;
	}


	/**
	 * Returns the severity for this bug.
	 * @see #SEVERITIES
	 * @return the severity
	 */
	@Override
	public String getSeverity() {
		return severity;
	}


	/**
	 * Sets the severity for this bug.
	 * @see #SEVERITIES
	 * @param severity - the severity to set
	 */
	@Override
	public void setSeverity(String severity) {
		this.severity = severity;
	}


	/**
	 * Returns the target milestone.
	 * @return the targetMilestone
	 */
	@Override
	public String getTargetMilestone() {
		return targetMilestone;
	}


	/**
	 * Sets the target milestone.
	 * @param targetMilestone - the target milestone to set
	 */
	@Override
	public void setTargetMilestone(String targetMilestone) {
		this.targetMilestone = targetMilestone;
	}


	/**
	 * @return the everConfirmed
	 */
	@Override
	public boolean isEverConfirmed() {
		return everConfirmed;
	}


	/**
	 * @param everConfirmed the everConfirmed to set
	 */
	@Override
	public void setEverConfirmed(boolean everConfirmed) {
		this.everConfirmed = everConfirmed;
	}


	/**
	 * Returns the reporter of this bug.
	 * @return the reporter
	 */
	@Override
	public String getReporter() {
		return reporter;
	}


	/**
	 * Sets the reporter of this bug.
	 * @param reporter - the reporter to set
	 */
	@Override
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}


	/**
	 * @return the reporterName
	 */
	public String getReporterName() {
		if (reporterName != null) return reporterName;
		return getReporter();
	}


	/**
	 * @param reporterName the reporterName to set
	 */
	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}


	/**
	 * @return the assignee
	 */
	public String getAssignee() {
		return assignee;
	}


	/**
	 * @param assignee the assignee to set
	 */
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}


	/**
	 * @return the assigneeName
	 */
	public String getAssigneeName() {
		if (assigneeName != null) return assigneeName;
		return getAssignee();
	}


	/**
	 * @param assigneeName the assigneeName to set
	 */
	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	/**
	 * Returns the QA contact.
	 * @return the QA contact
	 */
	@Override
	public String getQaContact() {
		return qaContact;
	}


	/**
	 * Sets the QA contact.
	 * @param qaContact - the QA contact to set
	 */
	@Override
	public void setQaContact(String qaContact) {
		this.qaContact = qaContact;
	}


	/**
	 * Returns the file location given in this bug.
	 * @return the file location
	 */
	@Override
	public String getFileLocation() {
		return fileLocation;
	}


	/**
	 * Sets the file location information.
	 * @param fileLocation - the file location to set
	 */
	@Override
	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	/**
	 * Adds a email to the CC list.
	 * @param e - the CC to add
	 * @return true if CC was set successfully
	 */
	@Override
	public boolean addCc(String e) {
		return cc.add(e);
	}

	/**
	 * Adds multiple emails to the CC list.
	 * @param c - the CCs to add
	 * @return true if CCs could be added successfully
	 */
	@Override
	public boolean addAllCc(Collection<? extends String> c) {
		return cc.addAll(c);
	}

	/**
	 * Clears the CC list.
	 */
	@Override
	public void clearCc() {
		cc.clear();
	}

	/**
	 * Returns all CCs of this bug.
	 * @return iterator of all CCs
	 */
	@Override
	public Iterator<String> getCcIterator() {
		return cc.iterator();
	}

	/**
	 * Removes a CC.
	 * @param o - the CC to remove
	 * @return true if CC was found and removed
	 */
	@Override
	public boolean removeCc(Object o) {
		return cc.remove(o);
	}

	/**
	 * Removes all given CCs from this bug. 
	 * @param c - list of CCs to remove
	 * @return true if CCs could be found and removed
	 */
	@Override
	public boolean removeAllCc(Collection<?> c) {
		return cc.removeAll(c);
	}

	/**
	 * Returns the number of CCs.
	 * @return number of CCs
	 */
	@Override
	public int getCcCount() {
		return cc.size();
	}


	/**
	 * Creates a new and empty long description text.
	 * @return a fresh and new long description record.
	 */
	@Override
	public LongDescription addLongDescription() {
		LongDescription rc = new DefaultLongDescription();
		longDescriptions.add(rc);
		return rc;
	}

	/**
	 * Removes all long description texts.
	 */
	@Override
	public void clearLongDescriptions() {
		longDescriptions.clear();
	}

	/**
	 * Returns all long description records.
	 * @return iterator on all long descriptions
	 */
	@Override
	public Iterator<LongDescription> getLongDescriptionIterator() {
		return longDescriptions.iterator();
	}

	/**
	 * Removes a specific long description record.
	 * @param o - the record to remove
	 * @return true if record could be removed
	 */
	@Override
	public boolean removeLongDescription(LongDescription o) {
		return longDescriptions.remove(o);
	}

	/**
	 * Returns the number of long description texts.
	 * @return number of long description records
	 */
	@Override
	public int getLongDescriptionCount() {
		return longDescriptions.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LongDescription getLongDescription(String id) {
		for (LongDescription desc : longDescriptions) {
			if (desc.getId().equals(id)) return desc;
		}
		return null;
	}


	/**
	 * @return the blocked
	 */
	@Override
	public long getBlocked() {
		return blocked;
	}

	/**
	 * @param blocked the blocked to set
	 */
	@Override
	public void setBlocked(long blocked) {
		this.blocked = blocked;
	}


	/**
	 * Creates and adds an attachment to this bug record.
	 * @return the attachment created
	 */
	@Override
	public Attachment addAttachment() {
		Attachment rc = new DefaultAttachment();
		attachments.add(rc);
		return rc;
	}


	/**
	 * Removes all attachments.
	 */
	@Override
	public void clearAttachments() {
		attachments.clear();
	}


	/**
	 * Returns all attachments.
	 * @return iterator on all attachments.
	 */
	@Override
	public Iterator<Attachment> getAttachmentIterator() {
		return attachments.iterator();
	}


	/**
	 * Removes an attachment.
	 * @param o - the attachment to remove
	 * @return true if attachment was found and removed
	 */
	@Override
	public boolean removeAttachment(Attachment o) {
		return attachments.remove(o);
	}


	/**
	 * Returns the number of attachments.
	 * @return number of attachments
	 */
	@Override
	public int getAttachmentCount() {
		return attachments.size();
	}

	/**
	 * returns the attachment with the given id.
	 * @param id id of attachment.
	 * @return Attachment or null if it doesn't exist.
	 */
	public Attachment getAttachment(long id) {
		for (Attachment a : attachments) {
			if (a.getId() == id) return a;
		}
		return null;
	}
	
	/**
	 * Adds a custom field value.
	 * This is a placeholder for customized fields in Bugzilla. A field
	 * can have multiple values.
	 * @param key - name of field
	 * @param value - value of field
	 */
	@Override
	public void setCustomField(String key, String value) {
		List<String> values = customFields.get(key);
		if (values == null) {
			values = new ArrayList<String>();
			customFields.put(key, values);
		}
		values.add(value);
	}
	
	/**
	 * Returns a list of all values of a field (or null)
	 * @param key - name of field
	 * @return list of values
	 */
	@Override
	public List<String> getCustomField(String key) {
		return customFields.get(key);
	}
	
	/**
	 * Returns first value of a field (or null)
	 * @param key - name of field
	 * @return first value of this field
	 */
	@Override
	public String getCustomFieldString(String key) {
		return getCustomFieldString(key, 0);
	}
	
	/**
	 * Returns the n-th value of a field (or null)
	 * @param key - name of field
	 * @param idx - index of value
	 * @return n-th value of field or null if not set
	 */
	@Override
	public String getCustomFieldString(String key, int idx) {
		List<String> values = customFields.get(key);
		if (values == null) return null;
		if (idx < 0) idx = 0;
		if (idx < values.size()) return values.get(idx);
		return values.get(0);
	}
	
	/**
	 * Returns the names of all custom fields.
	 * @return iterator of customized field names
	 */
	@Override
	public Iterator<String> getCustomFieldNames() {
		return customFields.keySet().iterator();
	}
	
	/**
	 * Returns the number of custom fields.
	 * @return number customized field names
	 */
	@Override
	public int getCustomFieldCount() {
		return customFields.size();
	}
	
	/**
	 * Adds all values to a custom field.
	 * @param key - name of field
	 * @param values - list of values to add
	 */
	@Override
	public void addAllCustomFields(String key, List<String> values) {
		List<String> values2 = customFields.get(key);
		if (values2 == null) {
			values2 = new ArrayList<String>();
			customFields.put(key, values2);
		}
		values2.addAll(values);
	}
	
	/**
	 * Adds all custom fields.
	 * @param p - map with names and values of fields
	 */
	@Override
	public void addAllCustomFields(Map<String, List<String>> p) {
		Iterator<String> keys = p.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			addAllCustomFields(key, p.get(key));
		}
	}
	
	/**
	 * Removes a value from a custom field.
	 * @param key - name of field
	 * @param value - value to remove
	 */
	@Override
	public void removeCustomField(String key, String value) {
		List<String> values = customFields.get(key);
		if (values == null) return;
		values.remove(value);
		if (values.size() == 0) customFields.remove(key);
	}
	
	/**
	 * Removes all values of a custom field.
	 * @param key - name of field.
	 */
	@Override
	public void removeParameter(String key) {
		List<String> values = customFields.get(key);
		if (values == null) return;
		customFields.remove(key);
	}
	
	/**
	 * Clears all custom fields.
	 */
	@Override
	public void clearCustomFields() {
		customFields.clear();
	}
	
	/**
	 * Returns whether this bug can be regarded as closed.
	 * A closed bug can have status: RESOLVED, VERIFIED, CLOSED
	 * @return true if bug is closed.
	 */
	@Override
	public boolean isClosed() {
		String s = getStatus();
		if (s == null) return false;
		return s.equalsIgnoreCase("CLOSED");
	}
	
	
	/**
	 * Returns whether this bug can be regarded as closed.
	 * A closed bug can have status: RESOLVED, VERIFIED, CLOSED
	 * @return true if bug is closed.
	 */
	@Override
	public boolean isResolved() {
		String s = getStatus();
		if (s == null) return false;
		return s.equalsIgnoreCase("RESOLVED") || s.equalsIgnoreCase("VERIFIED") || s.equalsIgnoreCase("CLOSED");
	}
	
	public boolean isCancelled() {
		String s = getStatus();
		if (s == null) return false;
		boolean rc = s.toUpperCase().startsWith("CANCEL");
		if (!rc) {
			s = getResolution();
			if (s != null) rc = s.toUpperCase().startsWith("CANCEL");
		}
		return rc;
	}
	
	public boolean isDuplicate() {
		String s = getStatus();
		if (s == null) return false;
		boolean rc = s.toUpperCase().startsWith("DUPLICATE");
		if (!rc) {
			s = getResolution();
			if (s != null) rc = s.toUpperCase().startsWith("DUPLICATE");
		}
		return rc;
	}
	
	/**
	 * Returns whether a bug must be regarded as still open.
	 * An open bug can have status: UNCONFIRMED, NEW, ASSIGNED, REOPENED
	 * @return true if bug is open
	 */
	@Override
	public boolean isOpen() {
		String s = getStatus();
		if (s == null) return true;
		return s.toUpperCase().indexOf("OPEN") >= 0 || 
		s.equalsIgnoreCase("NEW") || s.equalsIgnoreCase("CREATED") ||
		s.equalsIgnoreCase("UNCONFIRMED") || s.equalsIgnoreCase("ASSIGNED");
	}
	
	/**
	 * Returns whether a bug must be regarded as in progress.
	 * An bug in progress is neither open or closed.
	 * @return true if bug is in progress
	 */
	@Override
	public boolean isInProgress() {
		return !isOpen() && !isClosed();
	}
	
	/**
	 * Returns the alias name of the bug.
	 * @return alias name of bug
	 */
	@Override
	public String getAlias() {
		return alias;
	}
	
	/**
	 * Sets the alias name of this bug.
	 * @param alias - the new alias
	 */
	@Override
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	/**
	 * Returns the whiteboard status.
	 * @return whiteboard status
	 */
	@Override
	public String getWhiteboard() {
		return whiteboard;
	}
	
	/**
	 * Sets the whiteboard status for this bug.
	 * @param whiteboard - the new whiteboard status.
	 */
	@Override
	public void setWhiteboard(String whiteboard) {
		this.whiteboard = whiteboard;
	}
	
	/**
	 * Returns the estimated time in hours.
	 * @return estimated time in hours
	 */
	@Override
	public double getEstimatedTime() {
		return estimatedTime;
	}
	
	/**
	 * Sets the estimated time in hours.
	 * @param estimatedTime - new estimated time in hours
	 */
	@Override
	public void setEstimatedTime(double estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	
	/**
	 * Returns the remaining time in hours.
	 * @return remaining time in hours
	 */
	@Override
	public double getRemainingTime() {
		return remainingTime;
	}
	
	/**
	 * Sets the remaining time in hours.
	 * @param remainingTime - new remaining time in hours
	 */
	@Override
	public void setRemainingTime(double remainingTime) {
		this.remainingTime = remainingTime;
	}
	
	/**
	 * Returns the actual time in hours.
	 * @return actual time in hours
	 */
	@Override
	public double getActualTime() {
		return actualTime;
	}
	
	/**
	 * Sets the actual time in hours.
	 * @param actualTime - new actual time in hours
	 */
	@Override
	public void setActualTime(double actualTime) {
		this.actualTime = actualTime;
	}
	
	/**
	 * Returns the deadline time.
	 * @return the deadline time
	 */
	@Override
	public Date getDeadline() {
		return deadline;
	}
	
	/**
	 * Sets the dealine time.
	 * @param deadline - the new dealine time
	 */
	@Override
	public void setDeadline(Date deadline) {
		if (deadline != null)
			this.deadline.setTime(deadline.getTime());
		else
			this.deadline.setTime(0);
	}
	

	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}


	/**
	 * @param link the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}


	/**
	 * @return the reporterTeam
	 */
	public String getReporterTeam() {
		return reporterTeam;
	}

	public void addLink(IssueLink l) {
		links.add(l);
	}
	
	public List<IssueLink> getLinks() {
		return links;
	}
	
	public int getLinkCount() {
		return links.size();
	}
	
	public void addChild(IssueLink l) {
		children.add(l);
	}
	
	public List<IssueLink> getChildren() {
		return children;
	}
	
	public int getChildCount() {
		return children.size();
	}
	
	/**
	 * @param reporterTeam the reporterTeam to set
	 */
	public void setReporterTeam(String reporterTeam) {
		this.reporterTeam = reporterTeam;
	}


	/**
	 * @return the assigneeTeam
	 */
	public String getAssigneeTeam() {
		return assigneeTeam;
	}


	/**
	 * @param assigneeTeam the assigneeTeam to set
	 */
	public void setAssigneeTeam(String assigneeTeam) {
		this.assigneeTeam = assigneeTeam;
	}


	/**
	 * A long description entry.
	 * This object is used to store information about long description texts.
	 * @author ralph
	 *
	 */
	public class DefaultLongDescription implements LongDescription {
		
		private String id;
		private String who;
		private Date when;
		private String theText;
		private Date lastUpdate;
		private String updateAuthor;
		private Set<Long> attachments;
		
		/**
		 * Default Constructor.
		 */
		public DefaultLongDescription() {
			id = "unknown";
			when = new Date(0);
			attachments = new HashSet<Long>();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getId() {
			return id;
		}


		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * Returns the Bugzilla bug report this attachment belongs to.
		 * @return the bug record of this attachment.
		 */
		@Override
		public Issue getBugzillaBug() {
			return DefaultIssue.this;
		}

		/**
		 * Returns the author of this text.
		 * @return the editor
		 */
		@Override
		public String getWho() {
			if (who != null) return who;
			return getReporter();
		}

		/**
		 * Sets the author of this text
		 * @param who - the author to set
		 */
		@Override
		public void setWho(String who) {
			this.who = who;
		}

		/**
		 * Returns the time of creation of this entry.
		 * @return the time of creation
		 */
		@Override
		public Date getWhen() {
			if (when.getTime() > 0) return when;
			return getCreationTimestamp();
		}

		/**
		 * Sets the time of creation.
		 * @param when - the timestamp to set
		 */
		@Override
		public void setWhen(Date when) {
			this.when.setTime(when.getTime());
		}

		/**
		 * Returns the actual text.
		 * @return the text
		 */
		@Override
		public String getTheText() {
			return theText;
		}

		/**
		 * Sets the text of the description.
		 * @param theText - the text to set
		 */
		@Override
		public void setTheText(String theText) {
			this.theText = theText;
		}

		/**
		 * Returns the date of last update.
		 * @return date of last update.
		 */
		@Override
		public Date getLastUpdate() {
			if (lastUpdate != null) return lastUpdate;
			return getWhen();
		}
		
		/**
		 * Sets the date of last update.
		 * @param lastUpdate date to be set
		 */
		@Override
		public void setLastUpdate(Date lastUpdate) {
			this.lastUpdate = lastUpdate;
		}
		
		/**
		 * Returns the author of the last update.
		 * @return name of author
		 */
		@Override
		public String getUpdateAuthor() {
			return updateAuthor;
		}
		
		/**
		 * Sets the last update's author.
		 * @param updateAuthor author to be set
		 */
		@Override
		public void setUpdateAuthor(String updateAuthor) {
			this.updateAuthor = updateAuthor;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addAttachment(long id) {
			attachments.add(id);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void clearAttachments() {
			attachments.clear();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<Attachment> getAttachmentIterator() {
			return new Iterator<Attachment>() {
				private Attachment nextAttachment = null;
				private Iterator<Long> idIterator = attachments.iterator();
				
				@Override
				public boolean hasNext() {
					if (nextAttachment == null) retrieveNext();
					return nextAttachment != null;
				}

				@Override
				public Attachment next() {
					if (nextAttachment == null) retrieveNext();
					return nextAttachment;
				}

				protected void retrieveNext() {
					while ((nextAttachment == null) && idIterator.hasNext()) {
						nextAttachment = getAttachment(idIterator.next());
					}
				}
				
				@Override
				public void remove() {
					throw new UnsupportedOperationException("remove is not supported");
				}
				
			};
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean removeAttachment(Attachment o) {
			// TODO Auto-generated method stub
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getAttachmentCount() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		
	}
	
	
	/**
	 * An attachment definition.
	 * This object represents an attachment for a bug.
	 * @author Ralph Schuster
	 *
	 */
	public class DefaultAttachment implements Attachment {
		
		private long id;
		private Date date;
		private String description;
		private String filename;
		private String type;
		
		/**
		 * Default constructor.
		 */
		public DefaultAttachment() {
			date = new Date(0);
		}
	
		/**
		 * Returns the Bugzilla bug report this attachment belongs to.
		 * @return the bug record of this attachment.
		 */
		@Override
		public Issue getBugzillaBug() {
			return DefaultIssue.this;
		}


		/**
		 * Returns the ID of this attachment.
		 * @return the ID
		 */
		@Override
		public long getId() {
			return id;
		}

		/**
		 * Sets the ID of the attachment.
		 * @param id - the ID to set
		 */
		@Override
		public void setId(long id) {
			this.id = id;
		}

		/**
		 * Returns the date of the attachment.
		 * @return the date
		 */
		@Override
		public Date getDate() {
			return date;
		}

		/**
		 * Sets the date of the attachment.
		 * @param date - the date to set
		 */
		@Override
		public void setDate(Date date) {
			this.date.setTime(date.getTime());
		}

		/**
		 * Returns the description of the attachment.
		 * @return the description
		 */
		@Override
		public String getDescription() {
			return description;
		}

		/**
		 * Sets the description of the attachment.
		 * @param description - the description to set
		 */
		@Override
		public void setDescription(String description) {
			this.description = description;
		}

		/**
		 * Returns the filename.
		 * @return the filename
		 */
		@Override
		public String getFilename() {
			return filename;
		}

		/**
		 * Sets the filename.
		 * @param filename - the filename to set
		 */
		@Override
		public void setFilename(String filename) {
			this.filename = filename;
		}

		/**
		 * Returns the type of the attachment.
		 * @return the type
		 */
		@Override
		public String getType() {
			return type;
		}

		/**
		 * Sets the type of the attachment.
		 * @param type - the type to set
		 */
		@Override
		public void setType(String type) {
			this.type = type;
		}
	}
	
	/**
	 * Describes a link to another issue
	 * @author Ralph Schuster
	 *
	 */
	public static class DefaultLink implements IssueLink {
		
		private int linkType;
		private String linkTypeName;
		private boolean inward;
		private String linkTypeDescription;
		private String issueId;
		
		public DefaultLink() {
			
		}

		public DefaultLink(int linkType, String linkTypeName, boolean inward,
				String linkTypeDescription, String issueId) {
			super();
			this.linkType = linkType;
			this.linkTypeName = linkTypeName;
			this.inward = inward;
			this.linkTypeDescription = linkTypeDescription;
			this.issueId = issueId;
		}

		/**
		 * @return the linkType
		 */
		public int getLinkType() {
			return linkType;
		}

		/**
		 * @param linkType the linkType to set
		 */
		public void setLinkType(int linkType) {
			this.linkType = linkType;
		}

		/**
		 * @return the linkTypeName
		 */
		public String getLinkTypeName() {
			return linkTypeName;
		}

		/**
		 * @param linkTypeName the linkTypeName to set
		 */
		public void setLinkTypeName(String linkTypeName) {
			this.linkTypeName = linkTypeName;
		}

		/**
		 * @return the inward
		 */
		public boolean isInward() {
			return inward;
		}

		/**
		 * @param inward the inward to set
		 */
		public void setInward(boolean inward) {
			this.inward = inward;
		}

		/**
		 * @return the linkTypeDescription
		 */
		public String getLinkTypeDescription() {
			return linkTypeDescription;
		}

		/**
		 * @param linkTypeDescription the linkTypeDescription to set
		 */
		public void setLinkTypeDescription(String linkTypeDescription) {
			this.linkTypeDescription = linkTypeDescription;
		}

		/**
		 * @return the issueId
		 */
		public String getIssueId() {
			return issueId;
		}

		/**
		 * @param issueId the issueId to set
		 */
		public void setIssueId(String issueId) {
			this.issueId = issueId;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getName()+"[id="+getId()+";summary="+getShortDescription()+";status="+getStatus()+"]";
	}

	
}
