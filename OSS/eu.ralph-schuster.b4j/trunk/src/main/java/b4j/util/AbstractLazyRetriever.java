/**
 * 
 */
package b4j.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

/**
 * Retrieves registered objects lazily (abstract implementation).
 * @author ralph
 *
 */
public abstract class AbstractLazyRetriever implements LazyRetriever {

	private Set<String> classificationNameQueue;
	private Set<Long> classificationIdQueue;
	private Set<Classification> classifications;
	private Set<Long> productIdQueue;
	private Set<Project> products;
	private Map<String,Set<String>> componentNameQueue;
	private Set<Component> components;
	private Set<String> userNameQueue;
	private Set<Long> userIdQueue;
	private Set<User> users;
	private Set<Long> commentIdQueue;
	private Set<Comment> comments;
	private Set<Long> attachmentIdQueue;
	private Set<Attachment> attachments;
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

	/**
	 * Constructor.
	 */
	public AbstractLazyRetriever() {
		classificationNameQueue = new HashSet<String>();
		classificationIdQueue = new HashSet<Long>();
		classifications = new HashSet<Classification>();
		productIdQueue = new HashSet<Long>();
		products = new HashSet<Project>();
		componentNameQueue = new HashMap<String,Set<String>>();
		components = new HashSet<Component>();
		userNameQueue = new HashSet<String>();
		userIdQueue = new HashSet<Long>();
		users = new HashSet<User>();
		commentIdQueue = new HashSet<Long>();
		comments = new HashSet<Comment>();
		attachmentIdQueue = new HashSet<Long>();
		attachments = new HashSet<Attachment>();
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
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerClassification(String name) {
		if (searchClassification(name) == null) {
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
		classificationIdQueue.remove(Long.getLong(classification.getId()));
	}

	/** Searches the classification whether it is already loaded */
	protected Classification searchClassification(String name) {
		for (Classification o : classifications) {
			if (name.equals(o.getName())) return o;
		}
		return null;
	}

	/** Searches the classification whether it is already loaded */
	protected Classification searchClassification(long id) {
		for (Classification o : classifications) {
			String oid = o.getId();
			if ((oid != null) && oid.equals(Long.toString(id))) return o;
		}
		return null;
	}

	/** Returns the classifications */
	protected Collection<String> getClassificationNames() {
		return classificationNameQueue;
	}

	/** Returns the classifications */
	protected Collection<Long> getClassificationIds() {
		return classificationIdQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerProduct(long id) {
		if (searchProduct(id) == null) {
			productIdQueue.add(id);
		}
	}

	/** Searches the product whether it is already loaded */
	protected Project searchProduct(long id) {
		for (Project o : products) {
			String oid = o.getId();
			if ((oid != null) && oid.equals(Long.toString(id))) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerProduct(Project product) {
		if (!products.contains(product)) products.add(product);
		productIdQueue.remove(Long.getLong(product.getId()));
	}

	/** Returns the products */
	protected Collection<Long> getProductIds() {
		return productIdQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerComponent(String productName, String name) {
		if (searchComponent(productName, name) == null) {
			Set<String> names = componentNameQueue.get(productName);
			if (names == null) {
				names = new HashSet<String>();
				componentNameQueue.put(productName, names);
			}
			names.add(name);
		}
	}

	/** Searches the component whether it is already loaded */
	protected Component searchComponent(String productName, String name) {
		for (Component o : components) {
			if (name.equals(o.getName()) && productName.equals(o.getProject().getName())) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerComponent(Component component) {
		if (!components.contains(component)) components.add(component);
		String productName = component.getProject().getName();
		Set<String> names = componentNameQueue.get(productName);
		if (names != null) {
			names.remove(component.getName());
			if (names.size() == 0) {
				componentNameQueue.remove(productName);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerUser(String name) {
		if (searchUser(name) == null) {
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

	/** Returns the users */
	protected Collection<String> getUserNames() {
		return userNameQueue;
	}

	/** Returns the users */
	protected Collection<Long> getUserIds() {
		return userIdQueue;
	}

	/** Searches the user whether it is already loaded */
	protected User searchUser(String name) {
		for (User o : users) {
			if (name.equals(o.getName())) return o;
		}
		return null;
	}

	/** Searches the user whether it is already loaded */
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
		userIdQueue.remove(Long.getLong(user.getId()));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerComment(long id) {
		if (searchComment(id) == null) {
			commentIdQueue.add(id);
		}
	}

	/** Searches the comment whether it is already loaded */
	protected Comment searchComment(long id) {
		for (Comment o : comments) {
			String oid = o.getId();
			if ((oid != null) && oid.equals(Long.toString(id))) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerComment(Comment comment) {
		if (!comments.contains(comment)) comments.add(comment);
		commentIdQueue.remove(Long.getLong(comment.getId()));
	}

	/** Returns the comments */
	protected Collection<Long> getCommentIds() {
		return commentIdQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerAttachment(long id) {
		if (searchAttachment(id) == null) {
			attachmentIdQueue.add(id);
		}
	}

	/** Searches the attachment whether it is already loaded */
	protected Attachment searchAttachment(long id) {
		for (Attachment o : attachments) {
			String oid = o.getId();
			if ((oid != null) && oid.equals(Long.toString(id))) return o;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerAttachment(Attachment attachment) {
		if (!attachments.contains(attachment)) attachments.add(attachment);
		attachmentIdQueue.remove(Long.getLong(attachment.getId()));
	}

	/** Returns the attachments */
	protected Collection<Long> getAttachmentIds() {
		return attachmentIdQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerPriority(String name) {
		if (searchPriority(name) == null) {
			priorityNameQueue.add(name);
		}
	}

	/** Searches the priority whether it is already loaded */
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

	/** Returns the priorities */
	protected Collection<String> getPriorityNames() {
		return priorityNameQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerSeverity(String name) {
		if (searchSeverity(name) == null) {
			severityNameQueue.add(name);
		}
	}

	/** Searches the severity whether it is already loaded */
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

	/** Returns the severities */
	protected Collection<String> getSeverityNames() {
		return severityNameQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStatus(String name) {
		if (searchStatus(name) == null) {
			statusNameQueue.add(name);
		}
	}

	/** Searches the status whether it is already loaded */
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

	/** Returns the status */
	protected Collection<String> getStatusNames() {
		return statusNameQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerResolution(String name) {
		if (searchResolution(name) == null) {
			resolutionNameQueue.add(name);
		}
	}

	/** Searches the resolution whether it is already loaded */
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

	/** Returns the resolutions */
	protected Collection<String> getResolutionNames() {
		return resolutionNameQueue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerIssueType(String name) {
		if (searchIssueType(name) == null) {
			issueTypeNameQueue.add(name);
		}
	}

	/** Searches the issue type whether it is already loaded */
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

	/** Returns the issue types */
	protected Collection<String> getIssueTypeNames() {
		return issueTypeNameQueue;
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

	/** Loads the classifications */
	protected abstract void loadClassifications() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProduct(long id) {
		Project rc = searchProduct(id);
		if (rc == null) {
			try {
				loadProducts();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load products", e);
			}
			rc = searchProduct(id);
		}
		return rc;
	}

	/** Loads the products */
	protected abstract void loadProducts() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getComponent(String productName, String name) {
		Component rc = searchComponent(productName, name);
		if (rc == null) {
			try {
				loadComponents();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load components", e);
			}
			rc = searchComponent(productName, name);
		}
		return rc;
	}

	/** Loads the components */
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

	/** Loads the users */
	protected abstract void loadUsers() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Comment getComment(long id) {
		Comment rc = searchComment(id);
		if (rc == null) {
			try {
				loadComments();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load comments", e);
			}
			rc = searchComment(id);
		}
		return rc;
	}

	/** Loads the comments */
	protected abstract void loadComments() throws Exception;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Attachment getAttachment(long id) {
		Attachment rc = searchAttachment(id);
		if (rc == null) {
			try {
				loadAttachments();
			} catch (Exception e) {
				throw new RuntimeException("Cannot load attachments", e);
			}
			rc = searchAttachment(id);
		}
		return rc;
	}

	/** Loads the attachments */
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

	/** Loads the priorities */
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

	/** Loads the severities */
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

	/** Loads the status */
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

	/** Loads the resolutions */
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

	/** Loads the issue types */
	protected abstract void loadIssueTypes() throws Exception;

}
