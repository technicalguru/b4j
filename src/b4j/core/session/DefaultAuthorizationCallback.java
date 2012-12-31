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

/**
 * Implements authorization information retrieval from configuration object.
 * This implementation can be used to directly store authorization information
 * in a global config without loading extra information from other files.
 * @author Ralph Schuster
 *
 */
public class DefaultAuthorizationCallback extends AbstractAuthorizationCallback {

	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = 2658372457661630519L;


	/**
	 * Default Constructor.
	 */
	public DefaultAuthorizationCallback() {
	}

	/**
	 * Configures the callback.
	 * Configuration object must must contain two elements &lt;login&gt;
	 * and &lt:password&gt;.
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		setName(config.getString("login"));
		setPassword(config.getString("password"));
	}

}
