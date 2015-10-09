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
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
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

import org.apache.commons.io.Charsets;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;

import rs.baselib.io.XmlReaderFilter;
import rs.baselib.lang.LangUtils;
import b4j.core.Attachment;
import b4j.core.Classification;
import b4j.core.Comment;
import b4j.core.Component;
import b4j.core.DefaultAttachment;
import b4j.core.DefaultClassification;
import b4j.core.DefaultComment;
import b4j.core.DefaultLink;
import b4j.core.Issue;
import b4j.core.IssueLink.Type;
import b4j.core.IssueType;
import b4j.core.Priority;
import b4j.core.Project;
import b4j.core.Resolution;
import b4j.core.SearchData;
import b4j.core.SearchResultCountCallback;
import b4j.core.Severity;
import b4j.core.Status;
import b4j.core.session.bugzilla.BugzillaComponent;
import b4j.core.session.bugzilla.BugzillaProject;
import b4j.core.session.bugzilla.BugzillaTransformer;
import b4j.core.session.bugzilla.BugzillaUser;
import b4j.core.session.bugzilla.BugzillaVersion;
import b4j.util.BugzillaUtils;
import b4j.util.HttpSessionParams;
import b4j.util.MetaData;
import b4j.util.UrlParameters;


/**
 * Implements Bugzilla access via HTTP.
 * <p>There is no additional configuration required. See
 * {@link AbstractPlainHttpSession} for configuration description.</p>
 * @author Ralph Schuster
 *
 */
public class BugzillaHttpSession extends AbstractPlainHttpSession {

	/** Constant for requesting URL connection to login page */
	public static final int BUGZILLA_LOGIN = 0;
	/** Constant for requesting URL connection to search result page */
	public static final int BUGZILLA_SEARCH = 1;
	/** Constant for requesting URL connection to logout page */
	public static final int BUGZILLA_LOGOUT = 2;
	/** Constant for requesting URL connection to show bug page */
	public static final int BUGZILLA_SHOW_BUG = 3;
	/** Constant for requesting URL connection to get attachment content */
	public static final int BUGZILLA_GET_ATTACHMENT = 4;

	private static final String MINIMUM_BUGZILLA_VERSION = "2.20";
	private static final String MAXIMUM_BUGZILLA_VERSION = null;

	/** The URL paths to the Bugzilla pages */
	protected static final String[] PAGES = new String[] {
		"/index.cgi",
		"/buglist.cgi",
		"/relogin.cgi",
		"/show_bug.cgi",
		"/attachment.cgi"
	};
	private static UrlParameters DEFAULT_SEARCH_PARAMETERS;

	private MetaData<String, IssueType> issueTypes = new MetaData<String, IssueType>(new BugzillaTransformer.IssueType());
	private MetaData<String, Status> status = new MetaData<String, Status>(new BugzillaTransformer.Status());
	private MetaData<String, Priority> priorities = new MetaData<String, Priority>(new BugzillaTransformer.Priority());
	private MetaData<String, Severity> severities = new MetaData<String, Severity>(new BugzillaTransformer.Severity());
	private MetaData<String, Resolution> resolutions = new MetaData<String, Resolution>(new BugzillaTransformer.Resolution());
	private MetaData<String, Project> projects = new MetaData<String, Project>(new BugzillaTransformer.Project());
	private MetaData<String, Component> components = new MetaData<String, Component>(new BugzillaTransformer.Component());
	private MetaData<String, Classification> classifications = new MetaData<String, Classification>(new BugzillaTransformer.Classification());

