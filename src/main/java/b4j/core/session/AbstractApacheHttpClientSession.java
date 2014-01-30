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

import org.apache.http.client.HttpClient;

import b4j.util.HttpClients;


/**
 * A base class for authorized sessions using Apache's {@link HttpClient}.
 * @author ralph
 * @since 2.0
 *
 */
public abstract class AbstractApacheHttpClientSession extends AbstractHttpSession {

	private HttpClient httpClient = null;
	
	/**
	 * Constructor.
	 */
	public AbstractApacheHttpClientSession() {
	}

	/**
	 * Returns a read-to-use HTTP client based on Apache's {@link HttpClient}.
	 * @return the client already configured with authentication.
	 */
	protected HttpClient getHttpClient() {
		if (this.httpClient == null) {
			this.httpClient = HttpClients.createApacheClient(getHttpSessionParams(), getLog());
		}
		return this.httpClient;
	}
	
	
}
