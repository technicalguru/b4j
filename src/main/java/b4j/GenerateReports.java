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
package b4j;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b4j.core.DefaultMetaInformation;
import b4j.core.Issue;
import b4j.core.MetaInformation;
import b4j.core.Session;
import b4j.report.BugzillaReportGenerator;
import rs.baselib.util.CommonUtils;

/**
 * Main task object that creates a report from Bugzilla.
 * This class represents the main purpose of Bugzilla4Java project.
 * It is configured through a {@link MetaInformation} object that
 * delivers the Bugzilla session object to be used, the search parameters
 * that bugs must fulfill in order to be included in a report, and the
 * actual report objects that will create the output.
 * <p>
 * Users of this class just need to create an instance, feed the appropriate
 * {@link MetaInformation} and run it through its {@link Runnable} interface.
 * </p>
 * <p>
 * The implemented {@link #main(String[])} method reads information from 
 * a given XML file and creates a {@link MetaInformation} object out of it.
 * </p>
 * 
 * @author Ralph Schuster
 *
 */
public class GenerateReports implements Runnable {

	private static Logger log = LoggerFactory.getLogger(GenerateReports.class);

	/** The meta information */
	private MetaInformation metaInformation;

	/** 
	 * Indicator if Bugzilla session should be closed when reports were 
	 * created.
	 */
	private boolean closeSessionWhenDone;

	/**
	 * Default Constructor.
	 */
	public GenerateReports() {
		setCloseSessionWhenDone(false);
	}

	/**
	 * Returns the meta information object for this generator.
	 * @return the metaInformation
	 */
	public MetaInformation getMetaInformation() {
		return metaInformation;
	}

	/**
	 * Sets the meta information object that this generator shall use.
	 * @param metaInformation the meta information to set
	 */
	public void setMetaInformation(MetaInformation metaInformation) {
		this.metaInformation = metaInformation;
	}


	/**
	 * Returns whether the generator will close the Bugzilla session
	 * after all reports were generated.
	 * Default is false.
	 * @return true when session will be closed, false otherwise.
	 */
	public boolean isCloseSessionWhenDone() {
		return closeSessionWhenDone;
	}

	/**
	 * Controls whether Bugzilla session will be closed after all
	 * reports were created. Default is false;
	 * @param closeSessionWhenDone - true if session shall be closed, false otherwise.
	 */
	public void setCloseSessionWhenDone(boolean closeSessionWhenDone) {
		this.closeSessionWhenDone = closeSessionWhenDone;
	}

	/**
	 * Starts the generator from command line.
	 * Reads the XML configuration file and starts the generator.
	 * The method accepts one argument: -x &lt;xml-config-file&gt;
	 * @param args - command line arguments.
	 */
	public static void main(String[] args) {
		try {
			log.info("GenerateReports started");

			// Parse the command line
			Parser parser = new GnuParser();
			CommandLine cl = parser.parse(getCommandLineOptions(), args);

			// Get the report data
			File xmlFile = new File(cl.getOptionValue("x"));
			if (!xmlFile.exists() || !xmlFile.canRead()) {
				log.error("Cannot read \""+xmlFile.getAbsolutePath()+"\"");
			} else {
				// Create and configure the report
				DefaultMetaInformation metaInfo = new DefaultMetaInformation();
				metaInfo.read(xmlFile);
				GenerateReports report = new GenerateReports();
				report.setMetaInformation(metaInfo);
				metaInfo.dump(log);

				// Run the report
				report.setCloseSessionWhenDone(true);
				report.run();
			}
		} catch (ParseException e) {
			HelpFormatter f = new HelpFormatter();
			f.printHelp(GenerateReports.class.getName(), getCommandLineOptions(), false);
			//log.error(e);
		} catch (Exception e) {
			log.error("Error occurred", e);
		}
		log.info("GenerateReports finished");			
	}

	/**
	 * Creates the command line options.
	 * @return CL options object
	 */
	protected static Options getCommandLineOptions() {
		Options rc = new Options();
		Option option = null;

		option = new Option("x", "xml-file", true, "XML file with report meta data");
		option.setRequired(true);
		option.setArgs(1);
		rc.addOption(option);
		return rc;
	}


	/**
	 * Actually runs the report.
	 * The method opens the Bugzilla session, performs a search and forwards all
	 * found bug records for registering to all reports configured.
	 * It then closes the reports and the Bugzilla Session if required (see 
	 * {@link #closeSessionWhenDone}.
	 */
	@Override
	public void run() {
		// Create session object
		Session session = getMetaInformation().getBugzillaSession();

		// Login
		if (session.open()) {

			// Make the search
			Iterable<Issue> bugs = session.searchBugs(getMetaInformation().getBugzillaSearchData(), null);
			if (bugs != null) {

				// Prepare all reports
				boolean hasReport = false;
				Iterable<BugzillaReportGenerator> reports = getMetaInformation().getReports();
				for (BugzillaReportGenerator report : reports) {

					// Check compatibility of reports
					if (CommonUtils.isCompatibleVersion(report.getMinimumBugzillaVersion(), report.getMaximumBugzillaVersion(), session.getBugzillaVersion())) {
						report.prepareReport();
						hasReport = true;
					} else {
						log.error("Report incompatible with found Bugzilla version:");
						log.error("   Bugzilla Version:     "+session.getBugzillaVersion());
						log.error("   Report Class:    cc   "+report.getClass().getSimpleName());
						log.error("   Report's min Version: "+report.getMinimumBugzillaVersion());
						log.error("   Report's max Version: "+report.getMaximumBugzillaVersion());
					}
				}

				// Iterate on all bugs
				if (hasReport) {
					for (Issue issue : bugs) {
						log.debug("issue found: "+issue.toString());

						// call all reports to register the bug
						for (BugzillaReportGenerator report : reports) {
							if (CommonUtils.isCompatibleVersion(report.getMinimumBugzillaVersion(), report.getMaximumBugzillaVersion(), session.getBugzillaVersion())) {
								report.registerBug(issue);
							}
						}
					}
				}

				// Ask all reports to finish their work
				for (BugzillaReportGenerator report : reports) {
					if (CommonUtils.isCompatibleVersion(report.getMinimumBugzillaVersion(), report.getMaximumBugzillaVersion(), session.getBugzillaVersion())) {
						log.info("Generating "+report.getClass().getSimpleName()+"...");
						report.closeReport();
					}
				}
			}

			// Logout if required
			if (isCloseSessionWhenDone()) session.close();
		} else {
			log.error("Cannot open Bugzilla session");
		}

	}
}
