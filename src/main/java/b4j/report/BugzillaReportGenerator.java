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

import rs.baselib.configuration.IConfigurable;
import b4j.core.Issue;
import b4j.core.Session;

/**
 * Interface for a report generator.
 * Generates a report from bug data and writes its results to some
 * place. The place is defined by the implementation of this class.
 * <p>
 * A report instance is usually created first, then {@link IConfigurable#configure(Configuration)}
 * will be called to give the object a chance to configure itself from
 * some configuration object (usually XML based).
 * </p>
 * <p>
 * Then, {@link #prepareReport()} will be called for each separate report this 
 * object has to create.
 * </p>
 * <p>
 * Next, the object will receive each bug to take into account. A report
 * shall collect all necessary information from the bug and forget about the
 * bug itself immediately as potentially thousands of bugs might be queued up.
 * </p>
 * <p>
 * Finally, {@link #closeReport()} will be called. This is the place where
 * your implementation should persist its collected data. Some abstract
 * classes are provided to ease the writing of new reports.
 * </p>
 * @author Ralph Schuster
 *
 */
public interface BugzillaReportGenerator extends IConfigurable {

	/**
	 * Sets the current Session object used.
	 * Implementations can use this session object to retrieve more
	 * data out of Bugzilla if the need to do so.
	 * @param bugzillaSession - the current session object
	 */
	public void setBugzillaSession(Session bugzillaSession);
	
	/**
	 * Returns the minimum Bugzilla version this session supports.
	 * Can return null if no lower limits must be enforced.
	 * @return minimum version of supported Bugzilla version or null
	 */
	public String getMinimumBugzillaVersion();
	
	/**
	 * Returns the maximum Bugzilla version this session supports.
	 * Can return null if no upper limits must be enforced.
	 * @return maximum version of supported Bugzilla version or null
	 */
	public String getMaximumBugzillaVersion();

	/**
	 * Prepares a new report.
	 * This will be called when a new report must be generated.
	 */
	public void prepareReport();
	
	/**
	 * Registers a bug for the report.
	 * Implementations should collect all necessary data from the given bug and
	 * forget about it to save memory.
	 * @param bug - the bug to collect data from
	 */
	public void registerBug(Issue bug);
	
	/**
	 * Closes a report.
	 * Implementations should persist its collected data or results here.
	 */
	public void closeReport();
}
