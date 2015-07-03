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

/**
 * Interface for a long description entry within a issue record.
 * This object is used to store information about long description texts.
 * @author Ralph Schuster
 * @since 2.0
 *
 */
public interface Comment extends BugzillaObject {

	/**
	 * Returns the description ID.
	 * @return the description ID
	 */
	public String getId();


	/**
	 * Sets the description ID
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param id - the ID to set
	 */
	public void setId(String id);

	/**
	 * Returns the ID of the issue this entry belongs to.
	 * @return the ID of the issue of this comment.
	 */
	public String getIssueId();
	
	/**
	 * Returns the author of this text.
	 * @return the editor
	 */
	public User getAuthor();

	/**
	 * Sets the author of this text
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param author - the author to set
	 */
	public void setAuthor(User author);

	/**
	 * Returns the time of creation of this entry.
	 * @return the time of creation
	 */
	public Date getCreationTimestamp();

	/**
	 * Sets the time of creation.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param when - the timestamp to set
	 */
	public void setCreationTimestamp(Date when);

	/**
	 * Returns the actual text.
	 * @return the text
	 */
	public String getTheText();

	/**
	 * Sets the text of the description.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param theText - the text to set
	 */
	public void setTheText(String theText);

	/**
	 * Returns the date of last update.
	 * @return date of last update.
	 */
	public Date getUpdateTimestamp();
	
	/**
	 * Sets the date of last update.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param lastUpdate date to be set
	 */
	public void setUpdateTimestamp(Date lastUpdate);
	
	/**
	 * Returns the author of the last update.
	 * @return name of author
	 */
	public User getUpdateAuthor();
	
	/**
	 * Sets the last update's author.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param updateAuthor author to be set
	 * @since 2.0
	 */
	public void setUpdateAuthor(User updateAuthor);
	
	/**
	 * Returns the attachment IDs for this comment.
	 * @return the attachment IDs
	 */
	public Collection<String> getAttachments();

	/**
	 * Sets the attachment IDs for this comment.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param attachments - the attachments to set
	 */
	public void setAttachments(Collection<String> attachments);

	/**
	 * Sets the attachment IDs for this comment.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param attachments - the attachment IDs to set
	 */
	public void setAttachments(String... attachments);

	/**
	 * Sets the attachments IDs for this comment.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param ids - the ids to set
	 */
	public void setAttachmentIds(Collection<String> ids);

	/**
	 * Adds the attachment IDs for this comment.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param attachments - the attachments to add
	 */
	public void addAttachments(Collection<String> attachments);

	/**
	 * Adds the attachment IDs for this comment.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param attachments - the attachments to add
	 */
	public void addAttachments(String... attachments);

	/**
	 * Removes the attachment IDs for this comment.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param attachments - the attachments to remove
	 */
	public void removeAttachments(Collection<String> attachments);

	/**
	 * Removes the attachment IDs for this issue.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param attachments - the attachments to remove
	 */
	public void removeAttachments(String... attachments);

	/**
	 * Removes the attachments for this issue.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 */
	public void removeAllAttachments();

	/**
	 * Returns the number of attachments for this issue.
	 * @return number of attachments
	 */
	public int getAttachmentCount();


}
