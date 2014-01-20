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

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import rs.baselib.util.CommonUtils;
import b4j.util.LazyRetriever;

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
	public static final SimpleDateFormat DATETIME_WITH_SEC_TZ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
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

	/** Key for {@link LazyRetriever} object */
	public static final String LAZY_RETRIEVER = "lazyRetriever";
	
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

	private String serverVersion;
	private String serverUri;
	private String uri;
	private String id;
	private String link;
	private String parentId;
	private Date creationTimestamp;
	private String summary;
	private String description;
	private Date updateTimestamp;
	//private boolean reporterAccessible;
	//private boolean cclistAccessible;
	private IssueType type;
	private Classification classification;
	private Project project;
	private List<Component> components;
	private List<Version> affectedVersions;
	private List<Version> plannedVersions;
	private List<Version> fixVersions;
	//private String repPlatform;
	//private String opSys;
	private Status status;
	private Resolution resolution;
	private Priority priority;
	private Severity severity;
	//private boolean everConfirmed;
	private User reporter;
	private User assignee;
	//private String qaContact;
	private List<Comment> comments;
	//private String fileLocation;
	//private List<String> cc;
	private List<Attachment> attachments;
	//private long blocked;
	private Map<String,Object> customFields;
	//private String alias;
	//private String whiteboard;
	//private double estimatedTime;
	//private double remainingTime;
	//private double actualTime;
	//private Date deadline;
	private List<IssueLink> links;
	private List<Issue> children;

	/**
	 * Default Constructor.
	 */
	public DefaultIssue() {
		affectedVersions = new ArrayList<Version>();
		plannedVersions = new ArrayList<Version>();
		fixVersions = new ArrayList<Version>();
		components = new ArrayList<Component>();
		comments = new ArrayList<Comment>();
		//cc = new ArrayList<String>();
		attachments = new ArrayList<Attachment>();
		customFields = new HashMap<String, Object>();
		creationTimestamp = new Date(0);
		updateTimestamp = new Date(0);
		//deadline = new Date(0);
		links = new ArrayList<IssueLink>();
		children = new ArrayList<Issue>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getServerVersion() {
		return serverVersion;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getServerUri() {
		return serverUri;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setServerUri(String serverUri) {
		this.serverUri = serverUri;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUri() {
		return uri;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUri(String uri) {
		this.uri = uri;
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
	 * {@inheritDoc}
	 */
	@Override
	public String getParentId() {
		return parentId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCreationTimestamp(Date creationTimestamp) {
		if (creationTimestamp != null)
			this.creationTimestamp.setTime(creationTimestamp.getTime());
		else
			this.creationTimestamp.setTime(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSummary() {
		return summary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpdateTimestamp(Date updateTimestamp) {
		if (updateTimestamp != null)
			this.updateTimestamp.setTime(updateTimestamp.getTime());
		else
			this.updateTimestamp.setTime(getCreationTimestamp().getTime());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IssueType getType() {
		type = check(type, "issueType");
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setType(IssueType type) {
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Classification getClassification() {
		classification = check(classification, "classification");
		return classification;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProject() {
		project = check(project, "project");
		return project;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Component> getComponents() {
		check(components, "component");
		return Collections.unmodifiableList(components);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setComponents(Collection<Component> components) {
		removeAllComponents();
		addComponents(components);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponents(Collection<Component> components) {
		if (components != null) this.components.addAll(components);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComponents(Component... components) {
		for (Component item : components) {
			this.components.add(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComponents(Collection<Component> components) {
		if (components != null) this.components.removeAll(components);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComponents(Component... components) {
		for (Component item : components) {
			this.components.remove(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllComponents() {
		components.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getComponentCount() {
		return getComponents().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Version> getAffectedVersions() {
		check(affectedVersions, "affectedVersion", "version");
		return Collections.unmodifiableList(affectedVersions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAffectedVersions(Collection<Version> versions) {
		removeAllAffectedVersions();
		addAffectedVersions(versions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAffectedVersions(Collection<Version> versions) {
		if (versions != null) affectedVersions.addAll(versions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAffectedVersions(Version... versions) {
		for (Version item : versions) {
			affectedVersions.add(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAffectedVersions(Collection<Version> versions) {
		if (versions != null) affectedVersions.removeAll(versions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAffectedVersions(Version... versions) {
		for (Version item : versions) {
			affectedVersions.remove(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllAffectedVersions() {
		affectedVersions.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAffectedVersionCount() {
		return getAffectedVersions().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Version> getPlannedVersions() {
		check(plannedVersions, "plannedVersion", "version");
		return Collections.unmodifiableList(plannedVersions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPlannedVersions(Collection<Version> versions) {
		removeAllPlannedVersions();
		addPlannedVersions(versions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPlannedVersions(Collection<Version> versions) {
		if (versions != null) plannedVersions.addAll(versions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPlannedVersions(Version... versions) {
		for (Version item : versions) {
			plannedVersions.add(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePlannedVersions(Collection<Version> versions) {
		if (versions != null) plannedVersions.removeAll(versions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePlannedVersions(Version... versions) {
		for (Version item : versions) {
			plannedVersions.remove(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllPlannedVersions() {
		plannedVersions.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPlannedVersionCount() {
		return getPlannedVersions().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Version> getFixVersions() {
		check(fixVersions, "fixVersion", "version");
		return Collections.unmodifiableList(fixVersions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFixVersions(Collection<Version> versions) {
		removeAllFixVersions();
		addFixVersions(versions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFixVersions(Collection<Version> versions) {
		if (versions != null) fixVersions.addAll(versions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addFixVersions(Version... versions) {
		for (Version item : versions) {
			fixVersions.add(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFixVersions(Collection<Version> versions) {
		if (versions != null) fixVersions.removeAll(versions);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeFixVersions(Version... versions) {
		for (Version item : versions) {
			fixVersions.remove(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllFixVersions() {
		fixVersions.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFixVersionCount() {
		return getFixVersions().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status getStatus() {
		status = check(status, "status");
		return status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Resolution getResolution() {
		resolution = check(resolution, "resolution");
		return resolution;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setResolution(Resolution resolution) {
		this.resolution = resolution;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Priority getPriority() {
		priority = check(priority, "priority");
		return priority;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Severity getSeverity() {
		severity = check(severity, "severity");
		return severity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getReporter() {
		reporter = check(reporter, "reporter", "user");
		return reporter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setReporter(User reporter) {
		this.reporter = reporter;
	}

	/**
	 * {@inheritDoc}
	 */
	public User getAssignee() {
		assignee = check(assignee, "assignee", "user");
		return assignee;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAssignee(User assignee) {
		this.assignee = assignee;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attachment getAttachment(String id) {
		check(attachments, "attachment");
		for (Attachment a : attachments) {
			if (CommonUtils.equals(a.getId(), id)) return a;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(String key, Object value) {
		customFields.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(String key) {
		return customFields.get(key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<String> getCustomFieldNames() {
		return customFields.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCustomFieldCount() {
		return customFields.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isClosed() {
		Status s = getStatus();
		if (s == null) return false;
		return s.isClosed(); //s.equalsIgnoreCase("CLOSED");
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isResolved() {
		Status s = getStatus();
		if (s == null) return false;
		boolean rc = s.isResolved(); //s.equalsIgnoreCase("RESOLVED") || s.equalsIgnoreCase("VERIFIED") || s.equalsIgnoreCase("CLOSED");
		if (!rc) {
			Resolution r = getResolution();
			if (r != null) rc = r.isResolved(); //s.toUpperCase().startsWith("CANCEL");
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isCancelled() {
		Status s = getStatus();
		if (s == null) return false;
		boolean rc = s.isCancelled(); //s.toUpperCase().startsWith("CANCEL");
		if (!rc) {
			Resolution r = getResolution();
			if (r != null) rc = r.isCancelled(); //s.toUpperCase().startsWith("CANCEL");
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDuplicate() {
		Status s = getStatus();
		if (s == null) return false;
		boolean rc = s.isDuplicate(); //s.toUpperCase().startsWith("DUPLICATE");
		if (!rc) {
			Resolution r = getResolution();
			if (r!= null) rc = r.isDuplicate(); //s.toUpperCase().startsWith("DUPLICATE");
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOpen() {
		Status s = getStatus();
		if (s == null) return true;
		return s.isOpen(); 
		//				s.toUpperCase().indexOf("OPEN") >= 0 || 
		//				s.equalsIgnoreCase("NEW") || s.equalsIgnoreCase("CREATED") ||
		//				s.equalsIgnoreCase("UNCONFIRMED") || s.equalsIgnoreCase("ASSIGNED");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInProgress() {
		return !isOpen() && !isClosed();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLink() {
		return link;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Comment> getComments() {
		check(comments, "comment");
		return Collections.unmodifiableList(comments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Comment getComment(String id) {
		for (Comment c : getComments()) {
			if (CommonUtils.equals(c.getId(), id)) return c;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setComments(Collection<Comment> comments) {
		removeAllComments();
		addComments(comments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComments(Collection<Comment> comments) {
		if (comments != null) this.comments.addAll(comments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addComments(Comment... comments) {
		for (Comment item : comments) {
			this.comments.add(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComments(Collection<Comment> comments) {
		if (comments != null) this.comments.removeAll(comments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeComments(Comment... comments) {
		for (Comment item : comments) {
			this.comments.remove(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllComments() {
		comments.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getCommentCount() {
		return getComments().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Attachment> getAttachments() {
		check(attachments, "attachment");
		return Collections.unmodifiableList(attachments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttachments(Collection<Attachment> attachments) {
		removeAllAttachments();
		addAttachments(attachments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAttachments(Collection<Attachment> attachments) {
		if (attachments != null) this.attachments.addAll(attachments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAttachments(Attachment... attachments) {
		for (Attachment item : attachments) {
			this.attachments.add(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttachments(Collection<Attachment> attachments) {
		if (attachments != null) this.attachments.removeAll(attachments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttachments(Attachment... attachments) {
		for (Attachment item : attachments) {
			this.attachments.remove(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllAttachments() {
		attachments.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAttachmentCount() {
		return getAttachments().size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Issue> getChildren() {
		return Collections.unmodifiableList(children);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChildren(Collection<Issue> children) {
		removeAllChildren();
		addChildren(children);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addChildren(Collection<Issue> children) {
		if (children != null) this.children.addAll(children);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addChildren(Issue... children) {
		for (Issue item : children) {
			this.children.add(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeChildren(Collection<Issue> children) {
		if (children != null) this.children.removeAll(children);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeChildren(Issue... children) {
		for (Issue item : children) {
			this.children.remove(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllChildren() {
		children.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildCount() {
		return children.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<IssueLink> getLinks() {
		return Collections.unmodifiableList(links);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLinks(Collection<IssueLink> links) {
		removeAllLinks();
		addLinks(links);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLinks(Collection<IssueLink> links) {
		if (links != null) this.links.addAll(links);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addLinks(IssueLink... links) {
		for (IssueLink item : links) {
			this.links.add(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLinks(Collection<IssueLink> links) {
		if (links != null) this.links.removeAll(links);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeLinks(IssueLink... links) {
		for (IssueLink item : links) {
			this.links.remove(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAllLinks() {
		links.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getLinkCount() {
		return links.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getClass().getName()+"[id="+getId()+";summary="+getSummary()+";status="+getStatus()+"]";
	}

	/**
	 * Checks the lazy retrieval of the given value.
	 * <p>The method checks whether <code>originalValue</code> is already set and returns this. Otherwise
	 * it will check for a {@link LazyRetriever} instance and the <code>${propertyPrefix}_name</code> and
	 * <code>${propertyPrefix}_id</code> custom fields. If either of them is set, the {@link LazyRetriever}
	 * is asked for an actual value to be returned.</p>
	 * @param originalValue the value currently stored
	 * @param propertyPrefix the property to be checked
	 * @return the value (either originalValue or the lazily retrieved value or <code>null</code>)
	 */
	protected <T> T check(T originalValue, String propertyPrefix) {
		return check(originalValue, propertyPrefix, propertyPrefix);
	}
	
	/**
	 * Checks the lazy retrieval of the given value.
	 * <p>The method checks whether <code>originalValue</code> is already set and returns this. Otherwise
	 * it will check for a {@link LazyRetriever} instance and the <code>${propertyPrefix}_name</code> and
	 * <code>${propertyPrefix}_id</code> custom fields. If either of them is set, the {@link LazyRetriever}
	 * is asked for an actual value to be returned.</p>
	 * @param originalValue the value currently stored
	 * @param propertyPrefix the property to be checked
	 * @param typeProperty name of property at {@link LazyRetriever}
	 * @return the value (either originalValue or the lazily retrieved value or <code>null</code>)
	 */
	protected <T> T check(T originalValue, String propertyPrefix, String typeProperty) {
		if (originalValue != null) return originalValue;
		LazyRetriever retriever = (LazyRetriever)get(LAZY_RETRIEVER);
		if (retriever == null) return null;
		String name = (String)get(propertyPrefix+"_name");
		if (name != null) {
			T obj = retrieve(retriever, typeProperty, name);
			if (obj != null) return obj;
		}
		Long id = (Long)get(propertyPrefix+"_id");
		if (id != null) {
			T obj = retrieve(retriever, typeProperty, id);
			return obj;
		}
		return null;
	}

	/**
	 * Checks the lazy retrieval of the given collection values.
	 * <p>The method checks for a {@link LazyRetriever} instance and the <code>${propertyPrefix}_name</code> and
	 * <code>${propertyPrefix}_id</code> custom fields (can be collections or single values. If either of them is set, 
	 * the {@link LazyRetriever} is asked for actual values to be returned.</p>
	 * @param collection the value currently stored and enhanced with found values.
	 * @param propertyPrefix the property to be checked
	 */
	protected <T> void check(Collection<T> collection, String propertyPrefix) {
		check(collection, propertyPrefix, propertyPrefix);
	}
	
	/**
	 * Checks the lazy retrieval of the given collection values.
	 * <p>The method checks for a {@link LazyRetriever} instance and the <code>${propertyPrefix}_name</code> and
	 * <code>${propertyPrefix}_id</code> custom fields (can be collections or single values. If either of them is set, 
	 * the {@link LazyRetriever} is asked for actual values to be returned.</p>
	 * @param collection the value currently stored and enhanced with found values.
	 * @param propertyPrefix the property to be checked
	 * @param typeProperty name of property at {@link LazyRetriever}
	 */
	@SuppressWarnings("unchecked")
	protected <T> void check(Collection<T> collection, String propertyPrefix, String typeProperty) {
		LazyRetriever retriever = (LazyRetriever)get("lazyRetriever");
		if (retriever != null) {
			Object o = get(propertyPrefix+"_name");
			Collection<String> names = null;
			if (!(o instanceof Collection)) {
				names = new ArrayList<String>();
				if (o instanceof String) names.add((String)o);
			} else {
				names = (Collection<String>)o;
			}
			for (String name : names) {
				T obj = retrieve(retriever, typeProperty, name);
				if (obj != null) collection.add(obj);
			}

			o = get(propertyPrefix+"_id");
			Collection<Long> ids = null;
			if (!(o instanceof Collection)) {
				ids = new ArrayList<Long>();
				if (o instanceof Long) ids.add((Long)o);
			} else {
				ids = (Collection<Long>)o;
			}
			for (Long id : ids) {
				T obj = retrieve(retriever, typeProperty, id);
				if (obj != null) collection.add(obj);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private <T> T retrieve(LazyRetriever retriever, String property, String name) {
		try {
			String methodName = "get"+StringUtils.capitalize(property);
			Method m = retriever.getClass().getMethod(methodName, String.class);
			return (T)m.invoke(retriever, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T retrieve(LazyRetriever retriever, String property, Long id) {
		try {
			String methodName = "get"+StringUtils.capitalize(property);
			Method m = retriever.getClass().getMethod(methodName, Long.TYPE);
			return (T)m.invoke(retriever, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
