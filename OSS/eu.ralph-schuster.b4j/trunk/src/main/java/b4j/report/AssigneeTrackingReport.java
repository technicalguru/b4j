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

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b4j.core.Issue;

/**
 * Creates a tracking report for management purposes. The CSV file produced
 * tells about number of opened bugs for each assignee.
 * <p>
 * The report allows configuration of severity groups as too many severities
 * might make a report unreadable.
 * </p>
 * @author Ralph Schuster
 *
 */
public class AssigneeTrackingReport extends AbstractFileReport {

	private static Logger log = LoggerFactory.getLogger(AssigneeTrackingReport.class);
	
	private Map<String,Long> count;
	private int openCount = 0;
	private int closedCount = 0;
	private int totalCount = 0;
	
	/**
	 * Default constructor.
	 */
	public AssigneeTrackingReport() {
	}

	/**
	 * Closes a report.
	 * Implementations should persist its collected data or results here.
	 * @see b4j.report.BugzillaReportGenerator#closeReport()
	 */
	@Override
	public void closeReport() {
		PrintWriter out = new PrintWriter(new OutputStreamWriter(getOutputStream(), Charsets.UTF_8));

		// Header line
		out.println("ASSIGNEE;OPEN BUG COUNT");

		Iterator<String> si = count.keySet().iterator();
		while (si.hasNext()) {
			String assignee = si.next();
			Long l = count.get(assignee);

			StringBuffer line = new StringBuffer();
			line.append("\"");
			line.append(assignee);
			line.append("\";");
			line.append(l);
			out.println(line.toString());
		}
		
		// Total line here
		out.println("TOTAL;"+openCount+"\n");
		out.close();
		
		log.info("Report created");
	}

	/**
	 * Prepares the report.
	 * @see b4j.report.BugzillaReportGenerator#prepareReport()
	 */
	@Override
	public void prepareReport() {
		count = new HashMap<String, Long>();
		openCount = 0;
		closedCount = 0;
		totalCount = 0;
	}

	/**
	 * Registers a bug for the report.
	 * @param bug - the bug to collect data from
	 * @see b4j.report.BugzillaReportGenerator#registerBug(b4j.core.Issue)
	 */
	@Override
	public void registerBug(Issue bug) {
		totalCount++;
		if (bug.isOpen()) {
			openCount++;
			String assignee = bug.getAssignee().getId();
			Long cnt = count.get(assignee);
			if (cnt == null) cnt = new Long(0);
			cnt = cnt+1;
			count.put(assignee, cnt);
		}
		if (bug.isClosed()) {
			closedCount++;
		}
		totalCount++;
	}

	/**
	 * Returns the overall number of open bugs.
	 * @see Issue#isOpen()
	 * @return the openCount
	 */
	public int getOpenCount() {
		return openCount;
	}

	/**
	 * Returns the overall number of open bugs.
	 * @see Issue#isClosed()
	 * @return the closedCount
	 */
	public int getClosedCount() {
		return closedCount;
	}

	/**
	 * Returns the overall count of bugs.
	 * @return the totalCount
	 */
	public int getTotalCount() {
		return totalCount;
	}

}