	/**
	 * Default constructor.
	 */
	public BugzillaHttpSession() {
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
	 * Opens the session with configured Bugzilla instance.
	 * @return true when session could be established successfully, false otherwise
	 */
	@Override
	public boolean open() {
		if (isLoggedIn()) return true;
		setBugzillaVersion(null);

		HttpSessionParams httpParams = getHttpSessionParams();
		if (httpParams == null) httpParams = new HttpSessionParams();

		// Exception: No login required
		if (httpParams.getLogin() == null) {
			getLog().debug("No Auth required");
			setLoggedIn(true);
			return true;
		}

		try {
			// Bugzilla_login = xxx
			// Bugzilla_password = xxx
			// GoAheadAndLogIn = Login
			UrlParameters params = new UrlParameters();
			params.setParameter("Bugzilla_login", httpParams.getLogin());
			params.setParameter("Bugzilla_password", httpParams.getPassword());
			params.setParameter("GoAheadAndLogIn", "Login");
			String paramString = params.getUrlEncodedString();


			// make a connection
			HttpURLConnection con = getConnection(BUGZILLA_LOGIN);
			if (con == null) throw new IOException("Cannot open connection for login");
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Length", "" + paramString.length());
			con.setDoOutput(true);
			Writer out = new OutputStreamWriter(con.getOutputStream(), Charsets.UTF_8);
			out.write(paramString);
			out.flush();
			out.close();

			// Read the response;
			if (con.getResponseCode() == 200) {
				//debugResponse(con);
				boolean rc = retrieveCookies(con);

				// Get Bugzilla version and test for compatibility
				BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), Charsets.UTF_8));
				Pattern p = Pattern.compile(".*version\\s+([\\d\\.]+).*", Pattern.CASE_INSENSITIVE);

				String line;
				while ((line = r.readLine()) != null) {
					// Search for: version 3.0.5
					Matcher m = p.matcher(line);
					if (m.matches()) {
						setBugzillaVersion(m.group(1));
						break;
					}
				}

				setLoggedIn(rc);
				if (getLog().isInfoEnabled()) {
					if (rc) {
						getLog().info("Session opened:   "+getBaseUrl());
						if (getLog().isDebugEnabled()) getLog().debug("Bugzilla-Version: "+getBugzillaVersion());
					} else getLog().info("Bugzilla did not sent Cookie");
				}

				if (rc) checkBugzillaVersion();

				return rc;
			} else {
				getLog().error("Cannot open session: Response was \""+con.getResponseMessage()+"\"");
			}
		} catch (IOException e) {
			getLog().error("Cannot open session:", e);
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
		if (getHttpSessionParams().getLogin() != null) {
			try {
				// make a connection
				HttpURLConnection con = getConnection(BUGZILLA_LOGOUT);
				con.getResponseCode();
			} catch (IOException e) {

			}
		}
		super.close();
	}

	/**
	 * Returns the {@link IssueType} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<String, IssueType> getIssueTypes() {
		return issueTypes;
	}

	/**
	 * Returns the {@link Status} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<String, Status> getStatus() {
		return status;
	}

	/**
	 * Returns the {@link Priority} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<String, Priority> getPriorities() {
		return priorities;
	}

	/**
	 * Returns the {@link Severity} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<String, Severity> getSeverities() {
		return severities;
	}

	/**
	 * Returns the {@link Resolution} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<String, Resolution> getResolutions() {
		return resolutions;
	}

	/**
	 * Returns the {@link Project} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<String, Project> getProjects() {
		return projects;
	}

	/**
	 * Returns the {@link Component} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<String, Component> getComponents() {
		return components;
	}

	/**
	 * Returns the {@link Classification} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<String, Classification> getClassifications() {
		return classifications;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Issue getIssue(String id) {
		List<String> idList = new ArrayList<String>();
		idList.add(id);
		BugzillaBugIterator i = new BugzillaBugIterator(idList);
		if ((i != null) && i.hasNext()) return i.next();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getAttachment(Attachment attachment) throws IOException {
		HttpURLConnection con = getConnection(BUGZILLA_GET_ATTACHMENT, "id="+attachment.getId());
		return con != null ? con.getInputStream() : null;
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
	public Iterable<Issue> searchBugs(SearchData searchData, SearchResultCountCallback callback) {
		checkLoggedIn();

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
				BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream(), Charsets.UTF_8));
				Pattern p = Pattern.compile(".*href=\"show_bug\\.cgi\\?id=(\\d+)\">\\d+</a>.*");

				String line;
				while ((line = r.readLine()) != null) {
					// Search for: <a href="show_bug.cgi?id=2349">2349</a>
					Matcher m = p.matcher(line);
					if (m.matches()) {
						idList.add(m.group(1));
					} //else if (line.indexOf("show_bug.cgi") >= 0) log.info(line);
				}
				if (callback != null) callback.setResultCount(idList.size());
				if (getLog().isDebugEnabled()) getLog().debug("Found "+idList.size()+" bugs");

				// Return the bug iterator
				return new BugzillaBugIterator(idList);
			} else {
				getLog().debug("Response invalid: "+con.getResponseCode());
			}
		} catch (IOException e) {
			getLog().error("Cannot perform search", e);
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
		return getConnection(getBaseUrl()+PAGES[bugzillaPage], getParams, createRequestProperties(bugzillaPage));
	}

	/**
	 * Returns HTTP headers (request properties) that need to be set on each request.
	 * <p>The default implementation sets the Referer property as some installations require this header.</p>
	 * @param bugzillaPage the page to be requested
	 * @return the request properties to be set on HTTP connection
	 */
	protected Map<String,String> createRequestProperties(int bugzillaPage) {
		Map<String,String> rc = new HashMap<String, String>();
		rc.put("Referer", getBaseUrl()+PAGES[BUGZILLA_LOGIN]);
		return rc;
	}
	/**
	 * Returns default search parameters.
	 * DO NOT modify these defaults!
	 * @return default search parameters.
	 */
	public static UrlParameters getDefaultSearchParameters() {
		if (DEFAULT_SEARCH_PARAMETERS == null) {
			synchronized(PAGES) {
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
			}
		}
		return DEFAULT_SEARCH_PARAMETERS;
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
	protected class BugzillaBugIterator implements Iterable<Issue>, Iterator<Issue> {

		private List<String> bugList;
		private int delivered;
		private List<Issue> availableBugs;
		private XmlParser xmlParser;

		/**
		 * Default constructor.
		 * @param bugList - bug ID list to retrieve
		 */
		public BugzillaBugIterator(List<String> bugList) {
			delivered = 0;
			this.bugList = bugList;
			availableBugs = new ArrayList<Issue>();
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
		public synchronized Issue next() {
			Issue rc;
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
		protected synchronized void addBug(Issue bug) {
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
				if (getLog().isTraceEnabled()) getLog().trace("Requesting XML file...");

				// Build parameters
				UrlParameters parameters = new UrlParameters();
				parameters.addAll("id", bugList);
				parameters.setParameter("ctype", "xml");
				parameters.setParameter("excludefield", "attachmentdata");
				parameters.setParameter("submit", "XML");
				String paramString = parameters.getUrlEncodedString();

				// make a connection
				HttpURLConnection con = getConnection(BUGZILLA_SHOW_BUG);
				if (con == null) throw new RuntimeException("Cannot open connection");
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
				con.setRequestProperty("Content-Length", "" + paramString.length());
				con.setDoOutput(true);
				Writer out = new OutputStreamWriter(con.getOutputStream(), Charsets.UTF_8);
				out.write(paramString);
				out.flush();
				out.close();

				if (getLog().isTraceEnabled()) getLog().trace("Awaiting XML file...");
				// Read the response
				if (con.getResponseCode() == 200) {
					if (getLog().isTraceEnabled()) getLog().trace("Receiving XML file...");
					xmlParser = new XmlParser(con.getInputStream(), this);
					Thread t = new Thread(xmlParser);
					t.start();
				}
			} catch (IOException e) {

			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Iterator<Issue> iterator() {
			return this;
		}


	}

	/**
	 * Does the actual meat by parsing the XML response.
	 * Implementation of the separate writer thread. The XML will be parsed
	 * (SAX implementation) and all found bugs will be added to the
	 * corresponding iterator object ({@link BugzillaBugIterator#addBug(Issue)}).
	 * @author Ralph Schuster
	 */
	protected class XmlParser extends DefaultHandler2 implements Runnable {

		private InputStream xmlStream;
		private BugzillaBugIterator iterator;
		private XMLReader xmlReader;
		private Issue currentIssue;
		private Comment currentComment;
		private Attachment currentAttachment;
		private StringBuffer currentContent;
		private String currentCustomField;
		private String bugzillaVersion;
		private String bugzillaUri;
		private BugzillaUser currentUser;

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
				xmlReader.parse(new InputSource(new XmlReaderFilter(new InputStreamReader(xmlStream, Charsets.UTF_8))));
				if (getLog().isTraceEnabled()) getLog().trace("XML file completed");
			} catch (IOException e) {
				getLog().error("Error while retrieving Bugzilla XML response:", e);
			} catch (ParserConfigurationException e) {
				getLog().error("SAXParser not configured:", e);
			} catch (SAXException e) {
				String msg = e.getMessage();
				if ((msg != null) && (msg.indexOf("invalid XML character") >= 0)) {
					getLog().error("The XML file received contains illegal characters. This is not a B4J error but a Bugzilla problem. Sorry", e);
				} else {
					getLog().error("Error while parsing Bugzilla XML response:", e);
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
				setBugzillaVersion(bugzillaVersion);
			} else if (name.equals("bug")) {
				currentIssue = createIssue();
				currentIssue.setServerUri(bugzillaUri);
				currentIssue.setServerVersion(bugzillaVersion);
				currentIssue.setType(issueTypes.get("bug"));
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
				currentUser = new BugzillaUser();
				currentContent = new StringBuffer();
				String userName = attributes.getValue("name");
				if (userName != null) {
					currentUser.setRealName(userName);
				}
			} else if (name.equals("assigned_to")) {
				currentUser = new BugzillaUser();
				currentContent = new StringBuffer();
				String userName = attributes.getValue("name");
				if (userName != null) {
					currentUser.setRealName(userName);
				}
			} else if (name.equals("qa_contact")) {
				currentContent = new StringBuffer();
			} else if (name.equals("long_desc")) { // multiple
				currentComment = new DefaultComment(currentIssue.getId());
			} else if (name.equals("commentid")) {
				currentContent = new StringBuffer();
			} else if (name.equals("who")) {
				currentUser = new BugzillaUser();
				currentContent = new StringBuffer();
				String userName = attributes.getValue("name");
				if (userName != null) {
					currentUser.setRealName(userName);
				}
			} else if (name.equals("bug_when")) { // 2008-07-23 12:28:22
				currentContent = new StringBuffer();
			} else if (name.equals("thetext")) {
				currentContent = new StringBuffer();
			} else if (name.equals("bug_file_loc")) {
				currentContent = new StringBuffer();
			} else if (name.equals("cc")) { // multiple
				currentContent = new StringBuffer();
			} else if (name.equals("attachment")) { // multiple
				currentAttachment = new DefaultAttachment(currentIssue.getId());
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
			} else if (name.equals("dependson")) {
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
				iterator.addBug(currentIssue);
				currentIssue = null;
			} else if (name.equals("bug_id")) {
				currentIssue.setId(currentContent.toString());
				currentIssue.setUri(getBaseUrl()+PAGES[BUGZILLA_SHOW_BUG]+"?id="+currentIssue.getId());
				currentContent = null;
			} else if (name.equals("creation_ts")) { // 2008-07-23 12:28
				try {
					currentIssue.setCreationTimestamp(BugzillaUtils.parseDate(currentContent.toString()));
				} catch (ParseException e) {
					getLog().error("Cannot parse this time: "+currentContent.toString());
				}
				currentContent = null;
			} else if (name.equals("short_desc")) {
				currentIssue.setSummary(currentContent.toString());
				currentContent = null;
			} else if (name.equals("delta_ts")) { // 2008-07-23 12:28:22
				try {
					if (currentAttachment != null) {
						currentAttachment.set(Attachment.UPDATE_TIMESTAMP, BugzillaUtils.parseDate(currentContent.toString()));
					} else {
						currentIssue.setUpdateTimestamp(BugzillaUtils.parseDate(currentContent.toString()));
					}
				} catch (ParseException e) {
					getLog().error("Cannot parse this time: "+currentContent.toString());
				}
				currentContent = null;
			} else if (name.equals("reporter_accessible")) {
				currentIssue.set(Issue.REPORTER_ACCESSIBLE, LangUtils.getBoolean(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("cclist_accessible")) {
				currentIssue.set(Issue.CCLIST_ACCESSIBLE, LangUtils.getBoolean(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("classification_id")) {
				currentIssue.setClassification(classifications.get(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("classification")) {
				if (currentIssue.getClassification() != null) {
					((DefaultClassification)currentIssue.getClassification()).setName(currentContent.toString());
				}
				currentContent = null;
			} else if (name.equals("product")) {
				currentIssue.setProject(projects.get(currentContent.toString()));
				for (Component c : currentIssue.getComponents()) {
					if (c.getProject() == null) {
						((BugzillaComponent)c).setProject((BugzillaProject)currentIssue.getProject());
					}
				}
				currentContent = null;
			} else if (name.equals("component")) {
				Component c = components.get(currentContent.toString());
				if ((c.getProject() != null) && (currentIssue.getProject() != null)) {
					((BugzillaComponent)c).setProject((BugzillaProject)currentIssue.getProject());
				}
				currentIssue.addComponents(c);
				currentContent = null;
			} else if (name.equals("version")) {
				currentIssue.addFixVersions(new BugzillaVersion(null, currentIssue.getProject(), currentContent.toString()));
				currentContent = null;
			} else if (name.equals("rep_platform")) {
				currentIssue.set(Issue.REP_PLATFORM, currentContent.toString());
				currentContent = null;
			} else if (name.equals("op_sys")) {
				currentIssue.set(Issue.OP_SYS, currentContent.toString());
				currentContent = null;
			} else if (name.equals("bug_status")) {
				currentIssue.setStatus(status.get(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("resolution")) {
				currentIssue.setResolution(resolutions.get(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("priority")) {
				currentIssue.setPriority(priorities.get(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("bug_severity")) {
				currentIssue.setSeverity(severities.get(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("target_milestone")) {
				currentIssue.set(Issue.MILESTONE, currentContent.toString());
				currentContent = null;
			} else if (name.equals("everconfirmed")) {
				currentIssue.set(Issue.CONFIRMED, LangUtils.getBoolean(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("reporter")) {
				if (currentUser != null) {
					currentUser.setId(currentContent.toString());
					currentUser.setName(currentContent.toString());
					currentIssue.setReporter(currentUser);
				}
				currentUser = null;
				currentContent = null;
			} else if (name.equals("assigned_to")) {
				if (currentUser != null) {
					currentUser.setId(currentContent.toString());
					currentUser.setName(currentContent.toString());
					currentIssue.setAssignee(currentUser);
				}
				currentUser = null;
				currentContent = null;
			} else if (name.equals("qa_contact")) {
				currentIssue.set(Issue.QA_CONTACT, currentContent.toString());
				currentContent = null;
			} else if (name.equals("long_desc")) { // multiple
				currentIssue.addComments(currentComment);
				if (currentIssue.getDescription() == null) currentIssue.setDescription(currentComment.getTheText());
				currentComment = null;
			} else if (name.equals("commentid")) {
				currentComment.setId(currentContent.toString());
			} else if (name.equals("who")) {
				if (currentUser != null) {
					currentUser.setId(currentContent.toString());
					currentUser.setName(currentContent.toString());
					currentComment.setAuthor(currentUser);
					currentComment.setUpdateAuthor(currentUser);
				}
				currentUser = null;
				currentContent = null;
			} else if (name.equals("bug_when")) { // 2008-07-23 12:28:22
				try {
					currentComment.setCreationTimestamp(BugzillaUtils.parseDate(currentContent.toString()));
					currentComment.setUpdateTimestamp(BugzillaUtils.parseDate(currentContent.toString()));
				} catch (ParseException e) {
					getLog().error("Cannot parse this time: "+currentContent.toString());
				}
				currentContent = null;
			} else if (name.equals("thetext")) {
				currentComment.setTheText(currentContent.toString());
				currentContent = null;
			} else if (name.equals("bug_file_loc")) {
				currentIssue.set(Issue.BUG_FILE_LOCATION, currentContent.toString());
				currentContent = null;
			} else if (name.equals("attachment")) { // multiple
				currentAttachment = null;
			} else if (name.equals("attachid")) {
				if (currentAttachment != null) {
					currentAttachment.setId(currentContent.toString());
					currentAttachment.setUri(URI.create(getBaseUrl()+PAGES[BUGZILLA_GET_ATTACHMENT]+"?id="+currentAttachment.getId()));
					currentIssue.addAttachments(currentAttachment);
				} else if (currentComment != null) {
					currentComment.addAttachments(currentContent.toString());
				}
				currentContent = null;
			} else if (name.equals("date")) {
				currentContent = null;
			} else if (name.equals("desc")) {
				currentAttachment.setDescription(currentContent.toString());
				currentContent = null;
			} else if (name.equals("filename")) {
				currentAttachment.setFilename(currentContent.toString());
				currentContent = null;
			} else if (name.equals("type")) {
				currentAttachment.setType(currentContent.toString());
				currentContent = null;
			} else if (name.equals("cc")) { // multiple
				@SuppressWarnings("unchecked")
				Set<String> cc = (Set<String>)currentIssue.get(Issue.CC);
				if (cc == null) {
					cc = new HashSet<String>();
					currentIssue.set(Issue.CC, cc);
				}
				cc.add(currentContent.toString());
				currentContent = null;
			} else if (name.equals("blocked")) {
				currentIssue.addLinks(new DefaultLink(Type.DEPENDS_ON, Issue.BLOCKED_NAME, true, "Blocks", currentContent.toString()));
				currentContent = null;
			} else if (name.equals("dependson")) {
				currentIssue.addLinks(new DefaultLink(Type.DEPENDS_ON, Issue.DEPENDS_ON_NAME, true, "Depends on", currentContent.toString()));
				currentContent = null;
			} else if (name.equals("alias")) {
				currentIssue.set(Issue.ALIAS, currentContent.toString());
				currentContent = null;
			} else if (name.equals("status_whiteboard")) {
				currentIssue.set(Issue.WHITEBOARD, currentContent.toString());
				currentContent = null;
			} else if (name.equals("estimated_time")) {
				currentIssue.set(Issue.ESTIMATED_TIME, LangUtils.getDouble(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("remaining_time")) {
				currentIssue.set(Issue.REMAINING_TIME, LangUtils.getDouble(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("actual_time")) {
				currentIssue.set(Issue.ACTUAL_TIME, LangUtils.getDouble(currentContent.toString()));
				currentContent = null;
			} else if (name.equals("deadline")) {
				try {
					currentIssue.set(Issue.DEADLINE, BugzillaUtils.parseDate(currentContent.toString()));
				} catch (ParseException e) {
					getLog().error("Cannot parse this date: "+currentContent.toString());
				}
				currentContent = null;
			} else {
				if ((currentCustomField != null) && (currentContent != null)) {
					if (currentIssue != null) {
						currentIssue.set(currentCustomField, currentContent.toString());
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