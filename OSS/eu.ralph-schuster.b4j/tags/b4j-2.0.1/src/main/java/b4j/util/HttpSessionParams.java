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

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;

import rs.baselib.configuration.ConfigurationUtils;
import rs.baselib.configuration.IConfigurable;
import rs.baselib.security.AuthorizationCallback;
import rs.baselib.security.DefaultAuthorizationCallback;

/**
 * Helps in configuring and retrieving HTTP session config information.
 * @author ralph
 *
 */
public class HttpSessionParams implements IConfigurable {

	private AuthorizationCallback authorizationCallback;
	private Proxy proxy = null;
	private String proxyHost = null;
	private int proxyPort = -1;
	private AuthorizationCallback proxyAuthorizationCallback = null;
	private boolean basicAuthentication = false;

	/**
	 * Constructor.
	 */
	public HttpSessionParams() {
	}

	@Override
	public void configure(Configuration config) throws ConfigurationException {
		String className = null;

		Configuration authCfg = null;

		// Target Authentication
		try {
			authCfg = ((HierarchicalConfiguration)config).configurationAt("AuthorizationCallback(0)");
		} catch (IllegalArgumentException e) {}

		if (authCfg != null) {
			className = authCfg.getString("[@class]");
			if ((className == null) || (className.trim().length() == 0) 
					|| className.toLowerCase().trim().equals("null")
					|| className.toLowerCase().trim().equals("nil")) {
				className = DefaultAuthorizationCallback.class.getName();
			}
			setAuthorizationCallback((AuthorizationCallback)ConfigurationUtils.load(className, authCfg, true));
		}

		// Proxy Authentication
		String proxyDef = null;
		try {
			proxyDef = config.getString("proxy-host");
			if (proxyDef != null) {
				String s[] = proxyDef.split(":", 2);
				proxyHost = s[0];
				String port = "80";
				if (s.length > 1) port = s[1];
				proxyPort = Integer.parseInt(port);
				
				authCfg = ((HierarchicalConfiguration)config).configurationAt("ProxyAuthorizationCallback(0)");
				if (authCfg != null) {
					className = authCfg.getString("[@class]");
					if ((className == null) || (className.trim().length() == 0) 
							|| className.toLowerCase().trim().equals("null")
							|| className.toLowerCase().trim().equals("nil")) {
						className = DefaultAuthorizationCallback.class.getName();
					}
					setProxyAuthorizationCallback((AuthorizationCallback)ConfigurationUtils.load(className, authCfg, true));
				}
			}
		} catch (Exception e) {
			// No proxy
		}

	}

	/**
	 * Returns the authorizationCallback.
	 * @return the authorizationCallback
	 */
	public AuthorizationCallback getAuthorizationCallback() {
		return authorizationCallback;
	}

	/**
	 * Sets the authorizationCallback.
	 * @param authorizationCallback the authorizationCallback to set
	 */
	public void setAuthorizationCallback(AuthorizationCallback authorizationCallback) {
		this.authorizationCallback = authorizationCallback;
	}

	/**
	 * Returns true when the session authentication was defined.
	 * @return <code>true</code> when a session authentication was defined
	 */
	public boolean hasAuthorization() {
		return getAuthorizationCallback() != null;
	}

	/**
	 * Returns the login name as told by the callback handler.
	 * @return login name
	 */
	public String getLogin() {
		if (getAuthorizationCallback() == null) return null;
		return getAuthorizationCallback().getName();
	}

	/**
	 * Returns the password as told by the callback handler.
	 * @return the password
	 */
	public String getPassword() {
		if (getAuthorizationCallback() == null) return null;
		return getAuthorizationCallback().getPassword();
	}

	/**
	 * Returns true when the proxy was defined.
	 * @return <code>true</code> when a proxy was defined
	 */
	public boolean hasProxy() {
		return getProxyHost() != null;
	}

	/**
	 * Returns the proxy hostname of <code>null</code> if no proxy was defined.
	 * @return the proxy host
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * Returns the proxy port of <code>-1</code> if no proxy was defined.
	 * @return the proxy port
	 */
	public int getProxyPort() {
		return proxyPort;
	}

	/**
	 * Returns true when the proxy authentication was defined.
	 * @return <code>true</code> when a proxy authentication was defined
	 */
	public boolean hasProxyAuthentication() {
		return hasProxy() && (getProxyUser() != null);
	}

	/**
	 * Returns the proxy user of <code>null</code> if no proxy was defined.
	 * @return the proxy user
	 */
	public String getProxyUser() {
		if (getProxyAuthorizationCallback() != null) return getProxyAuthorizationCallback().getName(); 
		return null;
	}

	/**
	 * Returns the proxy password of <code>null</code> if no proxy was defined.
	 * @return the proxy password
	 */
	public String getProxyPassword() {
		if (getProxyAuthorizationCallback() != null) return getProxyAuthorizationCallback().getPassword(); 
		return null;
	}

	/**
	 * Sets the proxyHost.
	 * @param proxyHost the proxyHost to set
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
		this.proxy = null;
	}

	/**
	 * Sets the proxyPort.
	 * @param proxyPort the proxyPort to set
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
		this.proxy = null;
	}

	
	/**
	 * Returns the proxyAuthorizationCallback.
	 * @return the proxyAuthorizationCallback
	 */
	public AuthorizationCallback getProxyAuthorizationCallback() {
		return proxyAuthorizationCallback;
	}

	/**
	 * Sets the proxyAuthorizationCallback.
	 * @param proxyAuthorizationCallback the proxyAuthorizationCallback to set
	 */
	public void setProxyAuthorizationCallback(AuthorizationCallback proxyAuthorizationCallback) {
		this.proxyAuthorizationCallback = proxyAuthorizationCallback;
	}

	/**
	 * Returns the basicAuthentication.
	 * @return the basicAuthentication
	 */
	public boolean isBasicAuthentication() {
		return basicAuthentication;
	}

	/**
	 * Sets the basicAuthentication.
	 * @param basicAuthentication the basicAuthentication to set
	 */
	public void setBasicAuthentication(boolean basicAuthentication) {
		this.basicAuthentication = basicAuthentication;
	}

	/**
	 * Returns the proxy object or <code>null</code> if no proxy is present.
	 * @return the proxy object
	 */
	public Proxy getProxy() {
		if (hasProxy()) {
			if (proxy == null) {
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(getProxyHost(), getProxyPort()));
			}
			return proxy;
		}
		return null;
	}
}
