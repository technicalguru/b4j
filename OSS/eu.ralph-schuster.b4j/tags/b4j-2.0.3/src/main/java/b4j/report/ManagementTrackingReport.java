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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b4j.core.DefaultIssue;
import b4j.core.Issue;
import csv.CSVFactory;
import csv.TableWriter;

/**
 * Creates a tracking report for management purposes. The CSV file produced
 * tells about number of opened bugs for each calendar week per severity,
 * the amount of those bugs opened already closed, and the average time
 * in days that closing a bug needed.
 * <p>
 * The report allows configuration of severity groups as too many severities
 * might make a report unreadable.
 * </p>
 * <p>Configuration:</p>
 * <pre>
 *    &lt;report class="b4j.report.ManagementTrackingReport"&gt;
 *       &lt;outputFile&gt;test-ManagementTrackingReport.csv&lt;/outputFile&gt;
 * 
 *       &lt;!-- Sev groups are optional. All sevs not listed in a group will be tracked individually --&gt;
 *       &lt;severityGroup name="URGENT"&gt;
 *          &lt;severity&gt;blocker&lt;/severity&gt;
 *          &lt;severity&gt;critical&lt;/severity&gt;
 *          &lt;severity&gt;major&lt;/severity&gt;
 *       &lt;/severityGroup&gt;
 *       &lt;severityGroup name="MINORS"&gt;
 *          &lt;severity&gt;minor&lt;/severity&gt;
 *          &lt;severity&gt;trivial&lt;/severity&gt;
 *       &lt;/severityGroup&gt;
 *    &lt;/report&gt;
 * </pre>
 * @author Ralph Schuster
 *
 */
public class ManagementTrackingReport extends AbstractFileReport {

	private static Logger log = LoggerFactory.getLogger(ManagementTrackingReport.class);

	private Map<String,SeverityStats> severityStats;
	private Map<Integer, WeekStats> weekStats;
	private int openCount = 0;
	private int closedCount = 0;
	private int totalCount = 0;
	private long totalFixTime = 0;
	private int minWeek = 0;
	private int maxWeek = 0;
	private Map<String,String> severityGroups;
	private List<String> allSeverities;

	/**
	 * Default constructor.
	 */
	public ManagementTrackingReport() {
	}

	/**
	 * Closes a report.
	 * Implementations should persist its collected data or results here.
	 * @see b4j.report.BugzillaReportGenerator#closeReport()
	 */
	@Override
	public void closeReport() {
		if (minWeek == 0) {
			log.info("No bug matched criteria. Report is empty.");
			return;
		}

		try {
			TableWriter out = CSVFactory.getFactory().getWriter(getOutputFile()); 
			List<Object> values = new ArrayList<Object>();

			// Headers
			values.add("Week");
			for (String sev : allSeverities) {
				values.add("OPEN "+sev);
				values.add("CLOSED "+sev);
				values.add("TOTAL "+sev);
				values.add("AVG FIX "+sev+" (days)");
			}
			values.add("OPEN TOTAL");
			values.add("CLOSED TOTAL");
			values.add("TOTAL");
			values.add("AVG FIX (days)");
			out.printRow(values);

			// Iterate over each week
			for (int i=minWeek; i<=maxWeek; i++) {
				values.clear();
				values.add("KW"+getWeek(i)+"/"+getYear(i));

				WeekStats wStats = weekStats.get(i);
				if (wStats != null) {
					Map<String, SeverityStats> sevStats = wStats.getSeverityWeekStats();
					for (String sev : allSeverities) {
						SeverityStats stats = sevStats.get(sev);
						if (stats != null) {
							values.add(stats.getOpenCount());
							values.add(stats.getClosedCount());
							values.add(stats.getTotalCount());
							values.add(stats.getAverageFixTime()/DateUtils.MILLIS_PER_DAY);
						} else {
							values.add(0);
							values.add(0);
							values.add(0);
							values.add(0);
						}
					}
					values.add(wStats.getOpenCount());
					values.add(wStats.getClosedCount());
					values.add(wStats.getTotalCount());
					values.add(wStats.getAverageFixTime()/DateUtils.MILLIS_PER_DAY);
				} else {
					for (@SuppressWarnings("unused") String sev : allSeverities) {
						values.add(0);
						values.add(0);
						values.add(0);
						values.add(0);
					}
					values.add(0);
					values.add(0);
					values.add(0);
					values.add(0);
				}
				out.printRow(values);
			}

			// Total line here
			values.clear();
			values.add("TOTAL");
			for (String sev : allSeverities) {
				SeverityStats stats = severityStats.get(sev);
				if (stats != null) {
					values.add(stats.getOpenCount());
					values.add(stats.getClosedCount());
					values.add(stats.getTotalCount());
					values.add(stats.getAverageFixTime()/DateUtils.MILLIS_PER_DAY);
				} else {
					values.add(0);
					values.add(0);
					values.add(0);
					values.add(0);
				}
			}
			values.add(getOpenCount());
			values.add(getClosedCount());
			values.add(getTotalCount());
			values.add(getAverageFixTime()/DateUtils.MILLIS_PER_DAY);
			out.printRow(values);
			out.close();
		} catch (IOException e) {
			throw new RuntimeException("Cannot create CSV file", e);
		}
		log.info("Report created");
	}

