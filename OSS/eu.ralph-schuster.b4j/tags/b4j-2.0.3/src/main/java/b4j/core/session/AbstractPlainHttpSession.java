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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.Charsets;

import rs.baselib.lang.LangUtils;
import rs.baselib.security.AuthorizationCallback;

/**
 * Implements an abstract HTTP session including usage with proxies.
 * <p>Configuration:</p>
 * <pre>
 * &lt;bugzilla-session class="..."&gt;
 * 
 *    &lt;!-- The home URL of Bugzilla --&gt;
 *    &lt;bugzilla-home&gt;http://your-bugzilla.your-domain.com/&lt;/bugzilla-home&gt;
 *    
 *    &lt;!-- Optional: AuthenticationCallback implementation --&gt;
 *    &lt;AuthorizationCallback class="..."&gt;
 *       ...
 *    &lt;/AuthorizationCallback&gt;
 *    
 *    &lt;!-- Issue implementation class --&gt;
 *    &lt;Issue class="b4j.core.DefaultIssue"/&gt;
 *    
 *    &lt;!-- Optional: Proxy definition --&gt;
 *    &lt;proxy-host&gt;10.10.10.250:8080&lt;/proxy-host&gt;
 *    
 *    &lt;!-- Optional: Proxy AuthenticationCallback implementation --&gt;
 *    &lt;ProxyAuthorizationCallback class="..."&gt;
 *       ...
 *    &lt;/ProxyAuthorizationCallback&gt;
 *
 * &lt;/bugzilla-session&gt;
 * </pre>
 * @author Ralph Schuster
 * @see AuthorizationCallback
 * @see b4j.core.Issue
 * @since 2.0
 */
public abstract class AbstractPlainHttpSession extends AbstractHttpSession {

	private boolean loggedIn;
	private Set<HttpCookie> cookies;
	private URL baseUrl;
	private String bugzillaVersion;
	
	/**
	 * Constructor.
	 */
	public AbstractPlainHttpSession() {
		cookies = new HashSet<HttpCookie>();
	}

	/**
	 * Configures the session.
	 * The method is called to initialize the session object from a configuration.
	 * The configuration object must ensure to contain the elements "login",
	 * "password" and "bugzilla-home" to contain account information and
	 * HTTP URL. 
	 * <p>
	 * A proxy can be configured by using "proxy-host" element. Use the colon
	 * to separate host and port information.
	 * </p>
	 * <p>
	 * Proxy authorization is configured by element &lt;ProxyAuthorizationCallback&gt;. It
	 * defines the implementation class that will deliver login name and password.
	 * </p>
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		super.configure(config);
		try {
			baseUrl = new URL(config.getString("bugzilla-home"));
			
			if (getLog().isDebugEnabled()) {
				getLog().debug("Bugzilla URL: "+baseUrl);
			}
			
		} catch (MalformedURLException e) {
			throw new ConfigurationException("Malformed Bugzilla URL: ", e);
		}
	}
	
	/**
	 * Closes the previously established Bugzilla session.
	 */
	@Override
	public void close() {
		cookies.clear();
		baseUrl = null;
		setLoggedIn(false);
		if (getLog().isInfoEnabled()) getLog().info("Session closed");
	}
	
	/**
	 * @return the baseUrl
	 */
	public URL getBaseUrl() {
		return baseUrl;
	}

	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(URL baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Returns the bugzilla version this session object is connected to.
	 * Can return null if version is unknown.
	 * @return version number or null if unknown or not connected
	 */
	@Override
	public String getBugzillaVersion() {
		return bugzillaVersion;
	}

	/**
	 * Sets the Bugzilla version connected to.
	 * @param s new version
	 */
	protected void setBugzillaVersion(String s) {
		bugzillaVersion = s;
	}
	
	/**
	 * Returns true when session is connected to Bugzilla.
	 * @return true if session was successfully established, false otherwise
	 */
	@Override
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * Sets the login status.
	 * @param loggedIn - the login status to set
	 */
	protected void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
		if (loggedIn) getLog().info("Session opened: "+getBaseUrl().toString());
	}

	/**
	 * Returns all cookies from the Bugzilla session
	 * @return iterator on all cookies
	 */
	public Iterator<HttpCookie> getCookies() {
		return cookies.iterator();
	}

	/**
	 * Adds a cookie to the session.
	 * @param cookie - the cookie to add
	 */
	protected void addCookie(HttpCookie cookie) {
		cookies.add(cookie);
		if (getLog().isDebugEnabled()) getLog().debug("new cookie found: "+cookie.toString());
	}

	/**
	 * Makes a request to Bugzilla including eventual GET parameters.
	 * The method applies all cookies registered previously to allow
	 * a successful session connection.
	 * @param urlPath URL path to request
	 * @param params param string to be appended
	 * @return HTTP connection object
	 */
	protected HttpURLConnection getConnection(String urlPath, String params) {
		return getConnection(urlPath, params, null, true);
	}
	
	/**
	 * Makes a request to Bugzilla including eventual GET parameters.
	 * The method applies all cookies registered previously to allow
	 * a successful session connection.
	 * @param urlPath URL path to request
	 * @param params param string to be appended
	 * @param requestProperties properties of the request
	 * @return HTTP connection object
	 */
	protected HttpURLConnection getConnection(String urlPath, String params, Map<String,String> requestProperties) {
		return getConnection(urlPath, params, requestProperties, true);
	}
	
