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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import rs.baselib.util.RsDate;

/**
 * A long description entry.
 * This object is used to store information about long description texts.
 * @author ralph
 * @since 2.0
 *
 */
public class DefaultComment extends AbstractBugzillaObject implements Comment {

	/**
	 * 
	 */
	private String issueId;
	private String id;
	private User author;
	private RsDate when;
	private String theText;
	private RsDate lastUpdate;
	private User updateAuthor;
	private Set<String> attachments;

	/**
	 * Default Constructor.
	 * @param issueId - ID of issue this comment belongs to
	 */
	public DefaultComment(String issueId) {
		this.issueId = issueId;
		id = "unknown";
		when = new RsDate(0);
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
	public String getIssueId() {
		return this.issueId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User getAuthor() {
		return author;
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
	public RsDate getCreationTimestamp() {
		return when;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCreationTimestamp(RsDate when) {
		this.when = when;
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
	public RsDate getUpdateTimestamp() {
		if (lastUpdate != null) return lastUpdate;
		return getCreationTimestamp();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUpdateTimestamp(RsDate lastUpdate) {
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
	public void setAttachments(Collection<String> attachments) {
		removeAllAttachments();
		addAttachments(attachments);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttachments(String... attachments) {
		removeAllAttachments();
		addAttachments(attachments);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAttachmentIds(Collection<String> ids) {
		removeAllAttachments();
		addAttachments(ids);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAttachments(String... attachments) {
		for (String item : attachments) {
			this.attachments.add(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addAttachments(Collection<String> attachments) {
		for (String item : attachments) {
			this.attachments.add(item);
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
	public Collection<String> getAttachments() {
		return Collections.unmodifiableSet(attachments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttachments(Collection<String> attachments) {
		for (String item : attachments) {
			this.attachments.remove(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeAttachments(String... attachments) {
		for (String item : attachments) {
			this.attachments.remove(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getAttachmentCount() {
		return attachments.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DefaultComment other = (DefaultComment) obj;
		if (getId() == null) {
			if (other.getId() != null) return false;
		} else if (!getId().equals(other.getId())) return false;
		return true;
	}


}