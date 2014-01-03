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

/**
 * Interface representing a issuezilla issue record.
 * Usually instances are delivered by a Session search query.
 * @author Ralph Schuster
 *
 */
public interface Issue {

	/** Custom field name for Bugzilla field reporter_accessible */
	public static final String REPORTER_ACCESSIBLE = "reporter_accessible";
	/** Custom field name for Bugzilla field cclist_accessible */
	public static final String CCLIST_ACCESSIBLE = "cclist_accessible";
	/** Custom field name for Bugzilla field rep_platform */
	public static final String REP_PLATFORM = "rep_platform";
	/** Custom field name for Bugzilla field op_sys */
	public static final String OP_SYS = "op_sys";
	/** Custom field name for Bugzilla field everconfirmed */
	public static final String CONFIRMED = "everconfirmed";
	/** Custom field name for Bugzilla field qa_contact */
	public static final String QA_CONTACT = "qa_contact";
	/** Custom field name for Bugzilla field bug_file_loc */
	public static final String BUG_FILE_LOCATION = "bug_file_loc";
	/** Custom field name for Bugzilla field cc */
	public static final String CC = "cc";
	/** Custom field name for Bugzilla field blocked */
	public static final String BLOCKED = "blocked";
	/** Custom field name for Bugzilla field alias */
	public static final String ALIAS = "alias";
	/** Custom field name for Bugzilla field whiteboard */
	public static final String WHITEBOARD = "whiteboard";
	/** Custom field name for Bugzilla field estimated_time */
	public static final String ESTIMATED_TIME = "estimated_time";
	/** Custom field name for Bugzilla field remaining_time */
	public static final String REMAINING_TIME = "remaining_time";
	/** Custom field name for Bugzilla field actual_time */
	public static final String ACTUAL_TIME = "actual_time";
	/** Custom field name for Bugzilla field deadline */
	public static final String DEADLINE = "deadline";
	
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
	 * Returns the classification for this issue.
	 * @return classification
	 */
	public Classification getClassification();

	/**
	 * Sets the classification for this issue.
	 * @param classification - the classification to set
	 */
	public void setClassification(Classification classification);

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
	 * Sets a custom field value.
	 * Setting a value to <code>null</code> effectively deletes the field.
	 * @param key - name of field
	 * @param value - value of field
	 */
	public void set(String key, Object value);

	/**
	 * Returns value of a field.
	 * @param key - name of field
	 * @return value or null if not set.
	 */
	public Object get(String key);

	/**
	 * Returns the names of all custom fields.
	 * @return iterator of customized field names
	 */
	public Iterable<String> getCustomFieldNames();

	/**
	 * Returns the number of custom fields.
	 * @return number customized field names
	 */
	public int getCustomFieldCount();

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
