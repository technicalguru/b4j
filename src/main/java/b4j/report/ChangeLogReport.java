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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b4j.core.Issue;
import rs.baselib.configuration.ConfigurationUtils;

/**
 * Creates a Change Log from all closed bugs (see {@link Issue#isClosed()}).
 * The change log is a text file that lists all defined releases in configuration
 * along with id and summaries of each bug fixed. A release is defined with name
 * and timestamp within configuration.
 * <p>
 * This implementation does not evaluate the version information of a bug.
 * </p>
 * <p>Configuration:</p>
 * <pre>
 *    &lt;report class="b4j.report.ChangeLogReport"&gt;
 *       &lt;outputFile&gt;test-ChangeLog1.txt&lt;/outputFile&gt;
 *	
 *       &lt;!-- ReleaseProvider Implementation --> 
 *       &lt;ReleaseProvider class="..."&gt;
 *          ...
 *       &lt;/ReleaseProvider&gt;
 *
 *       &lt;!-- Optional: Additional change log entries --> 
 *       &lt;ChangeLogEntryProvider class="..."&gt;
 *          ...
 *       &lt;/ChangeLogEntryProvider>
 *
 *    &lt;/report&gt;
 * </pre>
 * @author Ralph Schuster
 * @see ReleaseProvider
 * @see ChangeLogEntryProvider
 */
public class ChangeLogReport extends AbstractFileReport {

	private static Logger log = LoggerFactory.getLogger(ChangeLogReport.class);

	private Map<Release,List<String>> changeLogs;
	private List<Release> releases;
	private ReleaseProvider releaseProvider;
	private List<ChangeLogEntryProvider> entryProviders;

	/**
	 * Constructor.
	 */
	public ChangeLogReport() {
		entryProviders = new ArrayList<ChangeLogEntryProvider>();
		changeLogs = new HashMap<Release, List<String>>();
		releases = new ArrayList<Release>();
	}


	/**
	 * Reads the configuration for the ChangeLog.
	 * The configuration must be XML based and has the following format:
	 * <pre>
&lt;outputFile&gt;D:/ChangeLog.txt&lt;/outputFile&gt;
&lt;Version timestamp="2008-06-10 00:00"&gt;
	&lt;Name&gt;Release 1.1&lt;/Name&gt;
	&lt;Include&gt;
	All SL Use cases - UC Descriptions reworked\, amended and created
	All SL Use cases - VD11 default sorting stated more precisely
	&lt;/Include&gt;
&lt;/Version&gt;
&lt;Version timestamp="2008-07-16 00:00"&gt;
	&lt;Name&gt;Release 1.2&lt;/Name&gt;
	&lt;Include&gt;UC55 - VD23 Description updated
	&lt;/Include&gt;
&lt;/Version&gt;
&lt;Version timestamp="2008-08-08 00:00"&gt;
	&lt;Name&gt;Release 2.0&lt;/Name&gt;
&lt;/Version&gt;
</pre>
	 * Include lines are taken over into the Change Log.
	 * <p>
	 * Timestamps give the time of the release closure. All bugs closed until
	 * that time will be considered for the ChangeLog of that release.
	 * </p>
	 * <p>
	 * The Name tag assigns a name to your release.
	 * </p> 
	 * @param config - the configuration object
	 * @throws ConfigurationException - when a configuration problem occurs
	 * @see b4j.report.AbstractFileReport#configure(Configuration)
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		super.configure(config);

		// get the release provider
		String className = config.getString("ReleaseProvider[@class]");
		if (className == null) {
			throw new ConfigurationException("No ReleaseProvider configured");
		} else {
			Configuration rConfig = ((HierarchicalConfiguration)config).configurationAt("ReleaseProvider(0)");
			releaseProvider = (ReleaseProvider)ConfigurationUtils.load(rConfig, true);
		}

		// Get all releases and sort them in time
		Iterator<Release> rel = releaseProvider.getReleases();
		while (rel.hasNext()) releases.add(rel.next());
		Collections.sort(releases, new ReleaseComparator());

		// get the entry providers
		List<Object> classNames = config.getList("ChangeLogEntryProvider[@class]");
		Iterator<Object> i = classNames.iterator();
		int idx = 0;
		while (i.hasNext()) {
			className = (String)i.next();
			try {
				ChangeLogEntryProvider r = (ChangeLogEntryProvider)ConfigurationUtils.load(((HierarchicalConfiguration)config).configurationAt("ChangeLogEntryProvider("+idx+")"), true);
				entryProviders.add(r);
			} catch (Exception e) {
				log.error("Cannot load ChangeLogEntryProvider "+className);
			}
			idx++;
		}

	}


	/**
	 * Registers a bug for the report.
	 * @param bug - the bug to collect data from
	 * @see b4j.report.BugzillaReportGenerator#registerBug(b4j.core.Issue)
	 */
	@Override
	public void registerBug(Issue bug) {
		if (!bug.isClosed()) return;
		Release r = getRelease(bug);
		if (r == null) {
			return;
		}

		List<String> l = changeLogs.get(r);
		if (l == null) {
			l = new ArrayList<String>();
			changeLogs.put(r, l);
		}
		l.add(getBugText(bug));
	}

