/**
 * 
 */
package b4j.core.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;

import b4j.core.Attachment;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.SearchData;
import b4j.core.SearchResultCountCallback;
import b4j.core.session.jira.JiraXmlHandler;
import b4j.util.UrlParameters;

/**
 * @author Ralph Schuster
 *
 */
public class HttpJiraSession extends AbstractHttpSession {

	protected static String JIRA_LOGIN = "/login.jsp";
	protected static String JIRA_LOGOUT = "/logout";
	protected static String JIRA_DASHBOARD = "/secure/Dashboard.jspa";
	//type=20&pid=10012&status=1&status=3&status=10001&sorter/field=issuekey&sorter/order=DESC&tempMax=1000
	protected static String JIRA_SEARCH = "/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml";
	protected static String JIRA_MY_ISSUES = "resolution=-1&assigneeSelect=issue_current_user&sorter/field=created&sorter/order=ASC&sorter/field=priority&sorter/order=DESC&tempMax=1000";
	protected static String JIRA_FILTER = "/sr/jira.issueviews:searchrequest-xml/%s/SearchRequest-%s.xml?tempMax=1000";
	protected static String JIRA_ISSUE = "/si/jira.issueviews:issue-xml/%s/%s";
	private Map<String,String> teams;
	private static UrlParameters DEFAULT_SEARCH_PARAMETERS;

	/**
	 * 
	 */
	public HttpJiraSession() {
	}

