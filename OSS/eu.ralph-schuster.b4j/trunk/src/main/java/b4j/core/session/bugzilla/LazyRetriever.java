/**
 * 
 */
package b4j.core.session.bugzilla;

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
 * Retrieves object lazily when they are needed.
 * The idea is to register objects and bulk-load them later when one of them is retreved.
 * @author ralph
 *
 */
public interface LazyRetriever {

	public void registerClassification(String name);
	public void registerClassification(long id);
	public void registerClassification(Classification classification);
	
	public void registerProduct(long id);
	public void registerProduct(Project product);

	public void registerComponent(String name);
	public void registerComponent(Component component);

	public void registerUser(String name);
	public void registerUser(long id);
	public void registerUser(User user);

	public void registerComment(long id);
	public void registerComment(Comment comment);

	public void registerAttachment(long id);
	public void registerAttachment(Attachment attachment);

	public void registerPriority(String name);
	public void registerPriority(Priority priority);

	public void registerSeverity(String name);
	public void registerSeverity(Severity severity);

	public void registerStatus(String name);
	public void registerStatus(Status status);

	public void registerResolution(String name);
	public void registerResolution(Resolution resolution);

	public void registerIssueType(String name);
	public void registerIssueType(IssueType issueType);
	
	public Classification getClassification(String name);
	public Classification getClassification(long id);
	public Project getProduct(long id);
	public Component getComponent(String name);
	public User getUser(String name);
	public User getUser(long id);
	public Comment getComment(long id);
	public Attachment getAttachment(long id);
	public Priority getPriority(String name);
	public Severity getSeverity(String name);
	public Status getStatus(String name);
	public Resolution getResolution(String name);
	public IssueType getIssueType(String name);
	
}
