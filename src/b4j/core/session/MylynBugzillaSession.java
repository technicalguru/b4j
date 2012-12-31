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
package b4j.core.session;

import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;

import b4j.core.BugzillaBug;
import b4j.core.BugzillaSearchData;
import b4j.core.SearchResultCountCallback;

/**
 * Implements the Bugzilla session on basis of Mylyn project.
 * Mylyn is an Eclipse project that has many more features
 * than {@link HttpBugzillaSession} has implemented. It is
 * the native choice as you just need to keep updated with
 * progress within Mylyn.
 * <p>
 * Please note that this class is marked abstract right
 * now due to fact that Mylyn-Bugzilla connector does not
 * work yet standalone although Mylyn promised.
 * </p>
 * @author Ralph Schuster
 *
 */
public abstract class MylynBugzillaSession extends AbstractAuthorizedSession {

	//private static Log log = LogFactory.getLog(MylynBugzillaSession.class);
	
	/**
	 * Constructor.
	 */
	public MylynBugzillaSession() {
		throw new IllegalStateException("Class cannot be instantiated yet");
	}

	/**
	 * Configures the session.
	 * The method is called to initialize the session object from a configuration.
	 * The configuration object must ensure to contain  "bugzilla-home" to contain 
	 * the HTTP URL.
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		super.configure(config);
	}

	/**
	 * Opens the session with configured Bugzilla instance.
	 * @return true when session could be established successfully, false otherwise
	 */
	@Override
	public boolean open() {
		if (isLoggedIn()) return true;
		return false;
	}

	/**
	 * Returns true when session is connected to Bugzilla.
	 * @return true if session was successfully established, false otherwise
	 */
	@Override
	public boolean isLoggedIn() {
		return false;
	}

	/**
	 * Performs a search for Bugzilla bugs.
	 * This method returns an iterator over all bug records found.
	 * @param searchData - all search parameters
	 * @param callback - a callback object that will retrieve the number of bugs 
	 * found for this search
	 * @return iterator on all bugs fulfilling the criteria expressed by search parameters.
	 */
	@Override
	public Iterator<BugzillaBug> searchBugs(BugzillaSearchData searchData,
			SearchResultCountCallback callback) {
		if (!isLoggedIn()) return null;

		return null;
	}

	/**
	 * Closes the previously established Bugzilla session.
	 */
	@Override
	public void close() {
		if (!isLoggedIn()) return;
	}

	/**
	 * Returns the bugzilla version this session object is connected to.
	 * Can return null if version is unknown.
	 * @return version number or null if unknown or not connected
	 */
	@Override
	public String getBugzillaVersion() {
		return null;
	}

	/**
	 * Returns the minimum Bugzilla version this session class supports.
	 * @return minimum version of supported Bugzilla software.
	 */
	@Override
	public String getMinimumBugzillaVersion() {
		return null;
	}

	/**
	 * Returns the maximum Bugzilla version this session class supports.
	 * @return maximum version of supported Bugzilla software.
	 */
	@Override
	public String getMaximumBugzillaVersion() {
		return null;
	}

	/**
	 * Debugs information into log.
	 * @param log - the log object
	 */
	@Override
	public void dump(Log log) {
		if (!log.isDebugEnabled()) return;
		super.dump(log);
	}

}
