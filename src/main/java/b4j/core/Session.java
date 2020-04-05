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

import java.io.IOException;
import java.io.InputStream;

import rs.baselib.configuration.IConfigurable;



/**
 * Represents a Bugzilla session. All interactions are performed through implementations
 * of this interface. Bugzilla sessions could be based on HTTP or MYSQL protocols or any
 * other mean of connection. This is transparent to the application using such a session.
 * <p>
 * Bugzilla latest version 3 announced an XML-RPC Webservice interface. However, all
 * functions seems to be marked unstable and/or experimental. So no session implementation
 * for that protocol will be delivered yet. The author regards that service as highly welcome
 * as it will support the B4J features in best way.
 * <p>
 * This interface can be extended in later versions to support GUI-styled interfaces. Future
 * additions might include addBug(), editBug(), listAccounts().
 * </p>
 * @author Ralph Schuster
 *
 */
public interface Session extends IConfigurable {

	/**
	 * Returns true when session is connected to Bugzilla.
	 * @return true if session was successfully established, false otherwise
	 */
	public boolean isLoggedIn();
	
	/**
	 * Opens the session with configured Bugzilla instance.
	 * @return true when session could be established successfully, false otherwise
	 */
	public boolean open();
	
	/**
	 * Closes the previously established Bugzilla session.
	 */
	public void close();
	
	/**
	 * Performs a search for Bugzilla bugs.
	 * This method returns an iterable over all bug records found. Implementations
	 * should not perform a query for all actual record data here as callers might not
	 * want to retrieve this data. Instead the iterator's next() method of the
	 * {@link Iterable} should retrieve these details.
	 * @param searchData - all search parameters
	 * @param callback - a callback object that will retrieve the number of bugs 
	 * found for this search
	 * @return iterable on all bugs fulfilling the criteria expressed by search parameters.
	 */
	public Iterable<Issue> searchBugs(SearchData searchData, SearchResultCountCallback callback);
	
	/**
	 * Returns the given issue
	 * @param id id of issue
	 * @return issue
	 */
	public Issue getIssue(String id);
	
	/**
	 * Returns an input stream that will contain the attachment's content.
	 * A caller is responsible to close the stream again.
	 * @param attachment attachment to retrieve.
	 * @return the input stream for the attachment itself
	 * @throws IOException when the IO stream cannot be created
	 * @since 1.3
	 */
	public InputStream getAttachment(Attachment attachment) throws IOException;
	
	/**
	 * Debug information into log.
	 */
	public void dump();
	
	/**
	 * Returns the minimum Bugzilla version this session class supports.
	 * Can return null if no lower limits must be enforced.
	 * @return minimum version of supported Bugzilla software.
	 */
	public String getMinimumBugzillaVersion();
	
	/**
	 * Returns the maximum Bugzilla version this session class supports.
	 * Can return null if no upper limits must be enforced.
	 * @return maximum version of supported Bugzilla software.
	 */
	public String getMaximumBugzillaVersion();
	
	/**
	 * Returns the bugzilla version this session object is connected to.
	 * Can return null if version is unknown.
	 * @return version number or null if unknown or not connected
	 */
	public String getBugzillaVersion();
}
