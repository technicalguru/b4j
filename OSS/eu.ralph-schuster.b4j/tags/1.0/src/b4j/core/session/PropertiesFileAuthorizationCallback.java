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

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Implements authorization information retrieval from a properties file.
 * @author Ralph Schuster
 *
 */
public class PropertiesFileAuthorizationCallback extends DefaultAuthorizationCallback {

	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = 6225987967365107756L;

	/**
	 * Default Constructor.
	 */
	public PropertiesFileAuthorizationCallback() {
	}

	/**
	 * Configures the callback.
	 * Configuration takes place from a Properties file whose
	 * path is contained within element &lt;File&gt;.
	 * The properties file itself must contain values for "login" 
	 * and "password" properties.
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		String path = config.getString("File");
		PropertiesConfiguration pConfig = new PropertiesConfiguration(path);
		super.configure(pConfig);
	}

}
