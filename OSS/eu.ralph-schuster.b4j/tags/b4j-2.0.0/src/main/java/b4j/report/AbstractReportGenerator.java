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
package b4j.report;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

import b4j.core.Session;

/**
 * @author Ralph Schuster
 *
 */
public abstract class AbstractReportGenerator implements BugzillaReportGenerator {

	private Session  bugzillaSession;

	/**
	 * 
	 */
	public AbstractReportGenerator() {
	}

	/**
	 * Configures the file report.
	 * This implementation retrieves the value for "outputFile" or throws
	 * an exception when no such element was defined.
	 * @param config - the configuration object
	 * @throws ConfigurationException - when a configuration problem occurs
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
	}

	/**
	 * Returns the maximum Bugzilla version this session supports.
	 * Returns null to indicate that no upper limits must be enforced.
	 * @return maximum version of supported Bugzilla version or null
	 */
	@Override
	public String getMaximumBugzillaVersion() {
		return null;
	}

	/**
	 * Returns the minimum Bugzilla version this session supports.
	 * Returns null to indicate that no lower limits must be enforced.
	 * @return minimum version of supported Bugzilla version or null
	 */
	@Override
	public String getMinimumBugzillaVersion() {
		return null;
	}

	/**
	 * Prepares a new report.
	 * This will be called when a new report must be generated.
	 */
	@Override
	public void prepareReport() {
	}


	/**
	 * Returns the current Bugzilla session object.
	 * @return the bugzillaSession used for this report.
	 */
	protected Session getBugzillaSession() {
		return bugzillaSession;
	}


	/**
	 * Sets the current Session object used.
	 * Implementations can use this session object to retrieve more
	 * data out of Bugzilla if the need to do so.
	 * @param bugzillaSession - the current session object
	 */
	@Override
	public void setBugzillaSession(Session bugzillaSession) {
		this.bugzillaSession = bugzillaSession;
	}

	
}
