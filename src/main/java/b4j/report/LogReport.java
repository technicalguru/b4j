/**
 * 
 */
package b4j.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b4j.core.Issue;

/**
 * @author Ralph Schuster
 *
 */
public class LogReport extends AbstractReportGenerator {

	private static Logger log = LoggerFactory.getLogger(LogReport.class);
	
	private int count = 0;
	/**
	 * 
	 */
	public LogReport() {
	}

	/* (non-Javadoc)
	 * @see b4j.report.BugzillaReportGenerator#closeReport()
	 */
	@Override
	public void closeReport() {
		log.info(count+" bugs found");
	}

	/* (non-Javadoc)
	 * @see b4j.report.BugzillaReportGenerator#registerBug(b4j.core.Issue)
	 */
	@Override
	public void registerBug(Issue bug) {
		log.info(bug.getId()+": "+bug.getStatus()+" "+bug.getPriority()+" "+bug.getResolution()+" "+bug.getShortDescription());
		count++;
	}

	/* (non-Javadoc)
	 * @see b4j.report.AbstractReportGenerator#prepareReport()
	 */
	@Override
	public void prepareReport() {
		count = 0;
		super.prepareReport();
	}

	
}
