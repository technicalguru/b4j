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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

/**
 * Adds entries from the XML configuration for each release listed.
 * <p>Configuration:</p>
 * <pre>
 *       &lt;ChangeLogEntryProvider class="b4j.report.DefaultChangeLogEntryProvider"&gt;
 *          &lt;Release name="Release 1.4.0"&gt;
 *             This is an additional entry defined in the report configuration for 1.0
 *             And this is the 2nd entry defined in the report configuration
 *          &lt;/Release&gt;
 *       &lt;/ChangeLogEntryProvider&gt;
 * </pre>
 * @author Ralph Schuster
 *
 */
public class DefaultChangeLogEntryProvider implements ChangeLogEntryProvider {

	private Map<String,List<String>> logEntries;

	/**
	 * Default constructor.
	 */
	public DefaultChangeLogEntryProvider() {
	}

	/**
	 * Returns the additional log entries
	 * @see b4j.report.ChangeLogEntryProvider#getChangeLogEntries(b4j.report.Release)
	 */
	@Override
	public Iterator<String> getChangeLogEntries(Release release) {
		List<String> l = logEntries.get(release.getReleaseName());
		if (l == null) return null;
		return l.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		logEntries = new HashMap<String, List<String>>();
		
		Iterator<Object> releases = config.getList("Release[@name]").iterator();
		int idx = 0;
		while (releases.hasNext()) {
			String name = (String)releases.next();
			String include = config.getString("Release("+idx+")");
			List<String> log = new ArrayList<String>();
			if (include != null) {
				String s[] = include.split("[\\n\\r]+");
				for (int j=0; j<s.length; j++) {
					s[j] = s[j].trim();
					if (s[j].length() > 0) log.add(s[j]);
				}
			}
			if (log.size() > 0) logEntries.put(name, log);
			idx++;
		}
	}

}
