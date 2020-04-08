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

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import b4j.core.DefaultIssue;

/**
 * Loads release information from a XML file.
 * <p>
 * The XML file contains &lt;Release&gt; tags.
 * Each release is configured with name and timestamp of the release:
 * </p>
<pre>
&lt;Release timestamp="2008-06-10 00:00"&gt;
	&lt;Name&gt;Release 20080610&lt;/Name&gt;
&lt;/Release&gt;
</pre>
 * @author Ralph Schuster
 *
 */
public class XmlFileReleaseProvider extends AbstractFileReleaseProvider {

	/**
	 * Default constructor.
	 */
	public XmlFileReleaseProvider() {
	}

	/**
	 * Loads the XML file.
 * The XML file contains &lt;Release&gt; tags.
 * Each release is configured with name and timestamp of the release:
 * <p>
<pre>
&lt;Release timestamp="2008-06-10 00:00"&gt;
	&lt;Name&gt;Release 20080610&lt;/Name&gt;
&lt;/Release&gt;
</pre>
	 * @param file - file to load releases from
	 * @throws ConfigurationException - if an error occurs
	 */
	@Override
	protected void loadReleases(File file) throws ConfigurationException {
		String timestamp = null;
		try {
			XMLConfiguration config = new XMLConfiguration(file);
			List<?> timestamps = config.getList("Release[@timestamp]");
			Iterator<?> i = timestamps.iterator();
			int idx = 0;
			DateFormat parser = DefaultIssue.DATETIME_WITHOUT_SEC();
			while (i.hasNext()) {
				timestamp = (String)i.next();
				Date releaseDate = parser.parse(timestamp);
				String releaseName = config.getString("Release("+idx+").Name");
				if (releaseName == null) throw new ConfigurationException("No release name found for timestamp: "+timestamp);
				
				addRelease(new DefaultRelease(releaseName, releaseDate));
				idx++;
			}
		} catch (ParseException e) {
			throw new ConfigurationException("Invalid time information: "+timestamp);
		}
	}

}
