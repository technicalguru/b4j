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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import java.io.File;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

import b4j.core.BugzillaSession;

/**
 * Abstract implementation for reports that generate a file on a 
 * local disk.
 * <p>
 * This class already implements default methods for most of
 * the interface's methods. So you usually just need to implement
 * {@link BugzillaReportGenerator#closeReport()}.
 * </p>
 * @author Ralph Schuster
 *
 */
public abstract class AbstractFileReport implements BugzillaReportGenerator {

	private File outputFile;
	private OutputStream outputStream;
	private BugzillaSession bugzillaSession;
	
	/**
	 * Default constructor.
	 */
	public AbstractFileReport() {
	}

	
	/**
	 * Configures the file report.
	 * This implementation retrieves the value for "outputFile" or throws
	 * an exception when no such element was defined.
	 * @param config - the configuration object
	 * @throws ConfigurationException - when a configuration problem occurs
	 */
	@Override
	public void init(Configuration config) throws ConfigurationException {
		String s = config.getString("outputFile");
		if (s != null) outputFile = new File(s);
		else throw new ConfigurationException("No outputFile element found");
	}


	/**
	 * Returns the configured output file.
	 * @return the output file
	 */
	public File getOutputFile() {
		return outputFile;
	}

	/**
	 * Returns the stream to the output file.
	 * @return the output stream to our file
	 */
	public OutputStream getOutputStream() {
		if (outputStream == null) {
			if (getOutputFile() == null) outputStream = System.out;
			else try {
				outputStream = new FileOutputStream(getOutputFile());
			} catch (FileNotFoundException e) {
				throw new IllegalStateException("Cannot create file stream", e);
			}
			
		}
		return outputStream;
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
	protected BugzillaSession getBugzillaSession() {
		return bugzillaSession;
	}


	/**
	 * Sets the current BugzillaSession object used.
	 * Implementations can use this session object to retrieve more
	 * data out of Bugzilla if the need to do so.
	 * @param bugzillaSession - the current session object
	 */
	@Override
	public void setBugzillaSession(BugzillaSession bugzillaSession) {
		this.bugzillaSession = bugzillaSession;
	}

	
}
