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
package b4j.core.session;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;

import rs.baselib.configuration.IConfigurable;

/**
 * Implements authorization information retrieval from a plain text file.
 * @author Ralph Schuster
 *
 */
public class TextFileAuthorizationCallback extends AbstractAuthorizationCallback implements IConfigurable {

	/**
	 * Default Constructor.
	 */
	public TextFileAuthorizationCallback() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void beforeConfiguration() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void afterConfiguration() {
	}

	/**
	 * Configures the callback.
	 * Configuration takes place from a plain text filewhose
	 * path is contained within element &lt;File&gt;. 
	 * The text file itself must contain login and password 
	 * as its only content. Syntax is &lt;login&gt;:&lt;password&gt;
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		String path = config.getString("File");
		configure(path);
	}

	/**
	 * Configures the callback from the text file.
	 * @param file filename
	 * @throws ConfigurationException
	 */
	public void configure(String file) throws ConfigurationException {
		configure(new File(file));
	}
	
	/**
	 * Configures the callback from the text file.
	 * @param file file
	 * @throws ConfigurationException
	 */
	public void configure(File file) throws ConfigurationException {
		try {
			String s = FileUtils.readFileToString(file);
			int pos = s.indexOf(':');
			if (pos > 0) {
				setName(s.substring(0, pos));
				setPassword(s.substring(pos+1));
			} else {
				throw new ConfigurationException("Invalid text file format: "+file.getAbsolutePath());
			}
		} catch (IOException e) {
			throw new ConfigurationException("Cannot read file", e);
		}
	}
}
