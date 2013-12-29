/*
 * This file is part of issuezilla for Java.
 *
 *  issuezilla for Java is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  issuezilla for Java is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with issuezilla for Java.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package b4j.core;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Interface representing a issuezilla issue record.
 * Usually instances are delivered by a Session search query.
 * @author Ralph Schuster
 *
 */
public interface Issue {

	/**
	 * Returns the version of server this issue was retrieved from.
	 * @return server software version or null if unknown
	 */
	public String getServerVersion();

	/**
	 * Sets the server version.
	 * @param version - version to be set
	 */
	public void setServerVersion(String version);

	/**
	 * Returns the URI representing the server instance this
	 * issue was retrieved from.
	 * @return URI of server or null
	 */
	public String getServerUri();

	/**
	 * Sets the server instance URI
	 * @param uri - the URI to set
	 */
	public void setServerUri(String uri);

	/**
	 * Returns the URI of this issue.
	 * @return URI of issue or null
	 */
	public String getUri();

	/**
	 * Sets the issue URI.
	 * @param uri - the URI to set
	 */
	public void setUri(String uri);

	/**
	 * Returns the issue ID.
	 * @return the issue ID
	 */
	public String getId();

	/**
	 * Sets the issue ID
	 * @param id - the ID to set
	 */
	public void setId(String id);

