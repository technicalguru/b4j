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

/**
 * Interface for a long description entry within a bug record.
 * This object is used to store information about long description texts.
 * @author Ralph Schuster
 *
 */
public interface LongDescription {

	/**
	 * Returns the Bugzilla bug report this entry belongs to.
	 * @return the bug record of this long description.
	 */
	public BugzillaBug getBugzillaBug();
	
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

}