	/**
	 * Configures the report.
	 * @param config - the configuration object
	 * @throws ConfigurationException - when a configuration problem occurs
	 * @see b4j.report.AbstractFileReport#configure(Configuration)
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		super.configure(config);
		severityGroups = new HashMap<String, String>();
		allSeverities = new ArrayList<String>();
		Iterator<?> groups = config.getList("severityGroup[@name]").iterator();
		int grno = 0;
		while (groups.hasNext()) {
			String group = groups.next().toString();
			Configuration groupConfig = ((HierarchicalConfiguration)config).configurationAt("severityGroup("+grno+")");
			Iterator<?> sevs = groupConfig.getList("severity").iterator();
			while (sevs.hasNext()) {
				String sev = sevs.next().toString();
				severityGroups.put(sev, group);
			}
			allSeverities.add(group);
			grno++;
		}

		// Adding all remaining severities now for later remember
		for (int i=0; i<DefaultIssue.SEVERITIES.length; i++) {
			if (!severityGroups.containsKey(DefaultIssue.SEVERITIES[i])) allSeverities.add(DefaultIssue.SEVERITIES[i]);
		}
	}

	/**
	 * Prepares the report.
	 * @see b4j.report.BugzillaReportGenerator#prepareReport()
	 */
	@Override
	public void prepareReport() {
		severityStats = new HashMap<String, SeverityStats>();
		weekStats = new HashMap<Integer, WeekStats>();
		openCount = 0;
		closedCount = 0;
		totalCount = 0;
		totalFixTime = 0;
		minWeek = 0;
		maxWeek = 0;
	}

	/**
	 * Registers a bug for the report.
	 * @param bug - the bug to collect data from
	 * @see b4j.report.BugzillaReportGenerator#registerBug(b4j.core.Issue)
	 */
	@Override
	public void registerBug(Issue bug) {
		totalCount++;
		if (bug.isOpen()) openCount++;
		if (bug.isClosed()) {
			closedCount++;
			totalFixTime += bug.getUpdateTimestamp().getTime() - bug.getCreationTimestamp().getTime();
		}

		// forward to severity stats
		String sev = bug.getSeverity().getName();
		String group = severityGroups.get(sev);
		if (group != null) sev = group;
		SeverityStats stats = severityStats.get(sev);
		if (stats == null) {
			stats = new SeverityStats(sev);
			severityStats.put(sev, stats);
		}
		stats.registerBug(bug);

		// Forward to weekly status
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(bug.getCreationTimestamp());
		int week = cal.get(Calendar.YEAR)*100 + cal.get(Calendar.WEEK_OF_YEAR);
		WeekStats wstats = weekStats.get(week);
		if (wstats == null) {
			wstats = new WeekStats(week);
			weekStats.put(week, wstats);
		}
		wstats.registerBug(bug);

		// For later memory
		if (week > maxWeek) maxWeek = week;
		if ((minWeek == 0) || (week < minWeek)) minWeek = week;
	}

	/**
	 * Returns the overall average fix time.
	 * @return average fix time in calendar days.
	 */
	public long getAverageFixTime() {
		if (closedCount > 0) return totalFixTime / closedCount;
		return 0;
	}

	/**
	 * Returns the severity statistics objects.
	 * @return the severityStats
	 */
	public Map<String, SeverityStats> getSeverityStats() {
		return severityStats;
	}