	/**
	 * Returns the parent id
	 * @return ID of parent issue
	 */
	public String getParentId();

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId);

	/**
	 * Returns the timestamp when this issue was created.
	 * @return the timestamp of creation
	 */
	public Date getCreationTimestamp();

	/**
	 * Sets the time of issue creation.
	 * @param creationTimestamp - the timestamp to set
	 */
	public void setCreationTimestamp(Date creationTimestamp);

	/**
	 * Returns the summary of the issue.
	 * @return the short description or summary.
	 */
	public String getSummary();

	/**
	 * Sets the summary or short description of this issue.
	 * @param summary - the summary to set
	 */
	public void setSummary(String summary);

	/**
	 * Returns the description of the issue.
	 * @return the long description or summary.
	 */
	public String getDescription();

	/**
	 * Sets the description of this issue.
	 * @param description - the description to set
	 */
	public void setDescription(String description);

	/**
	 * Returns the timestamp when this issue was last changed.
	 * @return timestamp of last change
	 */
	public Date getUpdateTimestamp();

	/**
	 * Sets the timestamp of last change.
	 * @param updateTimestamp - the timestamp to set
	 */
	public void setUpdateTimestamp(Date updateTimestamp);

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
	 * Returns the type. 
	 * @return the type
	 */
	public IssueType getType();

	/**
	 * Sets the type.
	 * @param type - the type to set
	 */
	public void setType(IssueType type);

	/**
	 * Returns the classification name for this issue.
	 * @return classification name
	 */
	public String getClassification();

	/**
	 * Sets the classification name for this issue.
	 * @param classification - the classification name to set
	 */
	public void setClassification(String classification);

	/**
	 * Returns the project/product for this issue.
	 * @return project
	 */
	public Project getProject();

	/**
	 * Sets the project/product for this issue.
	 * @param project - the project/product to set
	 */
	public void setProject(Project project);

	/**
	 * Returns the components for this issue.
	 * @return the components
	 */
	public Collection<Component> getComponents();

	/**
	 * Sets the components for this issue.
	 * @param components - the components to set
	 */
	public void setComponents(Collection<Component> components);

	/**
	 * Adds the components for this issue.
	 * @param components - the components to add
	 */
	public void addComponents(Collection<Component> components);

	/**
	 * Adds the components for this issue.
	 * @param components - the components to add
	 */
	public void addComponents(Component... components);

	/**
	 * Removes the components for this issue.
	 * @param components - the components to remove
	 */
	public void removeComponents(Collection<Component> components);

	/**
	 * Removes the components for this issue.
	 * @param components - the components to remove
	 */
	public void removeComponents(Component... components);

	/**
	 * Removes the components for this issue.
	 */
	public void removeAllComponents();

	/**
	 * Returns the number of components for this issue.
	 * @return number of components
	 */
	public int getComponentCount();

	/**
	 * Returns the (fix) versions of the project/product for this issue.
	 * Fix versions are versions where the resolution of this issue will be
	 * delivered to.
	 * @return the fix versions of the product
	 */
	public Collection<String> getFixVersions();

	/**
	 * Sets the product (fix) versions for this issue.
	 * Fix versions are versions where the resolution of this issue will be
	 * delivered to.
	 * @param versions - the product fix versions to set
	 */
	public void setFixVersions(Collection<String> versions);

	/**
	 * Adds the product (fix) versions for this issue.
	 * Fix versions are versions where the resolution of this issue will be
	 * delivered to.
	 * @param versions - the product fix versions to add
	 */
	public void addFixVersions(Collection<String> versions);

	/**
	 * Adds the product (fix) versions for this issue.
	 * Fix versions are versions where the resolution of this issue will be
	 * delivered to.
	 * @param versions - the product fix versions to add
	 */
	public void addFixVersions(String... versions);

	/**
	 * Removes the product (fix) versions for this issue.
	 * Fix versions are versions where the resolution of this issue will be
	 * delivered to.
	 * @param versions - the product fix versions to remove
	 */
	public void removeFixVersions(Collection<String> versions);

	/**
	 * Removes the product (fix) versions for this issue.
	 * Fix versions are versions where the resolution of this issue will be
	 * delivered to.
	 * @param versions - the product fix versions to remove
	 */
	public void removeFixVersions(String... versions);

	/**
	 * Removes the fix versions for this issue.
	 */
	public void removeAllFixVersions();

	/**
	 * Returns the number of fix versions for this issue.
	 * @return number of fix versions
	 */
	public int getFixVersionCount();

	/**
	 * Returns the (affected) versions of the project/product for this issue.
	 * Affected versions are versions where the issue was reported to.
	 * Usually this is set for issue reports only.
	 * @return the affected versions of the product
	 */
	public Collection<String> getAffectedVersions();

	/**
	 * Sets the product (affected) versions for this issue.
	 * Affected versions are versions where the issue was reported to.
	 * Usually this is set for issue reports only.
	 * @param versions - the product affected versions to set
	 */
	public void setAffectedVersions(Collection<String> versions);

	/**
	 * Adds the product (affected) versions for this issue.
	 * Affected versions are versions where the issue was reported to.
	 * Usually this is set for issue reports only.
	 * @param versions - the product affected versions to add
	 */
	public void addAffectedVersions(Collection<String> versions);

	/**
	 * Adds the product (affected) versions for this issue.
	 * Affected versions are versions where the issue was reported to.
	 * Usually this is set for issue reports only.
	 * @param versions - the product affected versions to add
	 */
	public void addAffectedVersions(String... versions);

	/**
	 * Removes the product (affected) versions for this issue.
	 * Affected versions are versions where the issue was reported to.
	 * Usually this is set for issue reports only.
	 * @param versions - the product affected versions to remove
	 */
	public void removeAffectedVersions(Collection<String> versions);

	/**
	 * Removes the product (affected) versions for this issue.
	 * Affected versions are versions where the issue was reported to.
	 * Usually this is set for issue reports only.
	 * @param versions - the product affected versions to remove
	 */
	public void removeAffectedVersions(String... versions);

	/**
	 * Removes the affected versions for this issue.
	 */
	public void removeAllAffectedVersions();

	/**
	 * Returns the number of affected versions for this issue.
	 * @return number of affected versions
	 */
	public int getAffectedVersionCount();

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
	 * Returns the operating system for this issue.
	 * @return the operating system
	 */
	public String getOpSys();

	/**
	 * Sets the operating system for this issue.
	 * @param opSys - the operating system to set
	 */
	public void setOpSys(String opSys);

	/**
	 * Returns the link for this issue.
	 * @return the link
	 */
	public String getLink();

	/**
	 * Sets the link for this issue.
	 * @param link - the link to set
	 */
	public void setLink(String link);

	/**
	 * Returns the status of this issue.
	 * @return the status
	 */
	public Status getStatus();

	/**
	 * Sets the status of this issue.
	 * @param status - the status to set
	 */
	public void setStatus(Status status);

	/**
	 * Returns the resolution status of this issue.
	 * @return the resolution status
	 */
	public Resolution getResolution();

	/**
	 * Sets the resolution status.
	 * @param resolution - the resolution to set
	 */
	public void setResolution(Resolution resolution);

	/**
	 * Returns the priority of this issue.
	 * @return the priority
	 */
	public Priority getPriority();

	/**
	 * Sets the priority for this issue.
	 * @param priority - the priority to set
	 */
	public void setPriority(Priority priority);

	/**
	 * Returns the severity for this issue.
	 * @return the severity
	 */
	public Severity getSeverity();

	/**
	 * Sets the severity for this issue.
	 * @param severity - the severity to set
	 */
	public void setSeverity(Severity severity);

	/**
	 * Returns the (planned) versions of the project/product for this issue.
	 * Planned versions are versions where the resolution of this issue 
	 * is planned to be delivered to.
	 * @return the planned versions of the product
	 */
	public Collection<String> getPlannedVersions();

	/**
	 * Sets the product (planned) versions for this issue.
	 * Planned versions are versions where the resolution of this issue 
	 * is planned to be delivered to.
	 * @param versions - the product planned versions to set
	 */
	public void setPlannedVersions(Collection<String> versions);

	/**
	 * Adds the product (planned) versions for this issue.
	 * Planned versions are versions where the resolution of this issue 
	 * is planned to be delivered to.
	 * @param versions - the product planned versions to add
	 */
	public void addPlannedVersions(Collection<String> versions);

	/**
	 * Adds the product (planned) versions for this issue.
	 * Planned versions are versions where the resolution of this issue 
	 * is planned to be delivered to.
	 * @param versions - the product planned versions to add
	 */
	public void addPlannedVersions(String... versions);

	/**
	 * Removes the product (planned) versions for this issue.
	 * Planned versions are versions where the resolution of this issue 
	 * is planned to be delivered to.
	 * @param versions - the product planned versions to remove
	 */
	public void removePlannedVersions(Collection<String> versions);

	/**
	 * Removes the product (planned) versions for this issue.
	 * Planned versions are versions where the resolution of this issue 
	 * is planned to be delivered to.
	 * @param versions - the product planned versions to remove
	 */
	public void removePlannedVersions(String... versions);

	/**
	 * Removes the planned versions for this issue.
	 */
	public void removeAllPlannedVersions();

	/**
	 * Returns the number of planned versions for this issue.
	 * @return number of planned versions
	 */
	public int getPlannedVersionCount();

	/**
	 * @return the everConfirmed
	 */
	public boolean isEverConfirmed();

	/**
	 * @param everConfirmed the everConfirmed to set
	 */
	public void setEverConfirmed(boolean everConfirmed);

	/**
	 * Returns the reporter's ID of this issue.
	 * @return the reporter ID
	 */
	public User getReporter();

	/**
	 * Sets the reporter's ID of this issue.
	 * @param reporter - the reporter ID to set
	 */
	public void setReporter(User reporter);

	/**
	 * Returns the assignee's ID of this issue.
	 * @return the assignee ID
	 */
	public User getAssignee();

	/**
	 * Sets the assignee's ID for this issue.
	 * @param assignee - the assignee ID to set
	 */
	public void setAssignee(User assignee);

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
	 * Returns the file location given in this issue.
	 * @return the file location
	 */
	public String getFileLocation();

	/**
	 * Sets the file location information.
	 * @param fileLocation - the file location to set
	 */
	public void setFileLocation(String fileLocation);

	/**
	 * Returns the CC e-mails of the project/product for this issue.
	 * @return the CC e-mails of the product
	 */
	public Collection<String> getCcs();

	/**
	 * Sets the CC e-mails for this issue.
	 * @param cc - the CC e-mails to set
	 */
	public void setCcs(Collection<String> cc);

	/**
	 * Adds the CC e-mails for this issue.
	 * @param cc - the CC e-mails to add
	 */
	public void addCcs(Collection<String> cc);

	/**
	 * Adds the CC e-mails for this issue.
	 * @param cc - the CC e-mails to add
	 */
	public void addCcs(String... cc);

	/**
	 * Removes the CC e-mails for this issue.
	 * @param cc - the CC e-mails to remove
	 */
	public void removeCcs(Collection<String> cc);

	/**
	 * Removes the CC e-mails for this issue.
	 * @param cc - the CC e-mails to remove
	 */
	public void removeCcs(String... cc);

	/**
	 * Removes the CC e-mails for this issue.
	 */
	public void removeAllCcs();

	/**
	 * Returns the number of CC e-mails for this issue.
	 * @return number of CC e-mails
	 */
	public int getCcCount();

	/**
	 * Returns the comments for this issue.
	 * @return the comments
	 */
	public Collection<Comment> getComments();

	/**
	 * Returns the comment with given id.
	 * @param id id of comment
	 * @return the comment or null if not found
	 */
	public Comment getComment(String id);
	
	/**
	 * Sets the comments for this issue.
	 * @param comments - the components to set
	 */
	public void setComments(Collection<Comment> comments);

	/**
	 * Adds the comments for this issue.
	 * @param comments - the components to add
	 */
	public void addComments(Collection<Comment> comments);

	/**
	 * Adds the comments for this issue.
	 * @param comments - the components to add
	 */
	public void addComments(Comment... comments);

	/**
	 * Removes the comments for this issue.
	 * @param comments - the comments to remove
	 */
	public void removeComments(Collection<Comment> comments);

	/**
	 * Removes the comments for this issue.
	 * @param comments - the comments to remove
	 */
	public void removeComments(Comment... comments);

	/**
	 * Removes the comments for this issue.
	 */
	public void removeAllComments();

	/**
	 * Returns the number of comments for this issue.
	 * @return number of comments
	 */
	public int getCommentCount();

	/**
	 * @return the blocked
	 */
	public long getBlocked();

	/**
	 * @param blocked the blocked to set
	 */
	public void setBlocked(long blocked);

	/**
	 * Returns the attachments for this issue.
	 * @return the attachments
	 */
	public Collection<Attachment> getAttachments();

	/**
	 * Returns the attachment with given id.
	 * @param id id of attachment
	 * @return the attachment or null if not found
	 */
	public Attachment getAttachment(String id);

	/**
	 * Sets the attachments for this issue.
	 * @param attachments - the attachments to set
	 */
	public void setAttachments(Collection<Attachment> attachments);

	/**
	 * Adds the attachments for this issue.
	 * @param attachments - the attachments to add
	 */
	public void addAttachments(Collection<Attachment> attachments);

	/**
	 * Adds the attachments for this issue.
	 * @param attachments - the attachments to add
	 */
	public void addAttachments(Attachment... attachments);

	/**
	 * Removes the attachments for this issue.
	 * @param attachments - the attachments to remove
	 */
	public void removeAttachments(Collection<Attachment> attachments);

	/**
	 * Removes the attachments for this issue.
	 * @param attachments - the attachments to remove
	 */
	public void removeAttachments(Attachment... attachments);

	/**
	 * Removes the attachments for this issue.
	 */
	public void removeAllAttachments();

	/**
	 * Returns the number of attachments for this issue.
	 * @return number of attachments
	 */
	public int getAttachmentCount();

	/**
	 * Adds a custom field value.
	 * This is a placeholder for customized fields in issuezilla. A field
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
	 * Tells whether this issue can be regarded as closed.
	 * @return true if issue is closed.
	 */
	public boolean isClosed();

	/**
	 * Tells whether this issue can be regarded as progressing.
	 * @return true if issue is in progress.
	 */
	public boolean isInProgress();

	/**
	 * Tells whether this issue can be regarded as resolved.
	 * @return true if issue is resolved.
	 */
	public boolean isResolved();

	/**
	 * Tells whether this issue can be regarded as cancelled.
	 * @return true if issue is cancelled.
	 */
	public boolean isCancelled();

	/**
	 * Tells whether this issue is a duplicate.
	 * @return true if issue is duplicate.
	 */
	public boolean isDuplicate();

	/**
	 * Tells whether a issue must be regarded as still open.
	 * @return true if issue is open
	 */
	public boolean isOpen();

	/**
	 * Returns the alias name of the issue.
	 * @return alias name of issue
	 */
	public String getAlias();

	/**
	 * Sets the alias name of this issue.
	 * @param alias - the new alias
	 */
	public void setAlias(String alias);

	/**
	 * Returns the whiteboard status.
	 * @return whiteboard status
	 */
	public String getWhiteboard();

	/**
	 * Sets the whiteboard status for this issue.
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

	/**
	 * Returns the links for this issue.
	 * @return the links
	 */
	public Collection<IssueLink> getLinks();

	/**
	 * Sets the links for this issue.
	 * @param links - the links to set
	 */
	public void setLinks(Collection<IssueLink> links);

	/**
	 * Adds the links for this issue.
	 * @param links - the links to add
	 */
	public void addLinks(Collection<IssueLink> links);

	/**
	 * Adds the links for this issue.
	 * @param links - the links to add
	 */
	public void addLinks(IssueLink... links);

	/**
	 * Removes the links for this issue.
	 * @param links - the links to remove
	 */
	public void removeLinks(Collection<IssueLink> links);

	/**
	 * Removes the links for this issue.
	 * @param links - the links to remove
	 */
	public void removeLinks(IssueLink... links);

	/**
	 * Removes the links for this issue.
	 */
	public void removeAllLinks();

	/**
	 * Returns the number of links for this issue.
	 * @return number of links
	 */
	public int getLinkCount();

	/**
	 * Returns the children for this issue.
	 * @return the children
	 */
	public Collection<Issue> getChildren();

	/**
	 * Sets the children for this issue.
	 * @param children - the children to set
	 */
	public void setChildren(Collection<Issue> children);

	/**
	 * Adds the children for this issue.
	 * @param children - the children to add
	 */
	public void addChildren(Collection<Issue> children);

	/**
	 * Adds the children for this issue.
	 * @param children - the attachments to add
	 */
	public void addChildren(Issue... children);

	/**
	 * Removes the children for this issue.
	 * @param children - the attachments to remove
	 */
	public void removeChildren(Collection<Issue> children);

	/**
	 * Removes the children for this issue.
	 * @param children - the children to remove
	 */
	public void removeChildren(Issue... children);

	/**
	 * Removes the children for this issue.
	 */
	public void removeAllChildren();

	/**
	 * Returns the number of children for this issue.
	 * @return number of children
	 */
	public int getChildCount();


}
