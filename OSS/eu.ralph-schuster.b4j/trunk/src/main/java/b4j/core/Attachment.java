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

import java.net.URI;
import java.util.Date;

/**
 * Interface for an attachment within a issue.
 * This object represents an attachment for a issue.
 * @author Ralph Schuster
 *
 */
public interface Attachment {

	/**
	 * Returns the ID of this attachment.
	 * @return the ID
	 */
	public String getId();

	/**
	 * Sets the ID of the attachment.
	 * @param id - the ID to set
	 */
	public void setId(String id);

	/**
	 * Returns the issue ID this attachment belongs to.
	 * @return the issue ID of this attachment.
	 */
	public String getIssueId();
	
	/**
	 * Returns the date of the attachment.
	 * @return the date
	 */
	public Date getDate();

	/**
	 * Sets the date of the attachment.
	 * @param date - the date to set
	 */
	public void setDate(Date date);

	/**
	 * Returns the description of the attachment.
	 * @return the description
	 */
	public String getDescription();

	/**
	 * Sets the description of the attachment.
	 * @param description - the description to set
	 */
	public void setDescription(String description);

	/**
	 * Returns the filename.
	 * @return the filename
	 */
	public String getFilename();

	/**
	 * Sets the filename.
	 * @param filename - the filename to set
	 */
	public void setFilename(String filename);

	/**
	 * Returns the type of the attachment.
	 * @return the type
	 */
	public String getType();

	/**
	 * Sets the type of the attachment.
	 * @param type - the type to set
	 */
	public void setType(String type);
	
	/**
	 * Returns the URI where to retrieve the attachment.
	 * @return the content URI
	 */
	public URI getUri();
	
	/**
	 * Sets the URI where to retrieve the attachment.
	 * @param uri the content URI
	 */
	public void setUri(URI uri);
}