	/**
	 * Returns the Week statistics objects.
	 * @return the weekStats
	 */
	public Map<Integer, WeekStats> getWeekStats() {
		return weekStats;
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

	/**
	 * Returns the minimum week number detected.
	 * @return the minWeek
	 */
	public int getMinWeek() {
		return minWeek;
	}

	/**
	 * Returns the maximum week number detected.
	 * @return the maxWeek
	 */
	public int getMaxWeek() {
		return maxWeek;
	}

	/**
	 * This class implements statistics logic based on severities.
	 * @author Ralph Schuster
	 *
	 */
	protected static class SeverityStats {

		private int openCount = 0;
		private int closedCount = 0;
		private int totalCount = 0;
		private String severity;
		private long totalFixTime = 0;

		/**
		 * Default constructor.
		 * @param severity - severity name
		 */
		public SeverityStats(String severity) {
			this.severity = severity;
		}

		/**
		 * Registers the bug.
		 * @param bug - the bug to collect data from.
		 */
		public void registerBug(Issue bug) {
			totalCount++;
			if (bug.isOpen()) openCount++;
			if (bug.isClosed()) {
				closedCount++;
				totalFixTime += bug.getUpdateTimestamp().getTime() - bug.getCreationTimestamp().getTime();
			}
		}

		/**
		 * Returns the number of open bugs in this severity.
		 * @see Issue#isOpen()
		 * @return the openCount
		 */
		public int getOpenCount() {
			return openCount;
		}

		/**
		 * Returns the number of closed bugs in this severity.
		 * @see Issue#isClosed()
		 * @return the closedCount
		 */
		public int getClosedCount() {
			return closedCount;
		}

		/**
		 * Returns the nuzmber of bugs for this severity.
		 * @return the totalCount
		 */
		public int getTotalCount() {
			return totalCount;
		}

		/**
		 * Returns the severity name.
		 * @return the severity
		 */
		public String getSeverity() {
			return severity;
		}

		/**
		 * Returns the average fix time for this severity.
		 * @return average fix time in calendar days.
		 */
		public long getAverageFixTime() {
			if (closedCount > 0) return totalFixTime / closedCount;
			return 0;
		}
	}

	/**
	 * Object for collecting statistics based on a week.
	 * The class collects also data based on severities.
	 * @author Ralph Schuster
	 *
	 */
	protected class WeekStats {

		private int openCount = 0;
		private int closedCount = 0;
		private int totalCount = 0;
		private Map<String,SeverityStats> severityWeekStats;
		private int week = 0;
		private long totalFixTime = 0;

		/**
		 * Constructor.
		 * @param week - week no
		 */
		public WeekStats(int week) {
			this.week = week;
			severityWeekStats = new HashMap<String, SeverityStats>();
		}

		/**
		 * Registers the bug.
		 * @param bug - the bug to collect data from.
		 */
		public void registerBug(Issue bug) {
			totalCount++;
			if (bug.isOpen()) openCount++;
			if (bug.isClosed()) {
				closedCount++;
				totalFixTime += bug.getUpdateTimestamp().getTime() - bug.getCreationTimestamp().getTime();
			}

			// Forward to proper stat
			String sev = bug.getSeverity().getName();
			String group = severityGroups.get(sev);
			if (group != null) sev = group;
			SeverityStats stats = severityWeekStats.get(sev);
			if (stats == null) {
				stats = new SeverityStats(sev);
				severityWeekStats.put(sev, stats);
			}
			stats.registerBug(bug);
		}

		/**
		 * Returns the average fix time for bugs of this week.
		 * @return average fix time in calendar days.
		 */
		public long getAverageFixTime() {
			if (closedCount > 0) return totalFixTime / closedCount;
			return 0;
		}

		/**
		 * Returns the number of bugs still open from this week.
		 * @see Issue#isOpen()
		 * @return the openCount
		 */
		public int getOpenCount() {
			return openCount;
		}

		/**
		 * Returns the number of closed bugs from this week.
		 * @see Issue#isClosed()
		 * @return the closedCount
		 */
		public int getClosedCount() {
			return closedCount;
		}

		/**
		 * Returns the number of bugs opened in this week.
		 * @return the totalCount
		 */
		public int getTotalCount() {
			return totalCount;
		}

		/**
		 * Returns the severity statistics for this week.
		 * @return the severityWeekStats
		 */
		public Map<String, SeverityStats> getSeverityWeekStats() {
			return severityWeekStats;
		}

		/**
		 * Returns the week no.
		 * @return the week
		 */
		public int getWeek() {
			return week;
		}


	}

	/**
	 * Returns the week in year.
	 * @param week - week ID
	 * @return week no in year
	 */
	protected static int getWeek(int week) {
		return week - getYear(week)*100;
	}

	/**
	 * Returns the year for the week ID.
	 * @param week - week ID
	 * @return year
	 */
	protected static int getYear(int week) {
		return week / 100;
	}
}
