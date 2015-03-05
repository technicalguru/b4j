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

import org.slf4j.Logger;

import b4j.report.BugzillaReportGenerator;

/**
 * Interface for delivering meta information in order to create reports.
 * You can write your own implementations in case the default doesn't fit your needs.
 * @author Ralph Schuster
 *
 */
public interface MetaInformation extends BugzillaObject {

	/**
	 * Returns the session object to be used.
	 * @return session object through which all Bugzilla communication will be performed
	 */
	public Session getBugzillaSession();
	
	/**
	 * Returns the search data.
	 * @return search data object
	 */
	public SearchData getBugzillaSearchData();
	
	/**
	 * Returns all report generators.
	 * @return iterator on all BugzillaReportGenerator objects to be used
	 */
	public Iterable<BugzillaReportGenerator> getReports();
	
	/**
	 * Debug information into log.
	 * @param log - the log object
	 */
	public void dump(Logger log);
}