	/**
	 * Configuration allows:<br/>
	 * &lt;jira-home&gt;URL&lt;/jira-home&gt; - the JIRA base URL<br/>
	 * &lt;proxy-host&gt; - HTTP proxy (proxy authorization not possible yet)<br/>
	 * &lt;team name="NAME"&gt;<br/>
	 * &lt;Member name="FULLNAME"&gtUID&lt;/Member&gt;<br/>
	 * &lt;/Team&gt; - team and member UID associations 
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		super.configure(config);
		teams = new HashMap<String, String>();

		// Team definitions
		int idx = 0;
		while (true) {
			try {
				Configuration teamCfg = ((HierarchicalConfiguration)config).configurationAt("Team("+idx+")");
				String name = teamCfg.getString("[@name]");
				String members[] = teamCfg.getStringArray("Member");
				if (members != null) {
					for (int i=0; i<members.length; i++) teams.put(members[i], name);
				}
				idx++;
			} catch (IllegalArgumentException e) {
				// No more teams
				break;
			}
		}
	}


	/**
	 * Closes the JIRA session.
	 * @see b4j.core.Session#close()
	 */
	@Override
	public void close() {
		if (!isLoggedIn()) return;
		try {
			// make a connection
			HttpURLConnection con = getConnection(JIRA_LOGOUT);
			con.getResponseCode();
		} catch (IOException e) {

		}
		super.close();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMaximumBugzillaVersion() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMinimumBugzillaVersion() {
		return null;
	}

	/**
	 * Returns default properties for JIRA access.
	 */
	protected Map<String,String> getDefaultRequestProperties() {
		Map<String,String> rc = new HashMap<String, String>();
		rc.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2) Gecko/20100115 Firefox/3.6 (.NET CLR 3.5.30729)");
		rc.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		rc.put("Accept-Language", "de-de,de;q=0.8,en-us;q=0.5,en;q=0.3");
		rc.put("Accept-Encoding", "gzip,deflate");
		rc.put("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		return rc;
	}

	/**
	 * Opens the HTTP Session.
	 * @see b4j.core.Session#open()
	 */
	@Override
	public boolean open() {
		if (isLoggedIn()) return true;
		setBugzillaVersion(null);

		try {
			UrlParameters params = new UrlParameters();
			String firstPage = JIRA_DASHBOARD;
			boolean authorized = false;
			if ((getLogin() != null) && (getPassword() != null)) {
				// os_username = xxx
				// os_password = xxx
				// os_destination = /secure/
				authorized = true;
				params.setParameter("os_username", getLogin());
				params.setParameter("os_password", getPassword());
				params.setParameter("os_destination", "/secure/");
				firstPage = JIRA_LOGIN;
			}
			String paramString = params.getUrlEncodedString();

			// make a POST request
			Map<String,String> requestProperties = getDefaultRequestProperties();
			if (authorized) {
				requestProperties.put("Referer", getBaseUrl()+JIRA_DASHBOARD);
			}
			HttpURLConnection con = getConnection(firstPage, paramString, requestProperties, false);

			// Read the response;
			if ((con.getResponseCode() == 200) || (con.getResponseCode() == 302)) {
				//debugResponse(con);
				boolean rc = retrieveCookies(con);

				/* The JIRA version cannot be extracted from this response...do it now*/
				con = getConnection(JIRA_SEARCH, JIRA_MY_ISSUES);
				if (con.getResponseCode() == 200) {
					// Get JIRA version and test for compatibility
					BufferedReader r = new BufferedReader(new InputStreamReader(con.getInputStream()));
					Pattern p = Pattern.compile(".*<version>([\\d\\.]+)<\\/version>.*", Pattern.CASE_INSENSITIVE);

					String line;
					while ((line = r.readLine()) != null) {
						Matcher m = p.matcher(line);
						if (m.matches()) {
							setBugzillaVersion(m.group(1));
							break;
						}
					}
				} else {
					getLog().error("Cound not detect JIRA version.");
				}

				setLoggedIn(rc);
				if (getLog().isInfoEnabled()) {
					if (rc) {
						getLog().info("Session opened:   "+getBaseUrl());
						if (getLog().isDebugEnabled()) getLog().debug("JIRA-Version: "+getBugzillaVersion());
					} else getLog().info("JIRA did not sent Cookie");
				}

				if (rc) checkBugzillaVersion();
				return rc;

			} else {
				getLog().error("Response was: "+con.getResponseCode()); 
			}
		} catch (IOException e) {
			getLog().error("Cannot open session:", e);			
		}
		return false;
	}


	/**
	 * @see b4j.core.Session#getIssue(java.lang.String)
	 */
	@Override
	public Issue getIssue(String id) {
		SearchData search = new DefaultSearchData();
		search.add("issueId", id);
		Iterator<Issue> i = searchBugs(search, null);
		if ((i != null) && i.hasNext()) return i.next();
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getAttachment(Attachment attachment) throws IOException {
		return null; // TODO
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Issue> searchBugs(SearchData searchData, SearchResultCountCallback callback) {
		// mapping IDs to names: 
		// $BASEURL/secure/IssueNavigator.jspa?mode=show&createNew=true
		// SEARCH request:
		// $BASEURL/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?type=5&pid=10050&priority=1&priority=3&status=1&status=3&resolution=-1&resolution=1&sorter/field=issuekey&sorter/order=DESC&tempMax=1000
		// SINGLE request:
		// $BASEURL/si/jira.issueviews:issue-xml/BUSARC-451/BUSARC-451.xml

		// Perform the search
		try {
			String paramString = null;
			String url = null;
			if (searchData.hasParameter("filterId")) {
				String filterId = searchData.get("filterId").next();
				url = JIRA_FILTER;
				url = url.replaceAll("%s", filterId);
			} else if (searchData.hasParameter("issueId")) {
				String issueId = searchData.get("issueId").next();
				url = JIRA_ISSUE;
				url = url.replaceAll("%s", issueId);
			} else {
				UrlParameters params = UrlParameters.createUrlParameters(searchData);
				params.addDefaultParameters(getDefaultSearchParameters());
				paramString = params.getUrlEncodedString();
				url = JIRA_SEARCH;
			}

			// make a connection
			HttpURLConnection con = getConnection(url, paramString);

			// Read the response;
			if (con.getResponseCode() == 200) {
				//debugResponse(con);

				//Create the handler and start parsing the XML response
				JiraXmlHandler handler = new JiraXmlHandler(this);
				handler.startParsing(con);

				// It is important to first read until <issue> tag was read as this will
				// declare how many items are in there.
				int issueCount = -1;
				while (issueCount < 0) {
					try {
						Thread.sleep(300);
						issueCount = handler.getSize();
					} catch (InterruptedException e) {
						issueCount = 0;
					}
				}

				// Create the iterator if required and return
				if (issueCount > 0) {
					getLog().debug(issueCount+" JIRA issues found");
					return new JiraIssueIterator(handler);
				} else {
					getLog().info("No JIRA issues found");
				}

			} else {
				getLog().debug("Response invalid: "+con.getResponseCode());
			}

		} catch (Exception e) {
			getLog().error("Cannot perform search:", e);
		}
		return null;
	}

	/**
	 * Makes a request to Bugzilla without any GET parameters.
	 * @param bugzillaPage - ID of page to make the request to.
	 * @return HTTP connection object
	 */
	protected HttpURLConnection getConnection(String bugzillaPage) {
		return getConnection(bugzillaPage, null, null, true);
	}

	/**
	 * Makes a request to Bugzilla including eventual GET parameters.
	 * The method applies all cookies registered previously to allow
	 * a successful session connection.
	 * @param jiraPage URL path to request
	 * @param getParams param string to be appended
	 * @param requestProperties properties of the request
	 * @param isGet true when a GET request shall be prepared, fals for a POST request
	 * @return HTTP connection object
	 */
	protected HttpURLConnection getConnection(String jiraPage, String getParams, Map<String, String> requestProperties, boolean isGet) {
		// Some JIRA specific: Do not follow redirects...
		HttpURLConnection.setFollowRedirects(false);
		return super.getConnection(getBaseUrl()+jiraPage, getParams, requestProperties, isGet);
	}

	/**
	 * Returns default search parameters.
	 * DO NOT modify these defaults!
	 * @return default search parameters.
	 */
	public static UrlParameters getDefaultSearchParameters() {
		if (DEFAULT_SEARCH_PARAMETERS == null) {
			DEFAULT_SEARCH_PARAMETERS = new UrlParameters();
			DEFAULT_SEARCH_PARAMETERS.setParameter("sorter/field", "issuekey");
			DEFAULT_SEARCH_PARAMETERS.setParameter("sorter/order", "DESC");
			DEFAULT_SEARCH_PARAMETERS.setParameter("tempMax", "1000");
		}
		return DEFAULT_SEARCH_PARAMETERS;
	}

	/**
	 * The iterator class returned.
	 * @author Ralph Schuster
	 *
	 */
	public static class JiraIssueIterator implements Iterator<Issue> {

		private JiraXmlHandler handler;

		public JiraIssueIterator(JiraXmlHandler handler) {
			this.handler = handler;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return handler.hasNext();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Issue next() {
			return handler.next();
		}

		/**
		 * Not supported.
		 * Throws an exception.
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			// Not supported
			throw new UnsupportedOperationException("Remove is not supported");
		}

	}
}

/*************************************************************************
   CREATING A NEW SUB-TASK

POST /secure/CreateSubTaskIssueDetails.jspa HTTP/1.1
Host: jira.dev.lsy.pl:8080
User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 (.NET CLR 3.5.30729)
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,* /*;q=0.8
Accept-Language: de-de,de;q=0.8,en-us;q=0.5,en;q=0.3
Accept-Encoding: gzip,deflate
Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
Keep-Alive: 115
Proxy-Connection: keep-alive
Referer: http://jira.dev.lsy.pl:8080/secure/CreateSubTaskIssue.jspa?parentIssueId=19330&pid=10050&issuetype=5
Cookie: JSESSIONID=92762910D49241B88138AF384F0F88E5
Content-Type: multipart/form-data; boundary=---------------------------105733156123245
Content-Length: 2332
-----------------------------105733156123245
Content-Disposition: form-data; name="summary"

Test Task
-----------------------------105733156123245
Content-Disposition: form-data; name="priority"

4
-----------------------------105733156123245
Content-Disposition: form-data; name="duedate"

15/Apr/10
-----------------------------105733156123245
Content-Disposition: form-data; name="customfield_10030"


-----------------------------105733156123245
Content-Disposition: form-data; name="components"

10344
-----------------------------105733156123245
Content-Disposition: form-data; name="versions"

10360
-----------------------------105733156123245
Content-Disposition: form-data; name="fixVersions"

10361
-----------------------------105733156123245
Content-Disposition: form-data; name="assignee"

ralph
-----------------------------105733156123245
Content-Disposition: form-data; name="reporter"

ralph
-----------------------------105733156123245
Content-Disposition: form-data; name="environment"

SPEC
-----------------------------105733156123245
Content-Disposition: form-data; name="description"

This is a test task
-----------------------------105733156123245
Content-Disposition: form-data; name="timetracking"

1h
-----------------------------105733156123245
Content-Disposition: form-data; name="attachment_field.1"; filename=""
Content-Type: application/octet-stream


-----------------------------105733156123245
Content-Disposition: form-data; name="attachment_field.2"; filename=""
Content-Type: application/octet-stream


-----------------------------105733156123245
Content-Disposition: form-data; name="attachment_field.3"; filename=""
Content-Type: application/octet-stream


-----------------------------105733156123245
Content-Disposition: form-data; name="pid"

10050
-----------------------------105733156123245
Content-Disposition: form-data; name="issuetype"

5
-----------------------------105733156123245
Content-Disposition: form-data; name="parentIssueId"

19330
-----------------------------105733156123245
Content-Disposition: form-data; name="viewIssueKey"


-----------------------------105733156123245
Content-Disposition: form-data; name="Create"

Create
-----------------------------105733156123245--

 */

/*********************************************************
   ANSWER (NOTE THE ID!) 

HTTP/1.0 302 Moved Temporarily
Server: Apache-Coyote/1.1
Cache-Control: no-cache, no-store, must-revalidate
Pragma: no-cache
Expires: Thu, 01 Jan 1970 00:00:00 GMT
Location: http://jira.dev.lsy.pl:8080/browse/BUSARC-1033
Content-Type: text/html;charset=UTF-8
Content-Length: 0
Date: Thu, 08 Apr 2010 10:50:10 GMT
X-Cache: MISS from proxy2.lsy.pl
Connection: keep-alive
Proxy-Connection: keep-alive
 ******************************************************/

/*******************************************************
   LINKING TWO ISSUES

POST /secure/LinkExistingIssue.jspa HTTP/1.1
Host: jira.dev.lsy.pl:8080
User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; de; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 (.NET CLR 3.5.30729)
Accept: text/html,application/xhtml+xml,application/xml;q=0.9,* /*;q=0.8
Accept-Language: de-de,de;q=0.8,en-us;q=0.5,en;q=0.3
Accept-Encoding: gzip,deflate
Accept-Charset: ISO-8859-1,utf-8;q=0.7,*;q=0.7
Keep-Alive: 115
Proxy-Connection: keep-alive
Referer: http://jira.dev.lsy.pl:8080/secure/LinkExistingIssue!default.jspa?id=22177
Cookie: JSESSIONID=92762910D49241B88138AF384F0F88E5
Content-Type: application/x-www-form-urlencoded
Content-Length: 91
linkDesc=is+dependency+of&linkKey=LIDONG-5449%2C+&comment=&commentLevel=&id=22177&Link=Link

 */