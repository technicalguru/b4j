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

/**
 * Implements authorization information retrieval from a plain text file.
 * @author Ralph Schuster
 *
 */
public class TextFileAuthorizationCallback extends AbstractAuthorizationCallback {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -8555012260470161703L;

	/**
	 * Default Constructor.
	 */
	public TextFileAuthorizationCallback() {
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
		try {
			String path = config.getString("File");
			String s = FileUtils.readFileToString(new File(path));
			int pos = s.indexOf(':');
			if (pos > 0) {
				setName(s.substring(0, pos));
				setPassword(s.substring(pos+1));
			} else {
				throw new ConfigurationException("Invalid text file format: "+path);
			}
		} catch (IOException e) {
			throw new ConfigurationException("Cannot read file", e);
		}
	}

}
