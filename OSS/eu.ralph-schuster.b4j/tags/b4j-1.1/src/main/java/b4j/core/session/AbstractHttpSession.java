/**
 * 
 */
package b4j.core.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
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
import org.apache.commons.configuration.HierarchicalConfiguration;

import b4j.core.Configurable;

/**
 * Implements an abstract HTTP session including usage with proxies.
 * @author Ralph Schuster
 *
 */
public abstract class AbstractHttpSession extends AbstractAuthorizedSession {

	private boolean loggedIn;
	private Set<HttpCookie> cookies;
	private URL baseUrl;
	private String bugzillaVersion;
	private Proxy proxy;
	private AuthorizationCallback proxyAuthorizationCallback;
	
	/**
	 * 
	 */
	public AbstractHttpSession() {
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
		String className = null;
		try {
			baseUrl = new URL(config.getString("bugzilla-home"));
			
			if (getLog().isDebugEnabled()) {
				getLog().debug("Bugzilla URL: "+baseUrl);
			}
			
			// proxy configuration
			String s = config.getString("proxy-host");
			if (s != null) {
				if (getLog().isDebugEnabled()) {
					getLog().debug("Using HTTP Proxy: "+s);
				}
				
				int pos = -1;
				int port = 80;
				pos = s.indexOf(':');
				if (pos > 0) {
					port = Integer.parseInt(s.substring(pos+1));
					s = s.substring(0, pos);
				}
				setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(s, port)));
				
				// Proxy authorization
				try {
					Configuration authCfg = ((HierarchicalConfiguration)config).configurationAt("ProxyAuthorizationCallback(0)");
					if (authCfg != null) {
						className = authCfg.getString("[@class]");
						AuthorizationCallback callback = null;
						if ((className == null) 
								|| (className.trim().length() == 0) 
								|| className.toLowerCase().trim().equals("null")
								|| className.toLowerCase().trim().equals("nil")) {
							callback = new DefaultAuthorizationCallback();
						} else {
							Class<?> c = Class.forName(className);
							callback = (AuthorizationCallback)c.newInstance();
						}
						if (callback instanceof Configurable) {
							((Configurable)callback).configure(authCfg);
						}
						setProxyAuthorizationCallback(callback);
					}
				} catch (IllegalArgumentException e) {
					// No auth config for proxy
				}

			}
		} catch (MalformedURLException e) {
			throw new ConfigurationException("Malformed Bugzilla URL: ", e);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException("Cannot find class: "+className, e);
		} catch (InstantiationException e) {
			throw new ConfigurationException("Cannot instantiate class: "+className, e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Cannot access constructor: "+className, e);
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
	 * @return the proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * @param proxy the proxy to set
	 */
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	/**
	 * @return the proxyAuthorizationCallback
	 */
	public AuthorizationCallback getProxyAuthorizationCallback() {
		return proxyAuthorizationCallback;
	}

	/**
	 * @param proxyAuthorizationCallback the proxyAuthorizationCallback to set
	 */
	public void setProxyAuthorizationCallback(AuthorizationCallback proxyAuthorizationCallback) {
		this.proxyAuthorizationCallback = proxyAuthorizationCallback;
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
			if (proxy != null) {
				getLog().debug("Using proxy: "+getProxy());
				con = (HttpURLConnection)url.openConnection(getProxy());
				AuthorizationCallback callback = getProxyAuthorizationCallback();
				if (callback != null) {
					// apply proxy authorization
					// "Proxy-Authorization: Basic "<base64(user:password)>"
					// commons.codec delivers Base64 Encoder
					String authName = callback.getName();
					String authPass = callback.getPassword();
					if (authPass == null) authPass = "";
					String auth = authName + ':' + authPass;
					auth = new String(Base64.encodeBase64(auth.getBytes()));
					con.setRequestProperty("Proxy-Authorization", "Basic "+auth+"");
				}
			} else {
				con = (HttpURLConnection)url.openConnection();
			}
			
			// Apply all cookies if available
			applyCookies(con);
			
			// Apply request properties
			if (requestProperties != null) {
				for (String key : requestProperties.keySet()) {
					con.setRequestProperty(key, requestProperties.get(key));
				}
			}
			
			// Apply POST parameters
			if (!isGet) {
				if (params == null) params = "";
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				con.setRequestProperty("Content-Length", "" + params.length());

				con.setDoOutput(true);
				PrintWriter out = new PrintWriter(con.getOutputStream());
				out.print(params);
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
				List<HttpCookie> cookies = HttpCookie.parse("Set-Cookie: "+cookieHeader.next());
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
			Iterator<String> i = headerFields.keySet().iterator();
			while (i.hasNext()) {
				String key = i.next();
				Iterator<String> j = headerFields.get(key).iterator();
				while (j.hasNext()) getLog().debug(key+": "+j.next());
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
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