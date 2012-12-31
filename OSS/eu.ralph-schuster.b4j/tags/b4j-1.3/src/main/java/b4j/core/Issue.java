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

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Interface representing a Bugzilla bug record.
 * Usually instances are delivered by a Session search query.
 * @author Ralph Schuster
 *
 */
public interface Issue {

	/**
	 * Returns the version of Bugzilla this bug was retrieved from.
	 * @return Bugzilla software version or null if unknown
	 */
	public String getBugzillaVersion();


	/**
	 * Sets the Bugzilla version.
	 * @param bugzillaVersion - version to be set
	 */
	public void setBugzillaVersion(String bugzillaVersion);


	/**
	 * Returns the URI representing the Bugzilla instance this
	 * bug was retrieved from.
	 * @return URI of Bugzilla instance or null
	 */
	public String getBugzillaUri();


	/**
	 * Sets the Bugzilla instance URI
	 * @param bugzillaUri - the URI to set
	 */
	public void setBugzillaUri(String bugzillaUri);


	/**
	 * Returns the Bugzilla bug ID.
	 * @return the bug ID
	 */
	public String getId();


	/**
	 * Sets the Bugzilla bug ID
	 * @param id - the ID to set
	 */
	public void setId(String id);

	/**
	 * Returns the parent id
	 * @return ID of parent bug
	 */
	public String getParentId();


	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId);
	
	/**
	 * Returns the timestamp when this bug was created.
	 * @return the timestamp of creation
	 */
	public Date getCreationTimestamp();


	/**
	 * Sets the time of bug creation.
	 * @param creationTimestamp - the timestamp to set
	 */
	public void setCreationTimestamp(Date creationTimestamp);


	/**
	 * Returns the summary of the nug.
	 * @return the short description or summary.
	 */
	public String getShortDescription();


	/**
	 * Sets the summary or short description of this bug.
	 * @param shortDescription - the summary to set
	 */
	public void setShortDescription(String shortDescription);


	/**
	 * Returns the timestamp when this bug was last changed.
	 * @return timestamp of last change
	 */
	public Date getDeltaTimestamp();


	/**
	 * Sets the timestamp of last change.
	 * @param deltaTimestamp - the timestamp to set
	 */
	public void setDeltaTimestamp(Date deltaTimestamp);


	/**
	 * @return the reporterAccessible
	 */
	public boolean isReporterAccessible();


	/**
	 * @param reporterAccessible - the reporterAccessible to set
	 */
	public void setReporterAccessible(boolean reporterAccessible);


	/**
	 * @return the cclistAccessible
	 */
	public boolean isCclistAccessible();


	/**
	 * @param cclistAccessible - the cclistAccessible to set
	 */
	public void setCclistAccessible(boolean cclistAccessible);


	/**
	 * Returns the type id. 
	 * @return the type ID
	 */
	public long getType();


	/**
	 * Sets the ID of the type.
	 * @param typeId - the type ID to set
	 */
	public void setType(long typeId);


	/**
	 * @return the type name
	 */
	public String getTypeName();


	/**
	 * @param typeName the type name to set
	 */
	public void setTypeName(String typeName);


	/**
	 * Returns the classification name for this bug.
	 * @return classification name
	 */
	public String getClassification();


	/**
	 * Sets the classification name for this bug.
	 * @param classification - the classification name to set
	 */
	public void setClassification(String classification);


	/**
	 * Returns the product name for this bug.
	 * @return product name
	 */
	public String getProduct();


	/**
	 * Sets the product name for this bug.
	 * @param product - the product name to set
	 */
	public void setProduct(String product);


	/**
	 * Returns the component name for this bug.
	 * @return the component name
	 */
	public String getComponent();


	/**
	 * Sets the component name for this bug.
	 * @param component - the component name to set
	 */
	public void setComponent(String component);


	/**
	 * Returns the version of the product for this bug.
	 * Please do not mix with {@link #getBugzillaVersion()}.
	 * @return the version of the product
	 */
	public String getVersion();


	/**
	 * Sets the product version for this bug.
	 * Please do not mix with {@link #setBugzillaVersion(String)}.
	 * @param version - the product version to set
	 */
	public void setVersion(String version);


	/**
	 * Returns the reporter's platform.
	 * @return the reporter's platform
	 */
	public String getRepPlatform();


	/**
	 * Sets the reporter's platform.
	 * @param repPlatform - the platform to set
	 */
	public void setRepPlatform(String repPlatform);


	/**
	 * Returns the operating system for this bug.
	 * @return the operating system
	 */
	public String getOpSys();


	/**
	 * Sets the operating system for this bug.
	 * @param opSys - the operating system to set
	 */
	public void setOpSys(String opSys);

	/**
	 * Returns the link for this bug.
	 * @return the link
	 */
	public String getLink();


	/**
	 * Sets the link for this bug.
	 * @param link - the link to set
	 */
	public void setLink(String link);


	/**
	 * Returns the status of this bug.
	 * @return the status
	 */
	public String getStatus();


	/**
	 * Sets the status of this bug.
	 * @param status - the status to set
	 */
	public void setStatus(String status);


	/**
	 * Returns the resolution status of this bug.
	 * @return the resolution status
	 */
	public String getResolution();


	/**
	 * Sets the resolution status.
	 * @param resolution - the resolution to set
	 */
	public void setResolution(String resolution);


	/**
	 * Returns the priority of this bug.
	 * @return the priority
	 */
	public String getPriority();


	/**
	 * Sets the priority for this bug.
	 * @param priority - the priority to set
	 */
	public void setPriority(String priority);


	/**
	 * Returns the severity for this bug.
	 * @return the severity
	 */
	public String getSeverity();


	/**
	 * Sets the severity for this bug.
	 * @param severity - the severity to set
	 */
	public void setSeverity(String severity);


	/**
	 * Returns the target milestone.
	 * @return the targetMilestone
	 */
	public String getTargetMilestone();


	/**
	 * Sets the target milestone.
	 * @param targetMilestone - the target milestone to set
	 */
	public void setTargetMilestone(String targetMilestone);


	/**
	 * @return the everConfirmed
	 */
	public boolean isEverConfirmed();


	/**
	 * @param everConfirmed the everConfirmed to set
	 */
	public void setEverConfirmed(boolean everConfirmed);

	/**
	 * Returns the reporter's ID of this bug.
	 * @return the reporter ID
	 */
	public String getReporter();

	/**
	 * Sets the reporter's ID of this bug.
	 * @param reporter - the reporter ID to set
	 */
	public void setReporter(String reporter);

	/**
	 * Returns the reporter's name of this bug.
	 * @return the reporter name
	 */
	public String getReporterName();

	/**
	 * Sets the reporter's name of this bug.
	 * @param reporterName - the reporter name to set
	 */
	public void setReporterName(String reporterName);

	/**
	 * Returns the reporter's team of this bug.
	 * @return the reporter team
	 */
	public String getReporterTeam();

	/**
	 * Sets the reporter's team of this bug.
	 * @param reporterTeam - the reporter team to set
	 */
	public void setReporterTeam(String reporterTeam);


	/**
	 * Returns the assignee's name of this bug.
	 * @return the assignee name
	 */
	public String getAssigneeName();


	/**
	 * Sets the assignee's name for this bug.
	 * @param assigneeName - the assignee name to set
	 */
	public void setAssigneeName(String assigneeName);

	/**
	 * Returns the assignee's team of this bug.
	 * @return the assignee team
	 */
	public String getAssigneeTeam();


	/**
	 * Sets the assignee's team for this bug.
	 * @param assigneeTeam - the assignee team to set
	 */
	public void setAssigneeTeam(String assigneeTeam);

	/**
	 * Returns the assignee's ID of this bug.
	 * @return the assignee ID
	 */
	public String getAssignee();

	/**
	 * Sets the assignee's ID for this bug.
	 * @param assignee - the assignee ID to set
	 */
	public void setAssignee(String assignee);

	/**
	 * Returns the QA contact.
	 * @return the QA contact
	 */
	public String getQaContact();


	/**
	 * Sets the QA contact.
	 * @param qaContact - the QA contact to set
	 */
	public void setQaContact(String qaContact);


	/**
	 * Returns the file location given in this bug.
	 * @return the file location
	 */
	public String getFileLocation();


	/**
	 * Sets the file location information.
	 * @param fileLocation - the file location to set
	 */
	public void setFileLocation(String fileLocation);

	/**
	 * Adds a email to the CC list.
	 * @param e - the CC to add
	 * @return true if CC was set successfully
	 */
	public boolean addCc(String e);

	/**
	 * Adds multiple emails to the CC list.
	 * @param c - the CCs to add
	 * @return true if CCs could be added successfully
	 */
	public boolean addAllCc(Collection<? extends String> c);

	/**
	 * Clears the CC list.
	 */
	public void clearCc();

	/**
	 * Returns all CCs of this bug.
	 * @return iterator of all CCs
	 */
	public Iterator<String> getCcIterator();

	/**
	 * Removes a CC.
	 * @param o - the CC to remove
	 * @return true if CC was found and removed
	 */
	public boolean removeCc(Object o);

	/**
	 * Removes all given CCs from this bug. 
	 * @param c - list of CCs to remove
	 * @return true if CCs could be found and removed
	 */
	public boolean removeAllCc(Collection<?> c);

	/**
	 * Returns the number of CCs.
	 * @return number of CCs
	 */
	public int getCcCount();


	/**
	 * Creates a new and empty long description text.
	 * @return a fresh and new long description record.
	 */
	public LongDescription addLongDescription();

	/**
	 * Removes all long description texts.
	 */
	public void clearLongDescriptions();

	/**
	 * Returns all long description records.
	 * @return iterator on all long descriptions
	 */
	public Iterator<LongDescription> getLongDescriptionIterator();

	/**
	 * Removes a specific long description record.
	 * @param o - the record to remove
	 * @return true if record could be removed
	 */
	public boolean removeLongDescription(LongDescription o);

	/**
	 * Returns the number of long description texts.
	 * @return number of long description records
	 */
	public int getLongDescriptionCount();

	/**
	 * Returns the comment with given id.
	 * @param id id of comment
	 * @return comment or null if it doesn't exist.
	 */
	public LongDescription getLongDescription(String id);
	
	/**
	 * @return the blocked
	 */
	public long getBlocked();


	/**
	 * @param blocked the blocked to set
	 */
	public void setBlocked(long blocked);

	/**
	 * Creates and adds an attachment to this bug record.
	 * @return the attachment created
	 */
	public Attachment addAttachment();


	/**
	 * Removes all attachments.
	 */
	public void clearAttachments();


	/**
	 * Returns all attachments.
	 * @return iterator on all attachments.
	 */
	public Iterator<Attachment> getAttachmentIterator();


	/**
	 * Removes an attachment.
	 * @param o - the attachment to remove
	 * @return true if attachment was found and removed
	 */
	public boolean removeAttachment(Attachment o);

	/**
	 * Returns the number of attachments.
	 * @return number of attachments
	 */
	public int getAttachmentCount();

	/**
	 * returns the attachment with the given id.
	 * @param id id of attachment.
	 * @return Attachment or null if it doesn't exist.
	 * @since 1.3
	 */
	public Attachment getAttachment(long id);
	
	/**
	 * Adds a custom field value.
	 * This is a placeholder for customized fields in Bugzilla. A field
	 * can have multiple values.
	 * @param key - name of field
	 * @param value - value of field
	 */
	public void setCustomField(String key, String value);
	
	/**
	 * Returns a list of all values of a field (or null)
	 * @param key - name of field
	 * @return list of values
	 */
	public List<String> getCustomField(String key);
	
	/**
	 * Returns first value of a field (or null)
	 * @param key - name of field
	 * @return first value of this field
	 */
	public String getCustomFieldString(String key);
	
	/**
	 * Returns the n-th value of a field (or null)
	 * @param key - name of field
	 * @param idx - index of value
	 * @return n-th value of field or null if not set
	 */
	public String getCustomFieldString(String key, int idx);
	
	/**
	 * Returns the names of all custom fields.
	 * @return iterator of customized field names
	 */
	public Iterator<String> getCustomFieldNames();
	
	/**
	 * Returns the number of custom fields.
	 * @return number customized field names
	 */
	public int getCustomFieldCount();
	
	/**
	 * Adds all values to a custom field.
	 * @param key - name of field
	 * @param values - list of values to add
	 */
	public void addAllCustomFields(String key, List<String> values);
	
	/**
	 * Adds all custom fields.
	 * @param p - map with names and values of fields
	 */
	public void addAllCustomFields(Map<String, List<String>> p);
	
	/**
	 * Removes a value from a custom field.
	 * @param key - name of field
	 * @param value - value to remove
	 */
	public void removeCustomField(String key, String value);
	
	/**
	 * Removes all values of a custom field.
	 * @param key - name of field.
	 */
	public void removeParameter(String key);
	
	/**
	 * Clears all custom fields.
	 */
	public void clearCustomFields();
	
	/**
	 * Tells whether this bug can be regarded as closed.
	 * @return true if bug is closed.
	 */
	public boolean isClosed();
	
	/**
	 * Tells whether this bug can be regarded as progressing.
	 * @return true if bug is in progress.
	 */
	public boolean isInProgress();
	
	/**
	 * Tells whether this bug can be regarded as resolved.
	 * @return true if bug is resolved.
	 */
	public boolean isResolved();
	
	/**
	 * Tells whether this bug can be regarded as cancelled.
	 * @return true if bug is cancelled.
	 */
	public boolean isCancelled();
	
	/**
	 * Tells whether this bug is a duplicate.
	 * @return true if bug is duplicate.
	 */
	public boolean isDuplicate();
	
	/**
	 * Tells whether a bug must be regarded as still open.
	 * @return true if bug is open
	 */
	public boolean isOpen();

	/**
	 * Returns the alias name of the bug.
	 * @return alias name of bug
	 */
	public String getAlias();
	
	/**
	 * Sets the alias name of this bug.
	 * @param alias - the new alias
	 */
	public void setAlias(String alias);
	
	/**
	 * Returns the whiteboard status.
	 * @return whiteboard status
	 */
	public String getWhiteboard();
	
	/**
	 * Sets the whiteboard status for this bug.
	 * @param whiteboard - the new whiteboard status.
	 */
	public void setWhiteboard(String whiteboard);
	
	/**
	 * Returns the estimated time in hours.
	 * @return estimated time in hours
	 */
	public double getEstimatedTime();
	
	/**
	 * Sets the estimated time in hours.
	 * @param estimatedTime - new estimated time in hours
	 */
	public void setEstimatedTime(double estimatedTime);
	
	/**
	 * Returns the remaining time in hours.
	 * @return remaining time in hours
	 */
	public double getRemainingTime();
	
	/**
	 * Sets the remaining time in hours.
	 * @param remainingTime - new remaining time in hours
	 */
	public void setRemainingTime(double remainingTime);
	
	/**
	 * Returns the actual time in hours.
	 * @return actual time in hours
	 */
	public double getActualTime();
	
	/**
	 * Sets the actual time in hours.
	 * @param actualTime - new actual time in hours
	 */
	public void setActualTime(double actualTime);
	
	/**
	 * Returns the deadline time.
	 * @return the deadline time
	 */
	public Date getDeadline();
	
	/**
	 * Sets the dealine time.
	 * @param deadline - the new dealine time
	 */
	public void setDeadline(Date deadline);
	
	public void addLink(IssueLink l);
	public List<IssueLink> getLinks();
	public int getLinkCount();
	
	public void addChild(IssueLink l);
	public List<IssueLink> getChildren();
	public int getChildCount();
	
	
}
