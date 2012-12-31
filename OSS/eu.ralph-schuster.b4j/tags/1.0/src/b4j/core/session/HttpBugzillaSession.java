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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;

import b4j.core.Attachment;
import b4j.core.BugzillaBug;
import b4j.core.BugzillaSearchData;
import b4j.core.DefaultBugzillaBug;
import b4j.core.LongDescription;
import b4j.core.SearchResultCountCallback;
import b4j.util.UrlParameters;


/**
 * Implements Bugzilla access via HTTP. Use this session object
 * when you cannot access Bugzilla directly per JDBC.
 * This implementation does not implement access via proxy yet.
 * @author Ralph Schuster
 *
 */
public class HttpBugzillaSession extends AbstractAuthorizedSession {

	private static Log log = LogFactory.getLog(HttpBugzillaSession.class);
	
	/** Constant for requesting URL connection to login page */
	public static final int BUGZILLA_LOGIN = 0;
	/** Constant for requesting URL connection to search result page */
	public static final int BUGZILLA_SEARCH = 1;
	/** Constant for requesting URL connection to logout page */
	public static final int BUGZILLA_LOGOUT = 2;
	/** Constant for requesting URL connection to show bug page */
	public static final int BUGZILLA_SHOW_BUG = 3;
	
	private static final String MINIMUM_BUGZILLA_VERSION = "2.20";
	private static final String MAXIMUM_BUGZILLA_VERSION = "3.0.999";
	
	/** The URL paths to the Bugzilla pages */
	protected static final String[] PAGES = new String[] {
		"/index.cgi",
		"/buglist.cgi",
		"/relogin.cgi",
		"/show_bug.cgi",
	};
	private static UrlParameters DEFAULT_SEARCH_PARAMETERS;
	
	private boolean loggedIn;
	private Set<HttpCookie> cookies;
	private URL baseUrl;
	private String bugzillaVersion;
	private Proxy proxy;
	private AuthorizationCallback proxyAuthorizationCallback;
	
