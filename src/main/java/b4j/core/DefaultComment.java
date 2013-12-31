package b4j.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A long description entry.
 * This object is used to store information about long description texts.
 * @author ralph
 * @since 2.0
 *
 */
public class DefaultComment implements Comment {

	/**
	 * 
	 */
	private final Issue issue;
	private String id;
	private User author;
	private Date when;
	private String theText;
	private Date lastUpdate;
	private User updateAuthor;
	private Set<String> attachments;

	/**
	 * Default Constructor.
	 */
	public DefaultComment(Issue issue) {
		this.issue = issue;
		id = "unknown";
		when = new Date(0);
		attachments = new HashSet<String>();
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
	public Issue getIssue() {
		return this.issue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getAuthor() {
		if (author != null) return author;
		return this.issue.getReporter();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAuthor(User author) {
		this.author = author;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getWhen() {
		if (when.getTime() > 0) return when;
		return this.issue.getCreationTimestamp();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWhen(Date when) {
		this.when.setTime(when.getTime());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTheText() {
		return theText;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTheText(String theText) {
		this.theText = theText;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getLastUpdate() {
		if (lastUpdate != null) return lastUpdate;
		return getWhen();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getUpdateAuthor() {
		return updateAuthor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpdateAuthor(User updateAuthor) {
		this.updateAuthor = updateAuthor;
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
	public void setAttachments(Attachment... attachments) {
		removeAllAttachments();
		addAttachments(attachments);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttachmentIds(Collection<String> ids) {
		removeAllAttachments();
		addAttachmentIds(ids);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttachmentIds(String... ids) {
		removeAllAttachments();
		addAttachmentIds(ids);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAttachmentIds(Collection<String> ids) {
		for (String id : ids) {
			attachments.add(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAttachmentIds(String... ids) {
		for (String id : ids) {
			attachments.add(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAttachments(Attachment... attachments) {
		for (Attachment item : attachments) {
			this.attachments.add(item.getId());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAttachments(Collection<Attachment> attachments) {
		for (Attachment item : attachments) {
			this.attachments.add(item.getId());
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
	public Collection<Attachment> getAttachments() {
		List<Attachment> rc = new ArrayList<Attachment>();
		for (String id : this.attachments) {
			for (Attachment item : issue.getAttachments()) {
				if (item.getId().equals(id)) {
					rc.add(item);
				}
			}
		}
		return rc;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttachments(Collection<Attachment> attachments) {
		for (Attachment item : attachments) {
			this.attachments.remove(item.getId());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttachments(Attachment... attachments) {
		for (Attachment item : attachments) {
			this.attachments.remove(item.getId());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttachmentIds(Collection<String> ids) {
		for (String id : ids) {
			this.attachments.remove(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttachmentIds(String... ids) {
		for (String id : ids) {
			this.attachments.remove(id);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAttachmentCount() {
		return attachments.size();
	}

}