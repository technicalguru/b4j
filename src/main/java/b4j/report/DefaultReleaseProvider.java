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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;

import b4j.core.DefaultIssue;

/**
 * Generates releases and additional entries out from XML configuration.
 * <p>
 * Configuration:
 * </p>
 * <pre>
 *       &lt;ReleaseProvider class="b4j.report.DefaultReleaseProvider"&gt;
 *          &lt;Release timestamp="2012-09-29 00:00"&gt;
 *             &lt;Name&gt;Release 1.3&lt;/Name&gt;
 *          &lt;/Release&gt;
 *          &lt;Release timestamp="2012-12-25 00:00"&gt;
 *             &lt;Name&gt;Release 1.4.0&lt;/Name&gt;
 *          &lt;/Release&gt;
 *       &lt;/ReleaseProvider&gt;
 * </pre>
 * @author Ralph Schuster
 * @see ChangeLogReport
 *
 */
public class DefaultReleaseProvider implements ReleaseProvider {

	private List<Release> releases;
	
	/**
	 * Default constructor.
	 */
	public DefaultReleaseProvider() {
		releases = new ArrayList<Release>();
	}

	/**
	 * Configures the provider.
	 * The method will read all information from the XML configuration.
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		Iterator<?> versions = config.getList("Release[@timestamp]").iterator();
		int vno = 0;
		DateFormat parser = DefaultIssue.DATETIME_WITHOUT_SEC();
		while (versions.hasNext()) {
			String timestamp = (String)versions.next();
			Configuration vConfig = ((HierarchicalConfiguration)config).configurationAt("Release("+vno+")");
			String vName = vConfig.getString("Name");
			try {
				Date ts = parser.parse(timestamp);
				Release r = new DefaultRelease(vName, ts);
				addRelease(r);
			} catch (ParseException e) {
				throw new ConfigurationException("Invalid date format: ", e);
			}
			vno++;
		}
	}

	/**
	 * Adds the release to the list of releases.
	 * Please note that a certain release will not be added if there is already such a release.
	 * @param r - the release to add.
	 * @return true if release did not exist and was added.
	 */
	protected boolean addRelease(Release r) {
		if (releaseExists(r)) return false;
		releases.add(r);
		return true;
	}
	
	/**
	 * Tells whether the given release exists.
	 * @param r - release to check for existence
	 * @return true if release already exists.
	 */
	protected boolean releaseExists(Release r) {
		return releases.contains(r);
	}
	
	/**
	 * Removes a certain release from the list.
	 * @param r - release to remove
	 */
	protected void removeRelease(Release r) {
		releases.remove(r);
	}
	
	/**
	 * Returns the releases from the XML configuration.
	 * @return iterator on all releases
	 */
	@Override
	public Iterator<Release> getReleases() {
		return releases.iterator();
	}

}
