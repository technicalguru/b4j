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

import java.util.Date;
import java.util.Iterator;

/**
 * Interface for a long description entry within a bug record.
 * This object is used to store information about long description texts.
 * @author Ralph Schuster
 *
 */
public interface LongDescription {

	/**
	 * Returns the description ID.
	 * @return the description ID
	 */
	public String getId();


	/**
	 * Sets the description ID
	 * @param id - the ID to set
	 */
	public void setId(String id);

	/**
	 * Returns the Bugzilla bug report this entry belongs to.
	 * @return the bug record of this long description.
	 */
	public Issue getBugzillaBug();
	
	/**
	 * Returns the author of this text.
	 * @return the editor
	 */
	public String getWho();

	/**
	 * Sets the author of this text
	 * @param who - the author to set
	 */
	public void setWho(String who);

	/**
	 * Returns the time of creation of this entry.
	 * @return the time of creation
	 */
	public Date getWhen();

	/**
	 * Sets the time of creation.
	 * @param when - the timestamp to set
	 */
	public void setWhen(Date when);

	/**
	 * Returns the actual text.
	 * @return the text
	 */
	public String getTheText();

	/**
	 * Sets the text of the description.
	 * @param theText - the text to set
	 */
	public void setTheText(String theText);

	/**
	 * Returns the date of last update.
	 * @return date of last update.
	 */
	public Date getLastUpdate();
	
	/**
	 * Sets the date of last update.
	 * @param lastUpdate date to be set
	 */
	public void setLastUpdate(Date lastUpdate);
	
	/**
	 * Returns the author of the last update.
	 * @return name of author
	 */
	public String getUpdateAuthor();
	
	/**
	 * Sets the last update's author.
	 * @param updateAuthor author to be set
	 */
	public void setUpdateAuthor(String updateAuthor);
	
	/**
	 * Returns the name of the update's author.
	 * @return name of author
	 */
	public String getUpdateAuthorName();

	/**
	 * Sets the name of the update's author.
	 * @param updateAuthorName name of author
	 */
	public void setUpdateAuthorName(String updateAuthorName);
	
	/**
	 * Creates and adds an attachment to this bug record.
	 * @param id the attachment id referenced
	 */
	public void addAttachment(String id);

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
	 * Returns all attachments.
	 * @return iterator on all attachments.
	 */
	public Iterable<Attachment> getAttachments();

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

}
