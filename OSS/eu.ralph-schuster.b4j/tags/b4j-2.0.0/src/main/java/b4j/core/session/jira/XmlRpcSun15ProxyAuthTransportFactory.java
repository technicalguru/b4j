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

import java.net.Proxy;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransportFactory;
import org.apache.xmlrpc.client.XmlRpcTransport;

/**
 * Factory for proxy authorization transports.
 * @author Ralph Schuster
 *
 */
public class XmlRpcSun15ProxyAuthTransportFactory extends XmlRpcSun15HttpTransportFactory {

	private String proxyUser;
	private String proxyPassword;
	private Proxy proxy;
	
	public XmlRpcSun15ProxyAuthTransportFactory(XmlRpcClient pClient) {
		super(pClient);
	}
	
	public void setProxyAuthorization(String proxyUser, String proxyPassword) {
		this.proxyUser = proxyUser;
		this.proxyPassword = proxyPassword;
	}
	
	/**
     * Sets the proxy to use.
     * @param pProxy The proxy settings.
     */
    public void setProxy(Proxy pProxy) {
    	super.setProxy(pProxy);
        proxy = pProxy;
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

	/**
	 * @return the proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	public XmlRpcTransport getTransport() {
		XmlRpcSun15ProxyAuthTransport transport = new XmlRpcSun15ProxyAuthTransport(getClient());
		transport.setSSLSocketFactory(getSSLSocketFactory());
		transport.setProxy(getProxy());
		if (getProxy() != null) transport.setProxyAuthorization(getProxyUser(), getProxyPassword());
		return transport;
	}

}
