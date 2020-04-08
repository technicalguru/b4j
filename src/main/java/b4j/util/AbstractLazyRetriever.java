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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import rs.baselib.lang.LangUtils;
import b4j.core.Attachment;
import b4j.core.Classification;
import b4j.core.Comment;
import b4j.core.Component;
import b4j.core.IssueType;
import b4j.core.Priority;
import b4j.core.Project;
import b4j.core.Resolution;
import b4j.core.Severity;
import b4j.core.Status;
import b4j.core.User;
import b4j.core.Version;

/**
 * Retrieves registered objects lazily (abstract implementation).
 * @author ralph
 *
 */
public abstract class AbstractLazyRetriever implements LazyRetriever {

	private Set<String> classificationNameQueue;
	private Set<Long> classificationIdQueue;
	private Set<Classification> classifications;
	private Set<String> projectNameQueue;
	private Set<Long> projectIdQueue;
	private Set<Project> projects;
	private Map<String,Set<String>> componentNameQueue;
	private Set<Component> components;
	private Set<String> userNameQueue;
	private Set<Long> userIdQueue;
	private Set<User> users;
	private Set<String> commentQueue;
	private Map<String,Set<Comment>> comments; 
	private Set<String> attachmentQueue;
	private Map<String,Set<Attachment>> attachments;
	private Set<String> priorityNameQueue;
	private Set<Priority> priorities;
	private Set<String> severityNameQueue;
	private Set<Severity> severities;
	private Set<String> resolutionNameQueue;
	private Set<Resolution> resolutions;
	private Set<String> statusNameQueue;
	private Set<Status> status;
	private Set<String> issueTypeNameQueue;
	private Set<IssueType> issueTypes;
	private Map<String,Set<String>> versionNameQueue;
	private Set<Version> versions;

