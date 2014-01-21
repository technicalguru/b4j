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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.configuration.ConfigurationException;

import b4j.core.DefaultIssue;

/**
 * Loads release information from a text file.
 * The text file contains one release information per line.
 * A line consists of the timestamp YYYY-MM-DD HH:MM followed by the release name.
 * Lines starting with a hash (#) will be ignored.
 * @author Ralph Schuster
 *
 */
public class TextFileReleaseProvider extends AbstractFileReleaseProvider {

	/**
	 * Default constructor.
	 */
	public TextFileReleaseProvider() {
	}

	/**
	 * Loads the text file.
	 * The text file contains one release information per line.
	 * A line consists of the timestamp YYYY-MM-DD HH:MM followed by the release name.
	 * Lines starting with a hash (#) will be ignored.
	 * @param file - file to load releases from
	 * @throws ConfigurationException - if an error occurs
	 */
	@SuppressWarnings("resource")
	@Override
	protected void loadReleases(File file) throws ConfigurationException {
		String line = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#")) continue;
				if (line.length() < 16) throw new ConfigurationException("Invalid release information: "+line);
				Date releaseDate = DefaultIssue.DATETIME_WITHOUT_SEC.parse(line.substring(0, 16));
				String releaseName = line.substring(16).trim();
				addRelease(new DefaultRelease(releaseName, releaseDate));
			}
		} catch (FileNotFoundException e) {
			throw new ConfigurationException("Cannot open file: "+file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new ConfigurationException("Cannot read file: "+file.getAbsolutePath(), e);
		} catch (ParseException e) {
			throw new ConfigurationException("Invalid release information: "+line);
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (Exception e) { }
		}
	}

}