	/**
	 * Makes a request to Bugzilla including eventual GET parameters.
	 * The method applies all cookies registered previously to allow
	 * a successful session connection.
	 * @param urlPath URL path to request
	 * @param params param string to be appended
	 * @param requestProperties properties of the request
	 * @param isGet true when a GET request shall be prepared, false if this will be a POST request.
	 * @return HTTP connection object
	 */
	protected HttpURLConnection getConnection(String urlPath, String params, Map<String,String> requestProperties, boolean isGet) {
		try {
			URL url = null;
			if (isGet) {
				if (params == null) params = "";
				else params = "?" + params;
				url = new URL(urlPath+params);
			} else {
				url = new URL(urlPath);
			}
			if (getLog().isDebugEnabled()) getLog().debug(url.toString());
			
			// apply proxy
			HttpURLConnection con = null;
			if (getHttpSessionParams().hasProxy()) {
				getLog().debug("Using proxy: "+getHttpSessionParams().getProxy());
				con = (HttpURLConnection)url.openConnection(getHttpSessionParams().getProxy());
				AuthorizationCallback callback = getHttpSessionParams().getProxyAuthorizationCallback();
				if (callback != null) {
					// apply proxy authorization
					// "Proxy-Authorization: Basic "<base64(user:password)>"
					// commons.codec delivers Base64 Encoder
					String authName = callback.getName();
					String authPass = callback.getPassword();
					if (authPass == null) authPass = "";
					String auth = authName + ':' + authPass;
					auth = Base64.encodeBase64String(auth.getBytes(Charsets.UTF_8));
					con.setRequestProperty("Proxy-Authorization", "Basic "+auth+"");
				}
			} else {
				con = (HttpURLConnection)url.openConnection();
			}
			
			// Apply all cookies if available
			applyCookies(con);
			
			// Apply request properties
			if (requestProperties != null) {
				for (Map.Entry<String,String> entry : requestProperties.entrySet()) {
					con.setRequestProperty(entry.getKey(), entry.getValue());
				}
			}
			
			// Apply POST parameters
			if (!isGet) {
				if (params == null) params = "";
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				con.setRequestProperty("Content-Length", "" + params.length());

				con.setDoOutput(true);
				Writer out = new OutputStreamWriter(con.getOutputStream(), Charsets.UTF_8);
				out.write(params);
				out.flush();
				out.close();
			}
			
			return con;
		} catch (MalformedURLException e) {
			getLog().error("Invalid URL: ", e);
		} catch (IOException e) {
			getLog().error("Error when making request", e);
		}
		return null;
	}
	
	/**
	 * Retrieves and saves the cookies from the URL connection.
	 * @param con - the HTTP connection
	 * @return true when cookies were found, false otherwise
	 * @throws MalformedURLException - when the URL had a problem
	 */
	protected boolean retrieveCookies(URLConnection con) throws MalformedURLException {
		List<String> cookieHeaders = con.getHeaderFields().get("Set-Cookie");
		
		if (cookieHeaders != null) {
			Iterator<String> cookieHeader = cookieHeaders.iterator();
			while (cookieHeader.hasNext()) {
				String header = cookieHeader.next();
				// Fix #31 : Java 6 cannot understand HttpOnly attribute
				if (LangUtils.isJava6()) {
					header = header.replaceAll("(?i);?\\s*httponly", "");
				}
				List<HttpCookie> cookies = HttpCookie.parse("Set-Cookie: "+header);
				Iterator<HttpCookie> i = cookies.iterator();
				while (i.hasNext()) {
					HttpCookie cookie = i.next();
					String domain = cookie.getDomain();
					if ((domain == null) || HttpCookie.domainMatches(domain, baseUrl.getHost())) {
						addCookie(cookie);
					} else if (getLog().isDebugEnabled()) {
						getLog().debug("Cookie not applicable: "+cookie.toString());
					}
				}
			}
			return cookies.size() > 0;
		}
		return false;
	}
	
	/**
	 * Applies the registered cookies with this connection.
	 * @param con - HTTP connection object
	 */
	protected void applyCookies(HttpURLConnection con) {
		StringBuffer rc = new StringBuffer();
		Iterator<HttpCookie> cookies = getCookies();
		while (cookies.hasNext()) {
			if (rc.length() > 0) rc.append(";");
			rc.append(cookies.next().toString());
		}
		if (rc.length() > 0) con.setRequestProperty("Cookie", rc.toString());
		if (getLog().isTraceEnabled()) getLog().trace("applied cookie: "+rc.toString());

	}
	
	/**
	 * Debugs the response from a connection.
	 * All headers and the content will be dumped into the log.
	 * @param con - HTTP connection to dump
	 */
	public void debugResponse(HttpURLConnection con) {
		if (!getLog().isDebugEnabled()) return;
		try {
			Map<String,List<String>> headerFields = con.getHeaderFields();
			for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
				String key = entry.getKey();
				for (String value : entry.getValue()) {
					getLog().debug(key+": "+value);
				}
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), Charsets.UTF_8));
			String line;
			while ((line = r.readLine()) != null) {
				getLog().debug(line);
			}
		} catch (IOException e) {
			getLog().error("Error while debugging connection", e);
		}
	}
	
	/**
	 * Debugs information into log.
	 */
	@Override
	public void dump() {
		if (!getLog().isDebugEnabled()) return;
		getLog().debug("bugzilla-home="+getBaseUrl().toString());
		super.dump();
	}
	

}
