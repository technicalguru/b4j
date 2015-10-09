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

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

/**
 * Abstract implementations for release providers that load release information
 * from external files.
 * The main purpose of this class is to allow sub-classes to load their release
 * information from an external file, such as property files, text files or XML files.
 * This allows you to flexible configure the release information without having
 * the main configuration to be changed.
 * @author Ralph Schuster
 *
 */
public abstract class AbstractFileReleaseProvider extends DefaultReleaseProvider {

	/**
	 * Default Constructor.
	 */
	public AbstractFileReleaseProvider() {
	}
	
	/**
	 * Configures the provider.
	 * The method will read all information from the file given as configuration
	 * element &lt;File&gt;.
	 * <p>
	 * You can allow loading releases directly from configuration as parent class
	 * {@link DefaultReleaseProvider} does by setting element &lt;ignoreConfigReleases&gt;
	 * to false. The default behaviour is to ignore all releases mentioned in
	 * the configuration object but loading all from the external file.
	 * </p>
	 * <p>
	 * Release information in external files always takes precedence over configured
	 * information.
	 * </p>
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		// check which configuration takes precedence
		String s = config.getString("ignoreConfigReleases(0)");
		if (s == null) s = "true";
		boolean ignoreConfigReleases = Boolean.parseBoolean(s);
		
		// load the filename from configuration
		String filename = config.getString("File(0)");
		if (filename == null) throw new ConfigurationException("No File element given for release information.");
		
		// Load the releases
		File f = new File(filename);
		loadReleases(f);
		
		// configure the other config based releases if required
		if (!ignoreConfigReleases) super.configure(config);
	}
	
	/**
	 * Loads the releases.
	 * @param file - file to load releases from
	 * @throws ConfigurationException - if an error occurs
	 */
	protected abstract void loadReleases(File file) throws ConfigurationException;
}
