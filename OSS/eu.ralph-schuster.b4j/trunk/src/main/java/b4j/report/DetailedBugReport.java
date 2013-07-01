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
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import templating.Templating;
import b4j.core.Issue;
import b4j.core.LongDescription;
import b4j.util.BugzillaUtils;

/**
 * Reports some details about bug records into a file.
 * <p>Configuration:</p>
 * <pre>
 *    &lt;report class="b4j.report.DetailedBugReport"&gt;
 *       &lt;outputFile&gt;test-DetailedBugReport.txt&lt;/outputFile&gt;
 *       &lt;templateFile&gt;detailedBugReport.tmpl&lt;/templateFile&gt;
 *    &lt;/report&gt;
 * </pre>
 * @author Ralph Schuster
 * @see Templating
 *
 */
public class DetailedBugReport extends AbstractFileReport {

	private static Logger log = LoggerFactory.getLogger(DetailedBugReport.class);
	
	private PrintWriter writer;
	private String overallTemplate;
	private String bugTemplate;
	
	/**
	 * Constructor.
	 */
	public DetailedBugReport() {
	}

	/**
	 * Reads the configuration for the Bug Report.
	 * The configuration must be XML based and has the following format:
	 * <pre>
&lt;outputFile&gt;D:/report.txt&lt;/outputFile&gt;
&lt;templateFile&gt;D:/template.txt&lt;/templateFile&gt;
</pre>
	 * <p>
	 * The template file follows the rules by Typo3 templates.
	 * </p> 
	 * @param config - the configuration object
	 * @throws ConfigurationException - when a configuration problem occurs
	 * @see b4j.report.AbstractFileReport#init(org.apache.commons.configuration.Configuration)
	 */
	@Override
	public void init(Configuration config) throws ConfigurationException {
		super.init(config);
		String filename = config.getString("templateFile(0)");
		try {
			String template = Templating.getTemplate(filename);
			overallTemplate = Templating.getSubTemplate(template, "REPORT");
			log.trace("overall="+overallTemplate);
			bugTemplate = Templating.getSubTemplate(template, "BUG");
			log.trace("bug="+bugTemplate);
		} catch (IOException e) {
			throw new ConfigurationException("Cannot load "+filename, e);
		}
	}
	
	/**
	 * Just opens the report by sending the report prefix.
	 */
	@Override
	public void prepareReport() {
		super.prepareReport();
		PrintWriter writer = getWriter();
		writer.println(getString(getReportPrefixTemplate()));
	}


	/**
	 * Just closes the report by sending the report suffix.
	 */
	@Override
	public void closeReport() {
		PrintWriter writer = getWriter();
		writer.println(getString(getReportSuffixTemplate()));
		writer.close();
	}

	/**
	 * Registers a bug for the report.
	 * Implementations should collect all necessary data from the given bug and
	 * forget about it to save memory.
	 * @param bug - the bug to collect data from
	 */
	@Override
	public void registerBug(Issue bug) {
		PrintWriter writer = getWriter();
		writer.println(getBugString(bugTemplate, bug));
	}

	/**
	 * Returns the writer object.
	 * @return the writer object
	 */
	private PrintWriter getWriter() {
		if (writer == null) writer = new PrintWriter(getOutputStream());
		return writer;
	}
	
	/**
	 * Returns the part of the template that prefixes the bug records.
	 * @return template for bug report prefix
	 */
	private String getReportPrefixTemplate() {
		int pos = overallTemplate.indexOf("###BUGS###");
		if (pos > 0) return overallTemplate.substring(0, pos);
		if (pos == 0) return "";
		return overallTemplate;
	}
	
	/**
	 * Returns the part of the template that follows the bug records.
	 * @return template for bug report suffix
	 */
	private String getReportSuffixTemplate() {
		int pos = overallTemplate.indexOf("###BUGS###");
		if (pos >= 0) return overallTemplate.substring(pos+10);
		return "";
	}
	
	/**
	 * Replaces all overall variables and returns the result.
	 * @param template - the template to be used
	 * @return template with all variables replaced
	 */
	private String getString(String template) {
		return template.trim();
	}
	
	/**
	 * Replaces all variables for this bug and returns the result.
	 * @param template - the template to be used
	 * @param bug - the bug to be used
	 * @return template with all variables replaced
	 */
	private String getBugString(String template, Issue bug) {
		Map<String,Object> markers = new HashMap<String, Object>();
		markers.put("ID", bug.getId());
		markers.put("ALIAS", bug.getAlias());
		markers.put("ASSIGNEDTO", bug.getAssignee());
		markers.put("CLASSIFICATION", bug.getTypeName());
		markers.put("COMPONENT", bug.getComponent());
		markers.put("CRDATE", bug.getCreationTimestamp());
		markers.put("MTIME", bug.getDeltaTimestamp());
		markers.put("FILELOCATION", bug.getFileLocation());
		markers.put("PRIORITY", bug.getPriority());
		markers.put("OPSYS", bug.getOpSys());
		markers.put("SEVERITY", bug.getSeverity());
		markers.put("PRODUCT", bug.getProduct());
		markers.put("QACONTACT", bug.getQaContact());
		markers.put("REPORTER", bug.getReporter());
		markers.put("RESOLUTION", bug.getResolution());
		markers.put("REPORTER", bug.getReporter());
		markers.put("SUMMARY", bug.getShortDescription());
		markers.put("STATUS", bug.getStatus());
		markers.put("VERSION", bug.getVersion());
		Iterator<LongDescription> i = bug.getLongDescriptionIterator();
		String s = "";
		while (i.hasNext()) {
			LongDescription desc = i.next();
			String t = BugzillaUtils.join("\n   ", desc.getTheText().split("\\n"));
			s += "\n\n"+desc.getWho() + " ("+desc.getWhen()+"):\n   "+t.trim();
		}
		markers.put("DESCRIPTIONS", s.trim());
		template = Templating.replace(template, markers);
		return template.trim();
	}
}
