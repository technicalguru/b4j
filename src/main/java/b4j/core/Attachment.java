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

import rs.baselib.util.RsDate;

/**
 * Interface for an attachment within a issue.
 * This object represents an attachment for a issue.
 * @author Ralph Schuster
 *
 */
public interface Attachment extends BugzillaObject {

	/** Custom field name */
	public static final String UPDATE_TIMESTAMP = "updateTimestamp";
	
	/**
	 * Returns the ID of this attachment.
	 * @return the ID
	 */
	public String getId();

	/**
	 * Sets the ID of the attachment.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
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
	public RsDate getDate();

	/**
	 * Sets the date of the attachment.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param date - the date to set
	 */
	public void setDate(RsDate date);

	/**
	 * Returns the description of the attachment.
	 * @return the description
	 */
	public String getDescription();

	/**
	 * Sets the description of the attachment.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
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
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
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
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
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
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param uri the content URI
	 */
	public void setUri(URI uri);
}
