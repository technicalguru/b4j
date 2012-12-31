/**
 * 
 */
package b4j.core.session;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcSun15HttpTransportFactory;
import org.apache.xmlrpc.client.XmlRpcTransportFactory;

import b4j.core.Configurable;
import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.LongDescription;
import b4j.core.SearchData;
import b4j.core.SearchResultCountCallback;
import b4j.util.BugzillaUtils;

/**
 * Provides access to JIRA issues.
 * @author U434983
 *
 */
public class XmlRpcJiraSession extends AbstractAuthorizedSession {

	private static final String RPC_PATH = "/rpc/xmlrpc";
	private static SimpleDateFormat COMMENT_DATE_FORMATTER = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);
	private static SimpleDateFormat JIRA_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private boolean loggedIn;
	private String cookie;
	private URL baseUrl;
	private String jiraVersion;
	private Proxy proxy;
	private AuthorizationCallback proxyAuthorizationCallback;
	private XmlRpcClient rpcClient;
	private Map<String,String> priorities;
	private Map<String,String> statuses;
	private Map<String,String> issueTypes;
	private Map<String,String> resolutions;
	private Map<String,String> users;
	private Map<String,String> teams;
	
	/**
	 * Default constructor
	 */
	public XmlRpcJiraSession() {
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
		String className = null;
		users = new HashMap<String, String>();
		teams = new HashMap<String, String>();
		try {
			setBaseUrl(new URL(config.getString("jira-home")));
			
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
				setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(s, port)));
				
				// Proxy authorization
				try {
					Configuration authCfg = ((HierarchicalConfiguration)config).configurationAt("ProxyAuthorizationCallback(0)");
					if (authCfg != null) {
						AuthorizationCallback callback = null;
						className = authCfg.getString("[@class]");
						if ((className.trim().length() == 0) 
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

				// Team definitions
				int idx = 0;
				while (true) {
					try {
						Configuration teamCfg = ((SubnodeConfiguration)config).configurationAt("Team("+idx+")");
						String name = teamCfg.getString("[@name]");
						String members[] = teamCfg.getStringArray("Member");
						if (members != null) {
							for (int i=0; i<members.length; i++) registerTeamMember(name, members[i]);
						}
						idx++;
					} catch (IllegalArgumentException e) {
						// No more teams
						break;
					}
				}
			}
		} catch (MalformedURLException e) {
			throw new ConfigurationException("Malformed JIRA URL: ", e);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException("Cannot find class: "+className, e);
		} catch (InstantiationException e) {
			throw new ConfigurationException("Cannot instantiate class: "+className, e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("Cannot access constructor: "+className, e);
		}
	}
	
	/**
	 * Registers a member for a team.
	 * @param team team name
	 * @param member member name
	 */
	public void registerTeamMember(String team, String member) {
		teams.put(member, team);
	}
	
	/**
	 * Closes the JIRA session.
	 * @see b4j.core.Session#close()
	 */
	@Override
	public void close() {
		if (!isLoggedIn()) return;
		try {
			rpcClient.execute("jira1.logout", new Object [] { cookie });
		} catch (XmlRpcException e) {
			getLog().error("Logout error: ", e);
		}
		setLoggedIn(false);
		cookie = null;
	}

	/**
	 * Returns the version of JIRA connected to.
	 * @see b4j.core.Session#getBugzillaVersion()
	 */
	@Override
	public String getBugzillaVersion() {
		return jiraVersion;
	}

	/**
	 * Returns the maximum JIRA version supported.
	 * @see b4j.core.Session#getMaximumBugzillaVersion()
	 */
	@Override
	public String getMaximumBugzillaVersion() {
		return null;
	}

	/**
	 * Returns the minimum JIRA version supported.
	 * @see b4j.core.Session#getMinimumBugzillaVersion()
	 */
	@Override
	public String getMinimumBugzillaVersion() {
		return null;
	}

	/**
	 * Sets the login status.
	 * @param loggedIn - the login status to set
	 */
	protected void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	/**
	 * Returns true if connection to JIRA was established.
	 * @see b4j.core.Session#isLoggedIn()
	 */
	@Override
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * Returns the XMLPRC token for JIRA session.
	 * @return the token
	 */
	public String getCookie() {
		return cookie;
	}
	
	/**
	 * Opens the JIRA session (login).
	 * @see b4j.core.Session#open()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean open() {
		if (isLoggedIn()) return true;
		jiraVersion = null;
		
		try {
			XmlRpcClientConfigImpl rpcConfig = new XmlRpcClientConfigImpl();
			rpcConfig.setServerURL(new URL(baseUrl + RPC_PATH));
			rpcClient = new XmlRpcClient();
			rpcClient.setConfig(rpcConfig); //
			rpcClient.setTransportFactory(new XmlRpcSun15ProxyAuthTransportFactory(rpcClient));
			
			// Proxy
			if (getProxy() != null) {
				XmlRpcTransportFactory factory = rpcClient.getTransportFactory();
				if (getLog().isDebugEnabled()) getLog().debug("TransportFactory="+factory.getClass().getName());
				((XmlRpcSun15HttpTransportFactory) factory).setProxy(getProxy());
			}
			
			String rc = (String)rpcClient.execute("jira1.login", new Object[] { getLogin(), getPassword() });
			if (rc != null) {
				setLoggedIn(true);
				cookie = rc;

				// getServerInfo()
				HashMap<String, Object> info = (HashMap<String, Object>)rpcClient.execute("jira1.getServerInfo", new Object [] { cookie });
				for (String key : info.keySet()) {
					getLog().debug("ServerInfo: "+key+" = "+info.get(key));
				}
				jiraVersion = info.get("version").toString();
				
				// Retrieve basic information
				retrieveIssueTypes();
				retrievePriorities();
				retrieveResolutions();
				retrieveStatuses();
			}
		} catch (MalformedURLException e) {
			getLog().error("URL problem:", e);
		} catch (XmlRpcException e) {
			getLog().error("Request problem:", e);
		}
		return isLoggedIn();
	}

	/**
	 * @see b4j.core.Session#getIssue(java.lang.String)
	 */
	@Override
	public Issue getIssue(String id) {
		SearchData search = new DefaultSearchData();
		search.add("key", id);
		Iterator<Issue> i = searchBugs(search, null);
		if ((i != null) && i.hasNext()) return i.next();
		return null;
	}

	/**
	 * Searches for JIRA issues.
	 * Search data must contain either "filterID" to query existing filters, or one or more "key"s
	 * of JIRA issues to return. Additional filter criteria can be given by assigning valid values
	 * to names of issue attributes returned by JIRA.
	 * @see b4j.core.Session#searchBugs(b4j.core.SearchData, b4j.core.SearchResultCountCallback)
	 */
	@Override
	public Iterator<Issue> searchBugs(SearchData searchData, SearchResultCountCallback callback) {
		if (!isLoggedIn()) throw new IllegalStateException("Session is closed");
		if (searchData.hasParameter("filterId")) {
			Iterator<String> it1 = searchData.get("filterId");
			List<String> filterList = new ArrayList<String>();
			while (it1.hasNext()) filterList.add(it1.next());
			
			return new JiraIteratorFromFilters(filterList, searchData);
		} else if (searchData.hasParameter("key")) {
			Iterator<String> it1 = searchData.get("key");
			List<String> keyList = new ArrayList<String>();
			while (it1.hasNext()) keyList.add(it1.next());
			
			return new JiraIteratorFromKeys(keyList);
		} else {
			getLog().error("no search data given");
		}
		return null;
	}

	/**
	 * A test function.
	 */
	protected void test() {
		try {
			Object rc = rpcClient.execute("jira1.getProjectsNoSchemes", new Object [] { cookie });
			debugObject("projects = ", rc);
			
			// BUSARC
			rc = rpcClient.execute("jira1.getIssuesFromTextSearch", new Object [] { cookie, "e" });
			debugObject("issues = ", rc);
		} catch (XmlRpcException e) {
			getLog().error("XMLRPC error: ", e);
		}
	}
	
	/**
	 * A DEBUG function to print out XML-RPC responses.
	 * @param prefixed prefix for logging
	 * @param o object to be debugged
	 */
	private void debugObject(String prefixed, Object o) {
		getLog().debug(prefixed+" "+o.getClass().getName());
		if (o.getClass().isArray()) {
			Object a[] = (Object[])o;
			for (int i=0; i<a.length; i++) {
				if (a[i] != null) {
					String ac = a[i].getClass().getName();
					getLog().debug("o["+i+"] <"+ac+"> = "+a[i]);
				} else {
					getLog().debug("o["+i+"] = NULL");
				}
			}
		} else {
			getLog().debug(o.toString());
		}
	}
	
	/**
	 * Retrieves all Issue Types.
	 * @throws XmlRpcException
	 */
	@SuppressWarnings("unchecked")
	protected void retrieveIssueTypes() throws XmlRpcException {
		Object rc[] = (Object[])rpcClient.execute("jira1.getIssueTypes", new Object [] { cookie });
		issueTypes = new HashMap<String, String>();
		for (int i=0; i<rc.length; i++) {
			HashMap<String, String> map = (HashMap<String, String>)rc[i];
			issueTypes.put(map.get("id"), map.get("name"));
		}
	}
	
	/**
	 * Retrieves all priorities.
	 * @throws XmlRpcException
	 */
	@SuppressWarnings("unchecked")
	protected void retrievePriorities() throws XmlRpcException {
		Object rc[] = (Object[])rpcClient.execute("jira1.getPriorities", new Object [] { cookie });
		priorities = new HashMap<String, String>();
		for (int i=0; i<rc.length; i++) {
			HashMap<String, String> map = (HashMap<String, String>)rc[i];
			priorities.put(map.get("id"), map.get("name"));
		}
	}
	
	/**
	 * Retrieves all statuses.
	 * @throws XmlRpcException
	 */
	@SuppressWarnings("unchecked")
	protected void retrieveStatuses() throws XmlRpcException {
		Object rc[] = (Object[])rpcClient.execute("jira1.getStatuses", new Object [] { cookie });
		statuses = new HashMap<String, String>();
		for (int i=0; i<rc.length; i++) {
			HashMap<String, String> map = (HashMap<String, String>)rc[i];
			statuses.put(map.get("id"), map.get("name"));
		}
	}
	
	/**
	 * Retrieves all resolutions.
	 * @throws XmlRpcException
	 */
	@SuppressWarnings("unchecked")
	protected void retrieveResolutions() throws XmlRpcException {
		Object rc[] = (Object[])rpcClient.execute("jira1.getResolutions", new Object [] { cookie });
		resolutions = new HashMap<String, String>();
		for (int i=0; i<rc.length; i++) {
			HashMap<String, String> map = (HashMap<String, String>)rc[i];
			resolutions.put(map.get("id"), map.get("name"));
		}
	}
	
	/**
	 * Returns the name of the given user.
	 * This will trigger a XML-RPC request if user was not retrieved before.
	 * @param userid JIRA ID of user
	 * @return name of user
	 */
	public String getUserName(String userid) {
		if (userid == null) return null;
		String rc = users.get(userid);
		if (rc != null) return rc;
		try {
			Object o = rpcClient.execute("jira1.getUser", new Object [] { cookie, userid });
			if (o != null) {
				//debugObject("User", o);
				Object name = ((HashMap<?,?>)o).get("fullname");
				if (name != null) {
					users.put(userid, name.toString());
					return name.toString();
				}
			}
		} catch (XmlRpcException e) {
			getLog().error("Cannot retrieve user: ", e);
		}
		users.put(userid, userid);
		return userid;
	}
	
	/**
	 * Returns the team of the user.
	 * @see #configure(Configuration)
	 * @param userId id of user
	 * @return name of team
	 */
	public String getTeam(String userId) {
		return teams.get(userId);
	}
	
	/**
	 * Retrieves a specific JIRA issue
	 * @param key JIRA key
	 * @return JIRA issue
	 */
	@SuppressWarnings("unchecked")
	public Issue requestIssue(String key) {
		try {
			Object o = rpcClient.execute("jira1.getIssue", new Object [] { cookie, key });
			return createIssue((HashMap<String,Object>)o);
		} catch (XmlRpcException e) {
			getLog().error("Cannot retrieve issue: "+key, e);
		}
		return null;
	}
	
	/**
	 * Retrieves the comments of given issue and add them as long descriptions.
	 * @param issue the issue to retrieve comments for.
	 */
	protected void retrieveComments(Issue issue) {
		try {
			Object o = rpcClient.execute("jira1.getComments", new Object [] { cookie, issue.getId() });
			if (o != null) {
				Object arr[] = (Object[])o;
				for (int i=0; i<arr.length; i++) {
					HashMap<?,?> map = (HashMap<?,?>)arr[i];
					LongDescription comment = issue.addLongDescription();
					comment.setTheText((String)map.get("body"));
					String s = (String)map.get("created");
					try {
						if (s != null) comment.setWhen(COMMENT_DATE_FORMATTER.parse(s));
						else comment.setWhen(issue.getCreationTimestamp());
					} catch (ParseException e) {
						getLog().error("Parsing date failed: "+s, e);
						comment.setWhen(new Date(0));
					}
					s = (String)map.get("author");
					if (s != null) comment.setWho(getUserName(s));
					else comment.setWho(issue.getReporter());
					s = (String)map.get("updated");
					try {
						if (s != null) comment.setLastUpdate(COMMENT_DATE_FORMATTER.parse(s));
					} catch (ParseException e) {
						getLog().error("Parsing date failed: ", e);
						comment.setLastUpdate(new Date(0));
					}
					
					
					comment.setUpdateAuthor(getUserName((String)map.get("updateAuthor")));
				}
			}
		} catch (XmlRpcException e) {
			getLog().error("Cannot retrieve comments: "+issue.getId(), e);
		}
	}
	
	/**
	 * Converts the XML-RPC response for a singe JIRA issue to an object
	 * @param map XML-RPC response map
	 * @return JIRA issue
	 */
	protected Issue createIssue(HashMap<String, Object> map) {
		Issue rc = createIssue();
		setFields(rc, map);
		setIssueType(rc, issueTypes);
		setPriority(rc, priorities);
		setResolution(rc, resolutions);
		setStatus(rc, statuses);
		String assignee = rc.getAssignee();
		if (assignee != null) {
			rc.setAssigneeName(getUserName(assignee));
			String team = getTeam(assignee);
			if (team != null) rc.setAssigneeTeam(team);
		}
		String reporter = rc.getReporter();
		if (reporter != null) {
			rc.setReporterName(getUserName(reporter));
			String team = getTeam(reporter);
			if (team != null) rc.setReporterTeam(team);
		}
		
		// get the comments
		retrieveComments(rc);
		
		return rc;
	}
	
	/**
	 * Sets all fields in {@link Issue} according to map retrieved by 
	 * JIRA's XML-RPC interface.
	 * Do not call unless you know what you do :)
	 * @param issue issue
	 * @param map map returned from XML-RPC interface
	 */
	@SuppressWarnings("unchecked")
	public void setFields(Issue issue, HashMap<String, Object> map) {
		 // summary=VD253 & VD254 Message Transmission - FAX and Email transmission - missing specification information
		 // status=6
		 // votes=0, 
		 // assignee=u298390, 
		 // customFieldValues=[Ljava.lang.Object;@dc6a77, 
		 // fixVersions=[Ljava.lang.Object;@d1e89e, 
		 // resolution=1, 
		 // type=20, 
		 // id=15602, 
		 // reporter=u298390, 
		 // project=LIDONG, 
		 // created=2009-09-04 14:04:37.0, 
		 // updated=2009-11-05 06:26:46.0, 
		 // description=
		 // priority=4, 
		 // components=[Ljava.lang.Object;@b8bef7, 
		 // affectsVersions=[Ljava.lang.Object;@1016632, 
		 // key=LIDONG-2719
		issue.setId((String)map.get("key"));
		//issue.setCustomField("id", (String)map.get("id"));
		issue.setReporter((String)map.get("reporter"));
		issue.setAssignee((String)map.get("assignee"));
		issue.setShortDescription((String)map.get("summary"));
		try {
			issue.setCreationTimestamp(JIRA_DATE_FORMATTER.parse((String)map.get("created")));
			issue.setDeltaTimestamp(JIRA_DATE_FORMATTER.parse((String)map.get("updated")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LongDescription desc = issue.addLongDescription();
		desc.setTheText((String)map.get("description"));
		for (String key : map.keySet()) {
			Object value = map.get(key);
			if (value instanceof String) issue.setCustomField(key, (String)value);
		}
		Object customFields[] = (Object[])map.get("customFieldValues");
		for (int i=0; i<customFields.length; i++) {
			HashMap<String, Object> custom = (HashMap<String, Object>)customFields[i];
			String name = (String)custom.get("customfieldId");
			Object v = custom.get("values");
			if (v instanceof String) issue.setCustomField(name, (String)v);
			else issue.addAllCustomFields(name, (List<String>)v);
			//System.out.println(i+"="+customFields[i]);
		}
		
		if (getLog().isDebugEnabled()) {
			StringBuffer s = new StringBuffer();
			BugzillaUtils.debugObject(s, map);
			getLog().debug(s.toString());
		}
	}

	/**
	 * Sets the priority's name for the issue.
	 * @param issue JIRA issue
	 * @param valueMap maps priority IDs to names
	 * @return the priority name
	 */
	public static String setPriority(Issue issue, Map<String,String> valueMap) {
		String value = valueMap.get(issue.getCustomFieldString("priority"));
		issue.setPriority(value);
		issue.setSeverity(value);
		return value;
	}
	
	/**
	 * Sets the status's name for the issue.
	 * @param issue JIRA issue
	 * @param valueMap maps status IDs to names
	 * @return the status name
	 */
	public static String setStatus(Issue issue, Map<String,String> valueMap) {
		String value = valueMap.get(issue.getCustomFieldString("status"));
		issue.setStatus(value);
		return value;
	}
	
	/**
	 * Sets the resolution's name for the issue.
	 * @param issue JIRA issue
	 * @param valueMap maps resolution IDs to names
	 * @return the resolution name
	 */
	public static String setResolution(Issue issue, Map<String,String> valueMap) {
		String value = valueMap.get(issue.getCustomFieldString("resolution"));
		if (value == null) value = "UNRESOLVED";
		issue.setResolution(value);
		return value;
	}
	
	/**
	 * Sets the issue type's name for the issue.
	 * @param issue JIRA issue
	 * @param valueMap maps issue type IDs to names
	 * @return the issue type name
	 */
	public static String setIssueType(Issue issue, Map<String,String> valueMap) {
		String value = valueMap.get(issue.getCustomFieldString("type"));
		issue.setTypeName(value);
		return value;
	}

	
	/**
	 * Returns the baseUrl.
	 * @return the baseUrl
	 */
	public URL getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Sets the baseUrl.
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(URL baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Returns the proxy.
	 * @return the proxy
	 */
	public Proxy getProxy() {
		return proxy;
	}

	/**
	 * Sets the proxy.
	 * @param proxy the proxy to set
	 */
	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
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
	public void setProxyAuthorizationCallback(
			AuthorizationCallback proxyAuthorizationCallback) {
		this.proxyAuthorizationCallback = proxyAuthorizationCallback;
	}


	protected class JiraIteratorFromFilters implements Iterator<Issue> {
		
		private SearchData searchData;
		private List<String> filterList;
		private Set<String> deliveredBugs;
		private Issue nextBug;
		private int currentFilter = 0;
		private Object currentFilterList[] = null;
		private int currentIndex = -1;
		
		/**
		 * Default constructor.
		 * @param filterList - bug ID list to retrieve
		 * @param searchData - search data for filtering
		 */
		public JiraIteratorFromFilters(List<String> filterList, SearchData searchData) {
			deliveredBugs = new HashSet<String>();
			this.filterList = filterList;
			this.searchData = searchData;
			nextBug = null;
		}
		
		/**
		 * Returns true while number of delivered bugs (calls
		 * to {@link #next()} are less than number of bugs in
		 * request list.
		 * @return true if there are still bugs in the queue to read.
		 */
		@Override
		public boolean hasNext() {
			if (nextBug == null) retrieveNextBug();
			return nextBug != null;
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
		public Issue next() {
			if (nextBug == null) retrieveNextBug();
			if (nextBug == null) throw new IllegalStateException("No more JIRA issues");
			Issue rc = nextBug;
			deliveredBugs.add(rc.getId());
			nextBug = null;
			return rc;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Remove is not suported");
		}
		
		@SuppressWarnings("unchecked")
		private void retrieveNextBug() {
			if (nextBug != null) next();
			if (currentFilterList != null) {
				if (currentIndex < currentFilterList.length) {
					HashMap<String, Object> map = (HashMap<String, Object>)currentFilterList[currentIndex];
					Issue issue = createIssue(map);
					currentIndex++;
					// Already in result set => get next bug
					if (deliveredBugs.contains(issue.getId()) || isHiddenBug(issue)) {
						retrieveNextBug();
					} else {
						nextBug = issue;
					}
				} else {
					// End of list => get next list
					getNextFilterList();
					if (currentFilterList != null) retrieveNextBug();
				}
			} else {
				// Empty filter list => get next list
				getNextFilterList();
				if (currentFilterList != null) retrieveNextBug();
			}
		}
		
		private void getNextFilterList() {
			if (currentFilter < filterList.size()) {
				try {
					// Retrieve the filter list from JIRA
					Object rc = rpcClient.execute("jira1.getIssuesFromFilter", new Object [] { cookie, filterList.get(currentFilter) });
					currentFilter++;
					currentFilterList = (Object[])rc;
					currentIndex = 0;
				} catch (XmlRpcException e) {
					getLog().error("Error while retrieving filter: ", e);
				}
			} else {
				currentFilterList = null;
			}
		}
		
		protected boolean isHiddenBug(Issue issue) {
			//System.out.println("isHiddenBug called");
			boolean hasFilterCriteria = false;
			Iterator<String> names = searchData.getParameterNames();
			while (names.hasNext()) {
				String name = names.next();
				if (name.equals("filterId")) continue;
				else {
					hasFilterCriteria = true;
					boolean found = false;
					Iterator<String> values = searchData.get(name);
					//System.out.println("Searching for "+name+" = "+values.toString());
					List<String> issueValues = issue.getCustomField(name);
					//System.out.println("  issue got values: "+issueValues);
					if (issueValues != null) {
						while (values.hasNext()) {
							String value = values.next();
							found |= issueValues.contains(value);
							//System.out.println("   found="+found+" for "+value);
						}
						if (found) return false;
					}
				}
			}
			if (hasFilterCriteria) return true;
			return false;
		}
	}
	
	protected class JiraIteratorFromKeys implements Iterator<Issue> {
		
		private Iterator<String> keyIterator;
		private Issue nextBug;
		
		/**
		 * Default constructor.
		 * @param keyList - bug ID list to retrieve
		 */
		public JiraIteratorFromKeys(List<String> keyList) {
			keyIterator = keyList.iterator();
			nextBug = null;
		}
		
		/**
		 * Returns true while number of delivered bugs (calls
		 * to {@link #next()} are less than number of bugs in
		 * request list.
		 * @return true if there are still bugs in the queue to read.
		 */
		@Override
		public boolean hasNext() {
			if (nextBug == null) retrieveNextBug();
			return nextBug != null;
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
		public Issue next() {
			if (nextBug == null) retrieveNextBug();
			if (nextBug == null) throw new IllegalStateException("No more JIRA issues");
			Issue rc = nextBug;
			nextBug = null;
			return rc;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Remove is not suported");
		}
		
		private void retrieveNextBug() {
			if (nextBug != null) next();
			while (keyIterator.hasNext() && (nextBug == null)) {
				nextBug = requestIssue(keyIterator.next());
			}
		}
		
	}

}
