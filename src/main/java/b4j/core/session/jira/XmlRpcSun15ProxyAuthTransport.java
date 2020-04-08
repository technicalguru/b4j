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
package b4j.core.session.jira;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransport;

/**
 * A transport class for setting proxy authorization.
 * @author Ralph Schuster
 *
 */
public class XmlRpcSun15ProxyAuthTransport extends XmlRpcSun15HttpTransport {

	private String proxyUser;
	private String proxyPassword;
	
	public XmlRpcSun15ProxyAuthTransport(XmlRpcClient pClient) {
		super(pClient);
	}
	
	public void setProxyAuthorization(String proxyUser, String proxyPassword) {
		this.proxyUser = proxyUser;
		this.proxyPassword = proxyPassword;
	}
	
	/**
	 * @return the proxyUser
	 */
	public String getProxyUser() {
		return proxyUser;
	}

	/**
	 * @return the proxyPassword
	 */
	public String getProxyPassword() {
		return proxyPassword;
	}

	protected URLConnection newURLConnection(URL pURL) throws IOException {
		URLConnection rc = super.newURLConnection(pURL);
		if ((getProxy() != null) && (getProxyUser() != null) && (getProxyPassword() != null)) {
			String base64 = getProxyUser()+":"+getProxyPassword();
			rc.setRequestProperty("Proxy-Authorization", "Basic "+Base64.encodeBase64String(base64.getBytes(StandardCharsets.UTF_8)));
		}
		return rc;
	}
}

