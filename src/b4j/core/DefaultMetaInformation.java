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

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import b4j.report.BugzillaReportGenerator;


/**
 * Default implementation of meta information for a report generator.
 * This class reads its data from a XML file.
 * @author Ralph Schuster
 *
 */
public class DefaultMetaInformation implements MetaInformation {

	private static Log log = LogFactory.getLog(DefaultMetaInformation.class);
	
	private List<BugzillaReportGenerator> reports;
	private BugzillaSearchData searchData = new DefaultBugzillaSearchData();
	private BugzillaSession bugzillaSession;
	
	/**
	 * Default Constructor.
	 */
	public DefaultMetaInformation() {
	}
	
	/**
	 * Constructor with XML configuration file.
	 */
	public DefaultMetaInformation(File metaFile) throws Exception {
		read(metaFile);
	}

	/**
	 * Reads the XML configuration file.
	 * @param metaFile - the XML file to read
	 * @throws Exception - if an error occurs
	 */
	public void read(File metaFile) throws Exception {
			read(new FileReader(metaFile));
	}

	/**
	 * Reads and parses the XML configuration file from a stream reader.
	 * @param reader - a reader on the XML file.
	 * @throws ConfigurationException - if a configuration error occurs
	 */
	public void read(Reader reader) throws ConfigurationException {
			XMLConfiguration config = new XMLConfiguration();
			config.load(reader);
			configure(config);
	}
	
	/**
	 * Configures the data from an XML configuration object.
	 * @param config - XML configuration
	 * @throws ConfigurationException - if a configuration error occurs
	 */
	@SuppressWarnings("unchecked")
	public void configure(XMLConfiguration config) throws ConfigurationException {
		// Create and configure bugzilla session
		String className = null;
		try {
			Configuration sessionCfg = config.configurationAt("bugzilla-session(0)");
			className = sessionCfg.getString("[@class]");
			Class c = Class.forName(className);
			bugzillaSession = (BugzillaSession)c.newInstance();
			bugzillaSession.configure(sessionCfg);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException("Cannot find class: "+className, e);
		} catch (InstantiationException e) {
			throw new ConfigurationException("Cannot instantiate class: "+className, e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Cannot access constructor: "+className, e);
		}

		// Reports to generate
		reports = new ArrayList<BugzillaReportGenerator>();
		List<String> classNames = config.getList("report[@class]");
		Iterator<String> i = classNames.iterator();
		int idx = 0;
		while (i.hasNext()) {
			className = i.next();
			try {
				Class<?> clazz = Class.forName(className);
				Class<BugzillaReportGenerator> clazz2 = (Class<BugzillaReportGenerator>)clazz;
				BugzillaReportGenerator r = clazz2.newInstance();
				SubnodeConfiguration cConfig = config.configurationAt("report("+idx+")");
				r.init(cConfig);
				r.setBugzillaSession(bugzillaSession);
				reports.add(r);
			} catch (ClassNotFoundException e) {
				log.error("Cannot find report "+className);
			} catch (InstantiationException e) {
				log.error("Cannot instantiate "+className);
			} catch (IllegalAccessException e) {
				log.error("Cannot access constructor of "+className);
			}
			idx++;
		}

		// Create and configure search data
		searchData = new DefaultBugzillaSearchData();
		Configuration searchCfg = config.configurationAt("search(0)");
		searchData.configure(searchCfg);
	}

	/**
	 * Returns all report generators.
	 * @return iterator on all report generators.
	 */
	public Iterator<BugzillaReportGenerator> getReports() {
		return reports.iterator();
	}
	
	/**
	 * Returns the search data.
	 * @return search data object
	 */
	@Override
	public BugzillaSearchData getBugzillaSearchData() {
		return searchData;
	}

	/**
	 * Returns the session object to be used.
	 * @return session object through which all Bugzilla communication will be performed
	 */
	@Override
	public BugzillaSession getBugzillaSession() {
		return bugzillaSession;
	}

	
	/**
	 * Adds a new report generator.
	 * @param e - the generator to add
	 * @return true if generator could be added
	 */
	public boolean addReport(BugzillaReportGenerator e) {
		return reports.add(e);
	}

	/**
	 * Adds multiple report generators.
	 * @param c - collection of generators to add
	 * @return true if generators were added
	 */
	public boolean addAllReports(Collection<? extends BugzillaReportGenerator> c) {
		return reports.addAll(c);
	}

	/**
	 * Clears all generators.
	 */
	public void clearReports() {
		reports.clear();
	}

	/**
	 * Removes a report generator.
	 * @param o - generator object to remove
	 * @return true if generator could be removed
	 */
	public boolean removeReport(BugzillaReportGenerator o) {
		return reports.remove(o);
	}

	/**
	 * Removes all generators listed in argument.
	 * @param c - all generators to remove
	 * @return true if all reports were removed
	 */
	public boolean removeAllReports(Collection<?> c) {
		return reports.removeAll(c);
	}

	/**
	 * Sets the search parameter object.
	 * @param searchData - the search data to set
	 */
	public void setSearchData(BugzillaSearchData searchData) {
		this.searchData = searchData;
	}

	/**
	 * Debug information into log.
	 */
	public void dump() {
		dump(log);
	}
	
	/**
	 * Debug information into log.
	 * @param log - the log object
	 */
	@Override
	public void dump(Log log) {
		if (!log.isDebugEnabled()) return;
		getBugzillaSession().dump(log);
		getBugzillaSearchData().dump(log);
		Iterator<BugzillaReportGenerator> j = getReports();
		while (j.hasNext()) log.info("report="+j.next());
	}
}