	/**
	 * Constructor.
	 */
	public AbstractLazyRetriever() {
		classificationNameQueue = new HashSet<String>();
		classificationIdQueue = new HashSet<Long>();
		classifications = new HashSet<Classification>();
		projectNameQueue = new HashSet<String>();
		projectIdQueue = new HashSet<Long>();
		projects = new HashSet<Project>();
		componentNameQueue = new HashMap<String,Set<String>>();
		components = new HashSet<Component>();
		userNameQueue = new HashSet<String>();
		userIdQueue = new HashSet<Long>();
		users = new HashSet<User>();
		commentQueue = new HashSet<String>();
		comments = new HashMap<String,Set<Comment>>();
		attachmentQueue = new HashSet<String>();
		attachments = new HashMap<String,Set<Attachment>>();
		priorityNameQueue = new HashSet<String>();
		priorities = new HashSet<Priority>();
		severityNameQueue = new HashSet<String>();
		severities = new HashSet<Severity>();
		resolutionNameQueue = new HashSet<String>();
		resolutions = new HashSet<Resolution>();
		statusNameQueue = new HashSet<String>();
		status = new HashSet<Status>();
		issueTypeNameQueue = new HashSet<String>();
		issueTypes = new HashSet<IssueType>();
		versionNameQueue = new HashMap<String,Set<String>>();
		versions = new HashSet<Version>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerClassification(String name) {
		if ((name != null) && (searchClassification(name) == null)) {
			classificationNameQueue.add(name);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerClassification(long id) {
		if (searchClassification(id) == null) {
			classificationIdQueue.add(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerClassification(Classification classification) {
		if (!classifications.contains(classification)) classifications.add(classification);
		classificationNameQueue.remove(classification.getName());
		classificationIdQueue.remove(LangUtils.getLong(classification.getId()));
	}

	/** 
	 * Searches the classification whether it is already loaded.
	 * @param name - name of classification
	 * @return the classification found or {@code null}
	 */
	protected Classification searchClassification(String name) {
		for (Classification o : classifications) {
			if (name.equals(o.getName())) return o;
		}
		return null;
	}

	/** 
	 * Searches the classification whether it is already loaded
	 * @param name - ID of classification
	 * @return the classification found or {@code null}
	 */
	protected Classification searchClassification(long id) {
		for (Classification o : classifications) {
			String oid = o.getId();
			if ((oid != null) && oid.equals(Long.toString(id))) return o;
		}
		return null;
	}

	/** 
	 * Returns the classification names.
	 * @return list of classification names
	 */
	protected Collection<String> getClassificationNames() {
		return classificationNameQueue;
	}

	/** 
	 * Returns the classification IDs.
	 * @return list of classification IDs
	 */
	protected Collection<Long> getClassificationIds() {
		return classificationIdQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerProject(long id) {
		if (searchProject(id) == null) {
			projectIdQueue.add(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerProject(String name) {
		if ((name != null) && (searchProject(name) == null)) {
			projectNameQueue.add(name);
		}
	}

	/** 
	 * Searches a project by ID.
	 * @param id - ID of project
	 * @return project if found or {@code null}
	 */
	protected Project searchProject(long id) {
		for (Project o : projects) {
			String oid = o.getId();
			if ((oid != null) && oid.equals(Long.toString(id))) return o;
		}
		return null;
	}

	/** 
	 * Searches a project by name.
	 * @param name - name of project
	 * @return project if found or {@code null}
	 */
	protected Project searchProject(String name) {
		for (Project o : projects) {
			String oid = o.getName();
			if ((oid != null) && oid.equals(name)) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerProject(Project project) {
		if (!projects.contains(project)) projects.add(project);
		projectIdQueue.remove(LangUtils.getLong(project.getId()));
		projectNameQueue.remove(project.getName());
		for (Component c : project.getComponents()) {
			registerComponent(c);
		}
	}

	/** 
	 * Returns the project IDs.
	 * @return the project IDs
	 */
	protected Collection<Long> getProjectIds() {
		return projectIdQueue;
	}

	/** 
	 * Returns the project names.
	 * @return names of projects
	 */
	protected Collection<String> getProjectNames() {
		return projectNameQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerComponent(String projectName, String name) {
		if ((name != null) && (projectName != null) && (searchComponent(projectName, name) == null)) {
			Set<String> names = componentNameQueue.get(projectName);
			if (names == null) {
				names = new HashSet<String>();
				componentNameQueue.put(projectName, names);
			}
			names.add(name);
		}
	}

	/** 
	 * Searches the component whether it is already loaded.
	 * @param projectName - name of project for component
	 * @param name - name of component
	 * @return the component if loaded, {@code null} otherwise
	 */
	protected Component searchComponent(String projectName, String name) {
		for (Component o : components) {
			if (name.equals(o.getName()) && projectName.equals(o.getProject().getName())) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerComponent(Component component) {
		if (!components.contains(component)) components.add(component);
		String projectName = component.getProject().getName();
		Set<String> names = componentNameQueue.get(projectName);
		if (names != null) {
			names.remove(component.getName());
			if (names.size() == 0) {
				componentNameQueue.remove(projectName);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerUser(String name) {
		if ((name != null) && (searchUser(name) == null)) {
			userNameQueue.add(name);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerUser(long id) {
		if (searchUser(id) == null) {
			userIdQueue.add(id);
		}
	}

	/** 
	 * Returns the user names.
	 * @return list of user names
	 */
	protected Collection<String> getUserNames() {
		return userNameQueue;
	}

	/** 
	 * Returns the user IDs.
	 * @return list of user IDs
	 */
	protected Collection<Long> getUserIds() {
		return userIdQueue;
	}

	/** 
	 * Searches a user by name whether he/she is already loaded.
	 * @param name - name of user
	 * @return user if found or {@code null}
	 */
	protected User searchUser(String name) {
		for (User o : users) {
			if (name.equals(o.getName())) return o;
		}
		return null;
	}

	/** 
	 * Searches a user by ID  whether he/she is already loaded.
	 * @param id - ID of user
	 * @return user if found or {@code null}
	 */
	protected User searchUser(long id) {
		for (User o : users) {
			String oid = o.getId();
			if ((oid != null) && oid.equals(Long.toString(id))) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerUser(User user) {
		if (!users.contains(user)) users.add(user);
		userNameQueue.remove(user.getName());
		userIdQueue.remove(LangUtils.getLong(user.getId()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerComment(String issueId) {
		if ((searchComment(issueId) == null) && !commentQueue.contains(issueId)) {
			commentQueue.add(issueId);
		}
	}

	/** 
	 * Searches comments whether they are already loaded.
	 * @param issueId - ID of issue for comments
	 * @return list of collections when loaded, {@code null} otherwise.
	 */
	protected Collection<Comment> searchComment(String issueId) {
		if (comments.containsKey(issueId)) return comments.get(issueId);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerComments(String issueId, Set<Comment> comments) {
		commentQueue.remove(issueId);
		this.comments.put(issueId, comments);
	}

	/** 
	 * Returns the comments.
	 * @return list of comments loaded.
	 */
	protected Collection<String> getCommentIssues() {
		return commentQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerAttachment(String issueId) {
		if (searchAttachment(issueId) == null) {
			attachmentQueue.add(issueId);
		}
	}

	/** 
	 * Searches the attachments whether they are already loaded.
	 * @param issueID - ID of issue
	 * @return the attachments if loaded, {@code null} otherwise
	 */
	protected Set<Attachment> searchAttachment(String issueId) {
		return attachments.get(issueId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerAttachments(String issueId, Set<Attachment> attachments) {
		attachmentQueue.remove(issueId);
		this.attachments.put(issueId, attachments);
	}

	/** Returns the attachments */
	protected Collection<String> getAttachmentIssues() {
		return attachmentQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerPriority(String name) {
		if ((name != null) && (searchPriority(name) == null)) {
			priorityNameQueue.add(name);
		}
	}

	/** 
	 * Searches the priority whether it is already loaded.
	 * @param name - name of priority
	 * @return the priority if loaded, {@code null} otherwise
	 */
	protected Priority searchPriority(String name) {
		for (Priority o : priorities) {
			if (name.equals(o.getName())) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerPriority(Priority priority) {
		if (!priorities.contains(priority)) priorities.add(priority);
		priorityNameQueue.remove(priority.getName());
	}

	/** Returns the priority names.
	 * @return list of priority names
	 */
	protected Collection<String> getPriorityNames() {
		return priorityNameQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerSeverity(String name) {
		if ((name != null) && (searchSeverity(name) == null)) {
			severityNameQueue.add(name);
		}
	}

	/** 
	 * Searches the severity whether it is already loaded.
	 * @param name - name of severity
	 * @return the severity if loaded, {@code null} otherwise
	 */
	protected Severity searchSeverity(String name) {
		for (Severity o : severities) {
			if (name.equals(o.getName())) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerSeverity(Severity severity) {
		if (!severities.contains(severity)) severities.add(severity);
		severityNameQueue.remove(severity.getName());
	}

	/** 
	 * Returns the severity names.
	 * @return the {@link Severity} names.
	 */
	protected Collection<String> getSeverityNames() {
		return severityNameQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStatus(String name) {
		if ((name != null) && (searchStatus(name) == null)) {
			statusNameQueue.add(name);
		}
	}

	/** 
	 * Searches the status whether it is already loaded.
	 * @param name - status of priority
	 * @return the status if loaded, {@code null} otherwise
	 */
	protected Status searchStatus(String name) {
		for (Status o : status) {
			if (name.equals(o.getName())) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStatus(Status status) {
		if (!this.status.contains(status)) this.status.add(status);
		statusNameQueue.remove(status.getName());
	}

	/** 
	 * Returns the status names.
	 * @return list of status names.
	 */
	protected Collection<String> getStatusNames() {
		return statusNameQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerResolution(String name) {
		if ((name != null) && (searchResolution(name) == null)) {
			resolutionNameQueue.add(name);
		}
	}

	/** 
	 * Searches the resolution whether it is already loaded.
	 * @param name - name of resolution
	 * @return the resolution if loaded, {@code null} otherwise
	 */
	protected Resolution searchResolution(String name) {
		for (Resolution o : resolutions) {
			if (name.equals(o.getName())) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerResolution(Resolution resolution) {
		if (!resolutions.contains(resolution)) resolutions.add(resolution);
		resolutionNameQueue.remove(resolution.getName());
	}

	/** 
	 * Returns the resolution names.
	 * @return list of resolution names
	 */
	protected Collection<String> getResolutionNames() {
		return resolutionNameQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerIssueType(String name) {
		if ((name != null) && (searchIssueType(name) == null)) {
			issueTypeNameQueue.add(name);
		}
	}

	/** 
	 * Searches the issue type whether it is already loaded.
	 * @param name - name of issue type
	 * @return the issue type if loaded, {@code null} otherwise
	 */
	protected IssueType searchIssueType(String name) {
		for (IssueType o : issueTypes) {
			if (name.equals(o.getName())) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerIssueType(IssueType issueType) {
		if (!issueTypes.contains(issueType)) issueTypes.add(issueType);
		issueTypeNameQueue.remove(issueType.getName());
	}

	/** 
	 * Returns the issue type names.
	 * @return list of issue type names
	 */
	protected Collection<String> getIssueTypeNames() {
		return issueTypeNameQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerVersion(String projectName, String name) {
		if ((name != null) && (projectName != null) && (searchVersion(projectName, name) == null)) {
			Set<String> names = versionNameQueue.get(projectName);
			if (names == null) {
				names = new HashSet<String>();
				versionNameQueue.put(projectName, names);
			}
			names.add(name);
		}
	}

	/** 
	 * Searches the version whether it is already loaded.
	 * @param projectName - name of project for version
	 * @param name - name of version
	 * @return the version if loaded, {@code null} otherwise
	 */
	protected Version searchVersion(String projectName, String name) {
		for (Version o : versions) {
			if (name.equals(o.getName()) && projectName.equals(o.getProject().getName())) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerVersion(Version version) {
		if (!versions.contains(version)) versions.add(version);
		String projectName = version.getProject().getName();
		Set<String> names = versionNameQueue.get(projectName);
		if (names != null) {
			names.remove(version.getName());
			if (names.size() == 0) {
				versionNameQueue.remove(projectName);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Classification getClassification(String name) {
		Classification rc = searchClassification(name);
		if (rc == null) {
			try {
				loadClassifications();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load classifications", e);
			}
			rc = searchClassification(name);
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Classification getClassification(long id) {
		Classification rc = searchClassification(id);
		if (rc == null) {
			try {
				loadClassifications();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load classifications", e);
			}
			rc = searchClassification(id);
		}
		return rc;
	}

	/** 
	 * Loads the classifications.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadClassifications() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProject(String name) {
		Project rc = searchProject(name);
		if (rc == null) {
			try {
				loadProjects();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load classifications", e);
			}
			rc = searchProject(name);
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProject(long id) {
		Project rc = searchProject(id);
		if (rc == null) {
			try {
				loadProjects();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load projects", e);
			}
			rc = searchProject(id);
		}
		return rc;
	}

	/** 
	 * Loads the projects.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadProjects() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getComponent(String projectName, String name) {
		Component rc = searchComponent(projectName, name);
		if (rc == null) {
			try {
				loadComponents();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load components", e);
			}
			rc = searchComponent(projectName, name);
		}
		return rc;
	}

	/** 
	 * Loads the components.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadComponents() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUser(String name) {
		User rc = searchUser(name);
		if (rc == null) {
			try {
				loadUsers();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load users", e);
			}
			rc = searchUser(name);
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUser(long id) {
		User rc = searchUser(id);
		if (rc == null) {
			try {
				loadUsers();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load users", e);
			}
			rc = searchUser(id);
		}
		return rc;
	}

	/** 
	 * Loads the users.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadUsers() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Comment> getComments(String issue) {
		Collection<Comment> rc = searchComment(issue);
		if (rc == null) {
			try {
				loadComments();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load comments", e);
			}
			rc = searchComment(issue);
		}
		return rc;
	}

	/** 
	 * Loads the comments.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadComments() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Attachment> getAttachments(String issueId) {
		Collection<Attachment> rc = searchAttachment(issueId);
		if (rc == null) {
			try {
				loadAttachments();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load attachments", e);
			}
			rc = searchAttachment(issueId);
		}
		if (rc == null) rc = Collections.emptySet();
		return rc;
	}

	/** 
	 * Loads the attachments.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadAttachments() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Priority getPriority(String name) {
		Priority rc = searchPriority(name);
		if (rc == null) {
			try {
				loadPriorities();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load priorities", e);
			}
			rc = searchPriority(name);
		}
		return rc;
	}

	/** 
	 * Loads the priorities.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadPriorities() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Severity getSeverity(String name) {
		Severity rc = searchSeverity(name);
		if (rc == null) {
			try {
				loadSeverities();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load severities", e);
			}
			rc = searchSeverity(name);
		}
		return rc;
	}

	/** 
	 * Loads the severities.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadSeverities() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Status getStatus(String name) {
		Status rc = searchStatus(name);
		if (rc == null) {
			try {
				loadStatus();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load status", e);
			}
			rc = searchStatus(name);
		}
		return rc;
	}

	/** 
	 * Loads the status.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadStatus() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Resolution getResolution(String name) {
		Resolution rc = searchResolution(name);
		if (rc == null) {
			try {
				loadResolutions();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load resolutions", e);
			}
			rc = searchResolution(name);
		}
		return rc;
	}

	/** 
	 * Loads the resolutions.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadResolutions() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IssueType getIssueType(String name) {
		IssueType rc = searchIssueType(name);
		if (rc == null) {
			try {
				loadIssueTypes();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load issue types", e);
			}
			rc = searchIssueType(name);
		}
		return rc;
	}

	/** 
	 * Loads the issue types.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadIssueTypes() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Version getVersion(String projectName, String name) {
		Version rc = searchVersion(projectName, name);
		if (rc == null) {
			try {
				loadVersions();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load versions", e);
			}
			rc = searchVersion(projectName, name);
		}
		return rc;
	}

	/** 
	 * Loads the versions.
	 * @throws Exception when an error occurs
	 */
	protected abstract void loadVersions() throws Exception;

}