	/**
	 * Default constructor.
	 */
	public HttpBugzillaSession() {
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
			
			// proxy configuration
			String s = config.getString("proxy-host");
			if (s != null) {
				int pos = -1;
				int port = 80;
				pos = s.indexOf(':');
				if (pos > 0) {
					port = Integer.parseInt(s.substring(pos+1));
					s = s.substring(0, pos);
				}
				proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(s, port));

				// Proxy authorization
				Configuration authCfg = ((SubnodeConfiguration)config).configurationAt("ProxyAuthorizationCallback(0)");
				if (authCfg != null) {
					className = authCfg.getString("[@class]");
					if ((className.trim().length() == 0) 
							|| className.toLowerCase().trim().equals("null")
							|| className.toLowerCase().trim().equals("nil")) {
						proxyAuthorizationCallback = new DefaultAuthorizationCallback();
					} else {
						Class<?> c = Class.forName(className);
						proxyAuthorizationCallback = (AuthorizationCallback)c.newInstance();
					}
					proxyAuthorizationCallback.configure(authCfg);
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
	 * Returns the bugzilla version this session object is connected to.
	 * Can return null if version is unknown.
	 * @return version number or null if unknown or not connected
	 */
	@Override
	public String getBugzillaVersion() {
		return bugzillaVersion;
	}

	/**
	 * Returns the minimum Bugzilla version this session class supports.
	 * @see #MINIMUM_BUGZILLA_VERSION
	 * @return minimum version of supported Bugzilla software.
	 */
	@Override
	public String getMinimumBugzillaVersion() {
		return MINIMUM_BUGZILLA_VERSION;
	}

	/**
	 * Returns the maximum Bugzilla version this session class supports.
	 * @see #MAXIMUM_BUGZILLA_VERSION
	 * @return maximum version of supported Bugzilla software.
	 */
	@Override
	public String getMaximumBugzillaVersion() {
		return MAXIMUM_BUGZILLA_VERSION;
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
		if (log.isDebugEnabled()) log.debug("new cookie found: "+cookie.toString());
	}

	/**
	 * Opens the session with configured Bugzilla instance.
	 * @return true when session could be established successfully, false otherwise
	 */
	@Override
	public boolean open() {
		if (isLoggedIn()) return true;
		bugzillaVersion = null;
		
		// Exception: No login required
		if (getLogin() == null) {
			log.debug("No Auth required");
			setLoggedIn(true);
			return true;
		}
		
		try {
			// Bugzilla_login = xxx
			// Bugzilla_password = xxx
			// GoAheadAndLogIn = Login
			UrlParameters params = new UrlParameters();
			params.setParameter("Bugzilla_login", getLogin());
			params.setParameter("Bugzilla_password", getPassword());
			params.setParameter("GoAheadAndLogIn", "Login");
			String paramString = params.getUrlEncodedString();
			
			
			// make a connection
			HttpURLConnection con = getConnection(BUGZILLA_LOGIN);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Length", "" + paramString.length());
			con.setDoOutput(true);
			PrintWriter out = new PrintWriter(con.getOutputStream());
			out.print(paramString);
			out.flush();
			out.close();
			
			// Read the response;
			if (con.getResponseCode() == 200) {
				boolean rc = retrieveCookies(con);
				
				// Get Bugzilla version and test for compatibility
				BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
				Pattern p = Pattern.compile(".*version\\s+([\\d\\.]+).*", Pattern.CASE_INSENSITIVE);

				String line;
				while ((line = r.readLine()) != null) {
					// Search for: version 3.0.5
					Matcher m = p.matcher(line);
					if (m.matches()) {
						bugzillaVersion = m.group(1);
						break;
					}
				}
				
				setLoggedIn(rc);
				if (log.isInfoEnabled()) {
					if (rc) {
						log.info("Session opened:   "+baseUrl);
						if (log.isDebugEnabled()) log.debug("Bugzilla-Version: "+bugzillaVersion);
					} else log.info("Bugzilla did not sent Cookie");
				}
				
				if (rc) checkBugzillaVersion();
				
				return rc;
			}
		} catch (IOException e) {
			log.error("Cannot open session", e);
		}
		return false;
	}
	
	/**
	 * Closes the previously established Bugzilla session.
	 */
	@Override
	public void close() {
		if (!isLoggedIn()) return;
		
		// Only when login was required
		if (getLogin() != null) {
			try {
				// make a connection
				HttpURLConnection con = getConnection(BUGZILLA_LOGOUT);
				con.getResponseCode();
			} catch (IOException e) {

			}
		}
		cookies.clear();
		baseUrl = null;
		setLoggedIn(false);
		if (log.isInfoEnabled()) log.info("HttpBugzillaSession closed");
	}
	
	/**
	 * Performs a search for Bugzilla bugs.
	 * This method returns an iterator over all bug records found. The returned
	 * iterator will query its data when the first call to its {@link Iterator#next()}
	 * method is made. A separate thread will then be spawned to retrieve the
	 * required details.
	 * @param searchData - all search parameters
	 * @param callback - a callback object that will retrieve the number of bugs 
	 * found for this search
	 * @return iterator on all bugs fulfilling the criteria expressed by search parameters.
	 */
	@Override
	public Iterator<BugzillaBug> searchBugs(BugzillaSearchData searchData, SearchResultCountCallback callback) {
		if (!isLoggedIn()) return null;
		
		// Perform the search
		try {
			UrlParameters params = UrlParameters.createUrlParameters(searchData);
			params.addDefaultParameters(getDefaultSearchParameters());
			String paramString = params.getUrlEncodedString();
			
			// make a connection
			HttpURLConnection con = getConnection(BUGZILLA_SEARCH, paramString);
			
			// Read the response;
			if (con.getResponseCode() == 200) {
				//debugResponse(con);
				//if (true) return null;
				
				List<String> idList = new ArrayList<String>();
				
				// Parse the data for all bugs found
				BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
				Pattern p = Pattern.compile(".*href=\"show_bug\\.cgi\\?id=(\\d+)\">.*");

				String line;
				while ((line = r.readLine()) != null) {
					// Search for: <a href="show_bug.cgi?id=2349">2349</a>
					Matcher m = p.matcher(line);
					if (m.matches()) {
						idList.add(m.group(1));
					} //else if (line.indexOf("show_bug.cgi") >= 0) log.info(line);
				}
				if (callback != null) callback.setResultCount(idList.size());
				if (log.isInfoEnabled()) log.info("Found "+idList.size()+" bugs");
				
				// Return the bug iterator
				return new BugzillaBugIterator(idList);
			} else {
				log.debug("Response invalid: "+con.getResponseCode());
			}
		} catch (IOException e) {
			log.error("Cannot perform search", e);
		}
		
		return null;
	}
	
	/**
	 * Makes a request to Bugzilla without any GET parameters.
	 * @param bugzillaPage - ID of page to make the request to.
	 * @return HTTP connection object
	 */
	protected HttpURLConnection getConnection(int bugzillaPage) {
		return getConnection(bugzillaPage, null);
	}
	
	/**
	 * Makes a request to Bugzilla including eventual GET parameters.
	 * The method applies all cookies registered previously to allow
	 * a successful session connection.
	 * @param bugzillaPage - ID of page to make the request to
	 * @return HTTP connection object
	 */
	protected HttpURLConnection getConnection(int bugzillaPage, String getParams) {
		if (baseUrl == null) return null;
		if (getParams == null) getParams = "";
		else getParams = "?" + getParams;
		
		try {
			URL url = new URL(baseUrl+PAGES[bugzillaPage]+getParams);
			if (log.isDebugEnabled()) log.debug(url.toString());
			// apply proxy
			HttpURLConnection con = null;
			if (proxy != null) {
				log.debug("Using proxy: "+proxy);
				con = (HttpURLConnection)url.openConnection(proxy);
				if (proxyAuthorizationCallback != null) {
					// apply proxy authorization
					// "Proxy-Authorization: Basic "<base64(user:password)>"
					// commons.codec delivers Base64 Encoder
					String authName = proxyAuthorizationCallback.getName();
					String authPass = proxyAuthorizationCallback.getPassword();
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
			return con;
		} catch (MalformedURLException e) {
			log.error("Invalid URL: ", e);
		} catch (IOException e) {
			log.error("Error when making request", e);
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
					} else if (log.isDebugEnabled()) {
						log.debug("Cookie not applicable: "+cookie.toString());
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
		if (log.isTraceEnabled()) log.trace("applied cookie: "+rc.toString());

	}
	
	/**
	 * Debugs the response from a connection.
	 * All headers and the content will be dumped into the log.
	 * @param con - HTTP connection to dump
	 */
	public static void debugResponse(HttpURLConnection con) {
		if (!log.isDebugEnabled()) return;
		try {
			Map<String,List<String>> headerFields = con.getHeaderFields();
			Iterator<String> i = headerFields.keySet().iterator();
			while (i.hasNext()) {
				String key = i.next();
				Iterator<String> j = headerFields.get(key).iterator();
				while (j.hasNext()) log.debug(key+": "+j.next());
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line;
			while ((line = r.readLine()) != null) {
				log.debug(line);
			}
		} catch (IOException e) {
			log.error("Error while debugging connection", e);
		}
	}
	
	/**
	 * Returns default search parameters.
	 * DO NOT modify these defaults!
	 * @return default search parameters.
	 */
	public static UrlParameters getDefaultSearchParameters() {
		if (DEFAULT_SEARCH_PARAMETERS == null) {
			DEFAULT_SEARCH_PARAMETERS = new UrlParameters();
			DEFAULT_SEARCH_PARAMETERS.setParameter("query_format", "advanced");
			DEFAULT_SEARCH_PARAMETERS.setParameter("short_desc_type", "allwordssubstr");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("short_desc", "");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("classification", "");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("product", "");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("component", "");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("long_desc_type", "");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("long_desc", "");
			DEFAULT_SEARCH_PARAMETERS.setParameter("bug_file_loc_type", "allwordssubstr");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("bug_file_loc", "");
			DEFAULT_SEARCH_PARAMETERS.setParameter("keywords_type", "allwords");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("keywords", "");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("bug_status", "");
			DEFAULT_SEARCH_PARAMETERS.setParameter("emailassigned_to1", "1");
			DEFAULT_SEARCH_PARAMETERS.setParameter("emailtype1", "substring");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("email1", "");
			DEFAULT_SEARCH_PARAMETERS.setParameter("emailassigned_to2", "1");
			DEFAULT_SEARCH_PARAMETERS.setParameter("emailreporter2", "1");
			DEFAULT_SEARCH_PARAMETERS.setParameter("emailqa_contact2", "1");
			DEFAULT_SEARCH_PARAMETERS.setParameter("emailcc2", "1");
			DEFAULT_SEARCH_PARAMETERS.setParameter("emailtype2", "substring");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("email2", "");
			DEFAULT_SEARCH_PARAMETERS.setParameter("bugidtype", "include");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("bug_id", "");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("chfieldfrom", "");
			DEFAULT_SEARCH_PARAMETERS.setParameter("chfieldto", "Now");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("chfieldvalue", "");
			DEFAULT_SEARCH_PARAMETERS.setParameter("cmdtype", "doit");
			DEFAULT_SEARCH_PARAMETERS.setParameter("order", "Bug Number");
			DEFAULT_SEARCH_PARAMETERS.setParameter("field0-0-0", "noop");
			DEFAULT_SEARCH_PARAMETERS.setParameter("type0-0-0", "noop");
			//DEFAULT_SEARCH_PARAMETERS.setParameter("value0-0-0", "");
		}
		return DEFAULT_SEARCH_PARAMETERS;
	}
	
	/**
	 * Debugs information into log.
	 * @param log - the log object
	 */
	@Override
	public void dump(Log log) {
		if (!log.isDebugEnabled()) return;
		log.debug("bugzilla-home="+baseUrl.toString());
		super.dump(log);
	}
	
	/**
	 * Implementation of a bug iterator.
	 * This implementation is based on Silberschatz' proposal
	 * for synchronizing a reader and a writer thread.
	 * The first call to {@link BugzillaBugIterator#next()} will spawn
	 * the writer thread that actually retrieves bug data from Bugzilla.
	 * @author Ralph Schuster
	 *
	 */
	protected class BugzillaBugIterator implements Iterator<BugzillaBug> {

		private List<String> bugList;
		private int delivered;
		private List<BugzillaBug> availableBugs;
		private XmlParser xmlParser;
		
		/**
		 * Default constructor.
		 * @param bugList - bug ID list to retrieve
		 */
		public BugzillaBugIterator(List<String> bugList) {
			delivered = 0;
			this.bugList = bugList;
			availableBugs = new ArrayList<BugzillaBug>();
		}
		
		/**
		 * Returns true while number of delivered bugs (calls
		 * to {@link #next()} are less than number of bugs in
		 * request list.
		 * @return true if there are still bugs in the queue to read.
		 */
		@Override
		public boolean hasNext() {
			return delivered < bugList.size();
		}

		/**
		 * Delivers the next bug.
		 * The first call will trigger spawning of the writer thread. This implementation
		 * is based on Silberschatz' synchronization solution for
		 * reader and writer threads (reader's part here). That means, the
		 * call will wait until the next bug is available if required.
		 * @return next bug in queue
		 */
		@Override
		public synchronized BugzillaBug next() {
			BugzillaBug rc;
			if (xmlParser == null) startXmlParser();
			if (delivered >= bugList.size()) 
				throw new IllegalStateException("Empty queue");
			
			while (availableBugs.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) { }
			}
			rc = availableBugs.remove(0);
			delivered++;
			
			notify();
			
			return rc;
		}

		/**
		 * Adds a new bug to the list of available bugs.
		 * This implementation is based on Silberschatz' synchronization
		 * solution for reader and writer threads (writer's part here).
		 * @param bug - bug to add to queue
		 */
		protected synchronized void addBug(BugzillaBug bug) {
			while (availableBugs.size() >= 20) {
				try {
					wait();
				} catch (InterruptedException e) {}
			}
			availableBugs.add(bug);
			
			notify();
		}
		
		/**
		 * Always throws an exception.
		 */
		@Override
		public void remove() {
			throw new UnsupportedOperationException("This is a read-only iterator");
		}
		
		/**
		 * Starts the writer's thread.
		 * This method will create the appropriate POST request to the Bugzilla
		 * instance and hand over the connection to the new thread. Internal
		 * variables are set to indicate that the writer thread is running. 
		 */
		protected void startXmlParser() {
			try {
				if (log.isTraceEnabled()) log.trace("Requesting XML file...");
				
				// Build parameters
				UrlParameters parameters = new UrlParameters();
				parameters.addAll("id", bugList);
				parameters.setParameter("ctype", "xml");
				parameters.setParameter("excludefield", "attachmentdata");
				parameters.setParameter("submit", "XML");
				String paramString = parameters.getUrlEncodedString();

				// make a connection
				HttpURLConnection con = getConnection(BUGZILLA_SHOW_BUG);
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
				con.setRequestProperty("Content-Length", "" + paramString.length());
				con.setDoOutput(true);
				PrintWriter out = new PrintWriter(con.getOutputStream());
				out.print(paramString);
				out.flush();
				out.close();

				if (log.isTraceEnabled()) log.trace("Awaiting XML file...");
				// Read the response
				if (con.getResponseCode() == 200) {
					if (log.isTraceEnabled()) log.trace("Receiving XML file...");
					xmlParser = new XmlParser(con.getInputStream(), this);
					Thread t = new Thread(xmlParser);
					t.start();
				}
			} catch (IOException e) {

			}
		}
	}
	
	/**
	 * Does the actual meat by parsing the XML response.
	 * Implementation of the separate writer thread. The XML will be parsed
	 * (SAX implementation) and all found bugs will be added to the
	 * corresponding iterator object ({@link BugzillaBugIterator#addBug(BugzillaBug)}).
	 * @author Ralph Schuster
	 */
	protected class XmlParser extends DefaultHandler2 implements Runnable {
		
		private InputStream xmlStream;
		private BugzillaBugIterator iterator;
		private XMLReader xmlReader;
		private BugzillaBug currentBug;
		private LongDescription currentLongDescription;
		private Attachment currentAttachment;
		private StringBuffer currentContent;
		private String currentCustomField;
		private String bugzillaVersion;
		private String bugzillaUri;
		
		/**
		 * Constructor.
		 * @param xmlStream - input stream with XML response from Bugzilla
		 * @param iterator - corresponding iterator that receives the results 
		 */
		public XmlParser(InputStream xmlStream, BugzillaBugIterator iterator) {
			this.xmlStream = xmlStream;
			this.iterator = iterator;
		}
		
		/**
		 * Runs the extraction.
		 */
		public void run() {
			try {
				SAXParserFactory factory = SAXParserFactory.newInstance();
				// Prevent fetching the DTD
				factory.setValidating(false);
				factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
				
				// Create the XMLReader
				SAXParser xmlParser = factory.newSAXParser();
				xmlReader = xmlParser.getXMLReader();
				
				// This class itself will take care of the elements
				xmlReader.setContentHandler(this);
				xmlReader.parse(new InputSource(new BufferedReader(new InputStreamReader(xmlStream))));
				if (log.isTraceEnabled()) log.trace("XML file completed");
			} catch (IOException e) {
				log.error("Error while retrieving Bugzilla XML response:", e);
			} catch (ParserConfigurationException e) {
				log.error("SAXParser not configured:", e);
			} catch (SAXException e) {
				if (e.getMessage().indexOf("invalid XML character") >= 0) {
					log.error("The XML file received contains illegal characters. This is not a B4J error but a Bugzilla problem. Sorry", e);
				} else {
					log.error("Error while parsing Bugzilla XML response:", e);
				}
			}
		}

		/**
		 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */
		@Override
		public void startElement(String uri, String localName, String name,	Attributes attributes) throws SAXException {
			super.startElement(uri, localName, name, attributes);
			if (name.equals("bugzilla")) {
				bugzillaUri = attributes.getValue("urlbase");
				bugzillaVersion = attributes.getValue("version");
				HttpBugzillaSession.this.bugzillaVersion = bugzillaVersion;
			} else if (name.equals("bug")) {
				currentBug = createBugzillaBug();
				currentBug.setBugzillaUri(bugzillaUri);
				currentBug.setBugzillaVersion(bugzillaVersion);
			} else if (name.equals("bug_id")) {
				currentContent = new StringBuffer();
			} else if (name.equals("creation_ts")) { // 2008-07-23 12:28
				currentContent = new StringBuffer();
			} else if (name.equals("short_desc")) {
				currentContent = new StringBuffer();
			} else if (name.equals("delta_ts")) { // 2008-07-23 12:28:22
				currentContent = new StringBuffer();
			} else if (name.equals("reporter_accessible")) {
				currentContent = new StringBuffer();
			} else if (name.equals("cclist_accessible")) {
				currentContent = new StringBuffer();
			} else if (name.equals("classification_id")) {
				currentContent = new StringBuffer();
			} else if (name.equals("classification")) {
				currentContent = new StringBuffer();
			} else if (name.equals("product")) {
				currentContent = new StringBuffer();
			} else if (name.equals("component")) {
				currentContent = new StringBuffer();
			} else if (name.equals("version")) {
				currentContent = new StringBuffer();
			} else if (name.equals("rep_platform")) {
				currentContent = new StringBuffer();
			} else if (name.equals("op_sys")) {
				currentContent = new StringBuffer();
			} else if (name.equals("bug_status")) {
				currentContent = new StringBuffer();
			} else if (name.equals("resolution")) {
				currentContent = new StringBuffer();
			} else if (name.equals("priority")) {
				currentContent = new StringBuffer();
			} else if (name.equals("bug_severity")) {
				currentContent = new StringBuffer();
			} else if (name.equals("target_milestone")) {
				currentContent = new StringBuffer();
			} else if (name.equals("everconfirmed")) {
				currentContent = new StringBuffer();
			} else if (name.equals("reporter")) {
				currentContent = new StringBuffer();
			} else if (name.equals("assigned_to")) {
				currentContent = new StringBuffer();
			} else if (name.equals("qa_contact")) {
				currentContent = new StringBuffer();
			} else if (name.equals("long_desc")) { // multiple
				currentLongDescription = currentBug.addLongDescription();
			} else if (name.equals("who")) {
				currentContent = new StringBuffer();
			} else if (name.equals("bug_when")) { // 2008-07-23 12:28:22
				currentContent = new StringBuffer();
			} else if (name.equals("thetext")) {
				currentContent = new StringBuffer();
			} else if (name.equals("bug_file_loc")) {
				currentContent = new StringBuffer();
			} else if (name.equals("cc")) { // multiple
				currentContent = new StringBuffer();
			} else if (name.equals("attachment")) { // multiple
				currentAttachment = currentBug.addAttachment();
			} else if (name.equals("attachid")) {
				currentContent = new StringBuffer();
			} else if (name.equals("date")) {
				currentContent = new StringBuffer();
			} else if (name.equals("desc")) {
				currentContent = new StringBuffer();
			} else if (name.equals("filename")) {
				currentContent = new StringBuffer();
			} else if (name.equals("type")) {
				currentContent = new StringBuffer();
			} else if (name.equals("blocked")) {
				currentContent = new StringBuffer();
			} else if (name.equals("alias")) {
				currentContent = new StringBuffer();
			} else if (name.equals("status_whiteboard")) {
				currentContent = new StringBuffer();
			} else if (name.equals("estimated_time")) {
				currentContent = new StringBuffer();
			} else if (name.equals("remaining_time")) {
				currentContent = new StringBuffer();
			} else if (name.equals("actual_time")) {
				currentContent = new StringBuffer();
			} else if (name.equals("deadline")) {
				currentContent = new StringBuffer();
			} else {
				currentCustomField = name;
				currentContent = new StringBuffer();
				//log.warn("Custom field: "+name);
			}
		}
		
		/**
		 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public void endElement(String uri, String localName, String name) throws SAXException {
			if (name.equals("bug")) {
				iterator.addBug(currentBug);
				currentBug = null;
			} else if (name.equals("bug_id")) {
				currentBug.setId(Long.parseLong(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("creation_ts")) { // 2008-07-23 12:28
				try {
					currentBug.setCreationTimestamp(DefaultBugzillaBug.DATETIME_WITHOUT_SEC.parse(currentContent.toString()));
				} catch (ParseException e) {
					log.error("Cannot parse this time: "+currentContent.toString());
				}
				currentContent = null;
			} else if (name.equals("short_desc")) {
				currentBug.setShortDescription(currentContent.toString());
				currentContent = null;
			} else if (name.equals("delta_ts")) { // 2008-07-23 12:28:22
				try {
					currentBug.setDeltaTimestamp(DefaultBugzillaBug.DATETIME_WITH_SEC.parse(currentContent.toString()));
				} catch (ParseException e) {
					log.error("Cannot parse this time: "+currentContent.toString());
				}
				currentContent = null;
			} else if (name.equals("reporter_accessible")) {
				currentBug.setReporterAccessible(parseBoolean(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("cclist_accessible")) {
				currentBug.setCclistAccessible(parseBoolean(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("classification_id")) {
				currentBug.setClassificationId(Long.parseLong(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("classification")) {
				currentBug.setClassification(currentContent.toString());
				currentContent = null;
			} else if (name.equals("product")) {
				currentBug.setProduct(currentContent.toString());
				currentContent = null;
			} else if (name.equals("component")) {
				currentBug.setComponent(currentContent.toString());
				currentContent = null;
			} else if (name.equals("version")) {
				currentBug.setVersion(currentContent.toString());
				currentContent = null;
			} else if (name.equals("rep_platform")) {
				currentBug.setRepPlatform(currentContent.toString());
				currentContent = null;
			} else if (name.equals("op_sys")) {
				currentBug.setOpSys(currentContent.toString());
				currentContent = null;
			} else if (name.equals("bug_status")) {
				currentBug.setStatus(currentContent.toString());
				currentContent = null;
			} else if (name.equals("resolution")) {
				currentBug.setResolution(currentContent.toString());
				currentContent = null;
			} else if (name.equals("priority")) {
				currentBug.setPriority(currentContent.toString());
				currentContent = null;
			} else if (name.equals("bug_severity")) {
				currentBug.setSeverity(currentContent.toString());
				currentContent = null;
			} else if (name.equals("target_milestone")) {
				currentBug.setTargetMilestone(currentContent.toString());
				currentContent = null;
			} else if (name.equals("everconfirmed")) {
				currentBug.setEverConfirmed(parseBoolean(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("reporter")) {
				currentBug.setReporter(currentContent.toString());
				currentContent = null;
			} else if (name.equals("assigned_to")) {
				currentBug.setAssignedTo(currentContent.toString());
				currentContent = null;
			} else if (name.equals("qa_contact")) {
				currentBug.setQaContact(currentContent.toString());
				currentContent = null;
			} else if (name.equals("long_desc")) { // multiple
				currentLongDescription = null;
			} else if (name.equals("who")) {
				currentLongDescription.setWho(currentContent.toString());
				currentContent = null;
			} else if (name.equals("bug_when")) { // 2008-07-23 12:28:22
				try {
					currentLongDescription.setWhen(DefaultBugzillaBug.DATETIME_WITHOUT_SEC.parse(currentContent.toString()));
				} catch (ParseException e) {
					log.error("Cannot parse this time: "+currentContent.toString());
				}
				currentContent = null;
			} else if (name.equals("thetext")) {
				currentLongDescription.setTheText(currentContent.toString());
				currentContent = null;
			} else if (name.equals("bug_file_loc")) {
				currentBug.setFileLocation(currentContent.toString());
				currentContent = null;
			} else if (name.equals("attachment")) { // multiple
				currentAttachment = null;
			} else if (name.equals("attachid")) {
				currentAttachment.setId(Long.parseLong(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("date")) {
				currentContent = null;
			} else if (name.equals("desc")) {
				currentAttachment.setDescription(currentContent.toString());
				currentContent = null;
			} else if (name.equals("filename")) {
				currentAttachment.setDescription(currentContent.toString());
				currentContent = null;
			} else if (name.equals("type")) {
				currentAttachment.setType(currentContent.toString());
				currentContent = null;
			} else if (name.equals("cc")) { // multiple
				currentBug.addCc(currentContent.toString());
				currentContent = null;
			} else if (name.equals("blocked")) {
				currentBug.setBlocked(Long.parseLong(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("alias")) {
				currentBug.setAlias(currentContent.toString());
				currentContent = null;
			} else if (name.equals("status_whiteboard")) {
				currentBug.setWhiteboard(currentContent.toString());
				currentContent = null;
			} else if (name.equals("estimated_time")) {
				currentBug.setEstimatedTime(Double.parseDouble(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("remaining_time")) {
				currentBug.setRemainingTime(Double.parseDouble(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("actual_time")) {
				currentBug.setActualTime(Double.parseDouble(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("deadline")) {
				try {
					currentBug.setDeadline(DefaultBugzillaBug.DATE.parse(currentContent.toString()));
				} catch (ParseException e) {
					log.error("Cannot parse this date: "+currentContent.toString());
				}
				currentContent = null;
			} else {
				if ((currentCustomField != null) && (currentContent != null)) {
					if (currentBug != null) {
						currentBug.setCustomField(currentCustomField, currentContent.toString());
						currentCustomField = null;
						currentContent = null;
					}
				}
			}
			super.endElement(uri, localName, name);
		}

		/**
		 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
		 */
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (currentContent != null) currentContent.append(ch, start, length);
			super.characters(ch, start, length);
		}

		/**
		 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
		 */
		@Override
		public void startDocument() throws SAXException {
			// Nothing to do
			super.startDocument();
		}

		/**
		 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
		 */
		@Override
		public void endDocument() throws SAXException {
			// Nothing to do
			super.endDocument();
		}

		/**
		 * Parses a boolean for Bugzilla XML.
		 * Bugzilla uses "1" to represent a TRUE value in its XML.
		 * @param s - boolean to parse
		 * @return true if string represented the TRUE value
		 */
		public boolean parseBoolean(String s) {
			if (s == null) return false;
			s = s.trim();
			if (s.length() == 0) return false;
			if (s.equals("1")) return true;
			if (s.equals("0")) return false;
			return Boolean.parseBoolean(s);
		}
	}
}

/*

query_format=advanced
short_desc_type=allwordssubstr
short_desc=
classification=
product=
long_desc_type=substring
long_desc=
bug_file_loc_type=allwordssubstr
bug_file_loc=
keywords_type=allwords
keywords=
bug_status=
emailassigned_to1=1
emailtype1=substring
email1=
emailassigned_to2=1
emailreporter2=1
emailqa_contact2=1
emailcc2=1
emailtype2=substring
email2=
bugidtype=include
bug_id=
chfieldfrom=
chfieldto=Now
chfieldvalue=
cmdtype=doit
order=Bug+Number
field0-0-0=noop
type0-0-0=noop
value0-0-0=

*/