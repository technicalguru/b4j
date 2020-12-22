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
import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;

import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.session.BugzillaHttpSession;

/**
 * A demonstration of how a programmatic session opening will work.
 * @author Ralph
 *
 */
public class TestHttp {

	private static Logger log = LoggerFactory.getLogger(TestHttp.class);
	
	/**
	 * A demonstration of how a programmatic session opening will work.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Configuration myConfig = new XMLConfiguration(new File("conf/config.xml"));

			HttpBugzillaSession session = new HttpBugzillaSession();
			session.configure(myConfig);

			// FIX login() = open()
			if (session.open()) {
				log.info("Session opened");
				
				// Create search criteria
				DefaultSearchData searchData = new DefaultSearchData();
				searchData.add("classification", "Java Projects");
				searchData.add("product", "CSV Utility Package");
				
				// Perform the search
				Iterator<Issue> i = session.searchBugs(searchData, null);
				while (i.hasNext()) {
					Issue issue = i.next();
					log.info("Bug found: "+issue.getId()+" - "+issue.getShortDescription());
				}
				// Close the session again
				session.close();
				log.info("Session closed");
			}
		} catch (Throwable t) {
			log.error(t);
		}
	}

}