	/**
	 * Returns the text for a bug record to appear in Change Log.
	 * Subclasses can override this function if they wish other texts to appear in a Change Log.
	 * @param bug - the bug to generate the text from
	 * @return the change log entry generated from the bug record
	 */
	protected String getBugText(Issue bug) {
		return "Fixed Bug #"+bug.getId()+" - "+bug.getSummary();
	}

	/**
	 * Maps the bug's last change date to a release.
	 * @param bug - the bug to consider
	 * @return release for that bug
	 */
	protected Release getRelease(Issue bug) {
		long closeDate = bug.getUpdateTimestamp().getTimeInMillis();
		Iterator<Release> ri = releases.iterator();
		Release lastRelease = null;
		long lastTimestamp = 0;
		while (ri.hasNext()) {
			Release r = ri.next();
			long t = r.getReleaseTime().getTime();
			if ((closeDate > t) && (closeDate < lastTimestamp)) {
				return lastRelease;
			}
			lastTimestamp = t;
			lastRelease = r;
		}
		if (closeDate < lastTimestamp) {
			return lastRelease;
		}
		return null;
	}


	/**
	 * Closes a report.
	 * Implementations should persist its collected data or results here.
	 * @see b4j.report.BugzillaReportGenerator#closeReport()
	 */
	@Override
	public void closeReport() {
		PrintWriter out = new PrintWriter(new OutputStreamWriter(getOutputStream(), StandardCharsets.UTF_8));

		Iterator<Release> ri = releases.iterator();
		while (ri.hasNext()) {
			Release r = ri.next();
			List<String> log = changeLogs.get(r);
			if (log == null) log = new ArrayList<String>();

			// Add additional entries from entry providers
			Iterator<ChangeLogEntryProvider> ci = entryProviders.iterator();
			while (ci.hasNext()) {
				ChangeLogEntryProvider cp = ci.next();
				Iterator<String> e = cp.getChangeLogEntries(r);
				if (e != null) {
					while (e.hasNext()) log.add(e.next());
				}
			}

			// Print the log
			String name = getReleaseText(r);
			out.println(name);
			out.println(StringUtils.leftPad("", name.length(), '='));
			out.println();
			if (log.size() > 0) {
				Iterator<String> entries = log.iterator();
				while (entries.hasNext()) {
					out.println("  * "+entries.next());
				}
			} else {
				out.println("  No changes.");
			}
			out.println();
			out.println();
		}
		out.close();
		log.info("Report created");
	}

	/**
	 * Returns the text for a release to appear.
	 * The text will be printed as a header for each section with entries. The
	 * default implementation returns the release name. Subclasses can override
	 * this method if they wish to generate other headers.
	 * @param r - the release to generate the header text from
	 * @return the text to print for each change log section
	 */
	protected String getReleaseText(Release r) {
		return "Change Log "+r.getReleaseName();
	}

	/**
	 * Implements the sorting of releases.
	 * Releases are sorted in backward order in history.
	 * @author Ralph Schuster
	 *
	 */
	protected static class ReleaseComparator implements Comparator<Release> {

		/**
		 * Compares two releases.
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Release o1, Release o2) {
			if (o1.getReleaseTime().getTime() < o2.getReleaseTime().getTime()) return 1;
			if (o1.getReleaseTime().getTime() > o2.getReleaseTime().getTime()) return -1;
			return -o1.getReleaseName().compareTo(o2.getReleaseName());
		}

	}
}
