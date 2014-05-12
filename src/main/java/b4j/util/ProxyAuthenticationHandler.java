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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;

import com.atlassian.httpclient.api.Request;
import com.atlassian.jira.rest.client.AuthenticationHandler;

/**
 * Adds Proxy authentication header.
 * @author ralph
 *
 */
public class ProxyAuthenticationHandler implements AuthenticationHandler {

	private static final String AUTHORIZATION_HEADER = "Proxy-Authorization";

	private String username;
	private String password;

	/**
	 * Constructor.
	 */
	public ProxyAuthenticationHandler(String username, String password) {
		this.username = username;
		this.password = password;
	}


	@Override
	public void configure(final Request request) {
		request.setHeader(AUTHORIZATION_HEADER, "Basic " + encodeCredentials());
	}

	private String encodeCredentials() {
		byte[] credentials = (username + ':' + password).getBytes(Charsets.UTF_8);
		return Base64.encodeBase64String(credentials);
	}


}
