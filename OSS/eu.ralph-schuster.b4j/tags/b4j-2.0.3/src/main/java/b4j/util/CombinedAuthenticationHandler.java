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

package b4j.util;

import com.atlassian.httpclient.api.Request;
import com.atlassian.jira.rest.client.AuthenticationHandler;

/**
 * Combines multiple {@link AuthenticationHandler}s in a chain.
 * @author ralph
 *
 */
public class CombinedAuthenticationHandler implements AuthenticationHandler {

	private AuthenticationHandler handlers[];
	
	/**
	 * Constructor.
	 */
	public CombinedAuthenticationHandler(AuthenticationHandler... handlers) {
		this.handlers = handlers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void configure(Request request) {
		for (AuthenticationHandler handler : handlers) {
			if (handler != null) handler.configure(request);
		}
	}

}
