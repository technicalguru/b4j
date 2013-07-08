/**
 * 
 */
package b4j.core.session;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.joda.time.DateTime;

import rs.baselib.configuration.ConfigurationUtils;
import b4j.core.Attachment;
import b4j.core.Issue;
import b4j.core.LongDescription;
import b4j.core.SearchData;
import b4j.core.SearchResultCountCallback;
import b4j.core.session.jira.AsynchronousFilterRestClient;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.AuthenticationHandler;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.BasicResolution;
import com.atlassian.jira.rest.client.domain.Comment;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

/**
 * Provides access to JIRA issues.
 * @author ralph
 *
 */
public class XmlRpcJiraSession extends AbstractAuthorizedSession {

	private boolean loggedIn;
	private URL baseUrl;
	private String jiraVersion;
	private HttpClient httpClient;
	private JiraRestClient jiraClient;
	private AsynchronousFilterRestClient filterClient;
	private Proxy proxy;
	private AuthorizationCallback proxyAuthorizationCallback;
	
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
							className = DefaultAuthorizationCallback.class.getName();
						}
						callback = (AuthorizationCallback)ConfigurationUtils.load(className, authCfg, true);
						setProxyAuthorizationCallback(callback);
					}
				} catch (IllegalArgumentException e) {
					// No auth config for proxy
				}

			}
		} catch (MalformedURLException e) {
			throw new ConfigurationException("Malformed JIRA URL: ", e);
		}
	}
	
	/**
	 * Opens the JIRA session (login).
	 * @see b4j.core.Session#open()
	 */
	@Override
	public boolean open() {
		if (isLoggedIn()) return true;
		jiraVersion = null;
		
		try {
			JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
			URI jiraServerUri = getBaseUrl().toURI();
			AuthenticationHandler authenticationHandler = new BasicHttpAuthenticationHandler(getLogin(), getPassword());
			httpClient = new AsynchronousHttpClientFactory().createClient(jiraServerUri, authenticationHandler);
			jiraClient = factory.create(jiraServerUri, httpClient);
			//jiraVersion = jiraClient.getMetadataClient().getServerInfo().get().getVersion();
			filterClient = new AsynchronousFilterRestClient(jiraServerUri, httpClient, jiraClient.getSearchClient());
			setLoggedIn(true);
		//} catch (ExecutionException e) {
		//	getLog().error("Cannot login as "+getLogin(), e);
		} catch (Exception e) {
			getLog().error("Cannot connect to JIRA:", e);
		}
		return isLoggedIn();
	}

	/**
	 * Closes the JIRA session.
	 * @see b4j.core.Session#close()
	 */
	@Override
	public void close() {
		if (!isLoggedIn()) return;
		jiraClient = null;
		filterClient = null;
		httpClient = null;
		setLoggedIn(false);
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
	 * @see b4j.core.Session#getIssue(java.lang.String)
	 */
	@Override
	public Issue getIssue(String id) {
		try {
			Promise<com.atlassian.jira.rest.client.domain.Issue> promise = jiraClient.getIssueClient().getIssue(id);
			com.atlassian.jira.rest.client.domain.Issue issue = promise.get();
			return createIssue(issue);
		} catch (Exception e) {
			throw new RuntimeException("Cannot retrieve issue: "+id, e);
		}
	}

	/**
	 * Creates a issue from the Jira issue.
	 * @param issue the JIRA issue
	 * @return the B4J issue 
	 */
	protected Issue createIssue(com.atlassian.jira.rest.client.domain.Issue issue) {
		Issue rc = createIssue();
		rc.setId(issue.getKey());
		rc.setShortDescription(issue.getSummary());
		LongDescription desc = rc.addLongDescription();
		desc.setId(issue.getKey());
		desc.setTheText(issue.getDescription());
		desc.setLastUpdate(issue.getCreationDate().toDate());
		desc.setWhen(issue.getCreationDate().toDate());
		desc.setWho(issue.getReporter().getName());
		desc.setUpdateAuthor(issue.getReporter().getName());
		rc.setComponent(""+issue.getComponents());
		rc.setAssignee(issue.getAssignee().getName());
		rc.setAssigneeName(issue.getAssignee().getDisplayName());
		rc.setCreationTimestamp(issue.getCreationDate().toDate());
		rc.setBugzillaUri(issue.getSelf().toString());
		rc.setPriority(issue.getPriority().getName());
		rc.setProduct(issue.getProject().getName());
		rc.setReporter(issue.getReporter().getName());
		rc.setReporterName(issue.getReporter().getDisplayName());
		BasicResolution res = issue.getResolution();
		if (res != null) rc.setResolution(res.getName());
		rc.setStatus(issue.getStatus().getName());
		rc.setSeverity(issue.getPriority().getName());
		rc.setType(issue.getIssueType().getId());
		rc.setTypeName(issue.getIssueType().getName());
		rc.setVersion(join(issue.getFixVersions()));
		for (com.atlassian.jira.rest.client.domain.Attachment attachment : issue.getAttachments()) {
			Attachment a = rc.addAttachment();
			a.setId(attachment.getSelf().toString());
			a.setFilename(attachment.getFilename());
			a.setDescription(attachment.getFilename());
			a.setDate(attachment.getCreationDate().toDate());
			a.setType(attachment.getMimeType());
			a.setUri(attachment.getContentUri());
		}
		for (Comment comment : issue.getComments()) {
			desc = rc.addLongDescription();
			desc.setId(""+comment.getId());
			desc.setTheText(comment.getBody());
			desc.setWhen(comment.getCreationDate().toDate());
			desc.setWho(comment.getAuthor().getName());
			desc.setLastUpdate(comment.getUpdateDate().toDate());
			desc.setUpdateAuthor(comment.getUpdateAuthor().getName());
		}
		rc.setDeltaTimestamp(issue.getUpdateDate().toDate());
		DateTime d = issue.getDueDate();
		if (d != null) rc.setDeadline(d.toDate());
		return rc;
	}
	
	protected String join(Iterable<?> i) {
		StringBuffer rc = new StringBuffer();
		for (Object o : i) {
			if (rc.length() > 0) rc.append(',');
			rc.append(o.toString());
		}
		return rc.length() > 0 ? rc.toString() : null;
	}
	
	protected String join(Iterator<?> i) {
		StringBuffer rc = new StringBuffer();
		while (i.hasNext()) {
			Object o = i.next();
			if (rc.length() > 0) rc.append(',');
			rc.append(o.toString());
		}
		return rc.length() > 0 ? rc.toString() : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getAttachment(Attachment attachment) throws IOException {
		try {
			return httpClient.newRequest(attachment.getUri()).get().get().getEntityStream();
		} catch (InterruptedException e) {
			throw new IOException("Request was interrupted", e);
		} catch (ExecutionException e) {
			throw new IOException("Request could not be processed", e);
		}
	}
	
	/**
	 * Searches for JIRA issues.
	 * Search data can contain:
	 * <ul>
	 * <li><code>filterId</code> - to query existing filters</li>
	 * <li><code>jql</code> - to query with JQL</li>
	 * <li><code>key</code>'s - to query multiple, defined issues</li>
	 * </ul>
	 */
	@Override
	public Iterator<Issue> searchBugs(SearchData searchData, SearchResultCountCallback callback) {
		if (!isLoggedIn()) throw new IllegalStateException("Session is closed");
		Promise<SearchResult> result = null;
		if (searchData.hasParameter("filterId")) {
			result = filterClient.search(searchData.get("filterId").next());
		} else if (searchData.hasParameter("jql")) {
			result = jiraClient.getSearchClient().searchJql(searchData.get("jql").next());
		} else if (searchData.hasParameter("key")) {
			result = jiraClient.getSearchClient().searchJql("key in ("+join(searchData.get("key"))+")");
		} else {
			getLog().error("no search data given");
		}
		if (result != null)
			try {
				return new SearchIterator(result);
			} catch (Exception e) {
				throw new RuntimeException("Cannot retrieve issues", e);
			}
		return null;
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
	public void setProxyAuthorizationCallback(AuthorizationCallback proxyAuthorizationCallback) {
		this.proxyAuthorizationCallback = proxyAuthorizationCallback;
	}

	protected class SearchIterator implements Iterator<Issue> {

		private SearchResult result;
		private Iterator<BasicIssue> issues;
		
		public SearchIterator(Promise<SearchResult> result) throws InterruptedException, ExecutionException {
			this.result = result.get();
			issues = this.result.getIssues().iterator();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			return issues.hasNext();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Issue next() {
			return getIssue(issues.next().getKey());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new RuntimeException("Remove is not supported");
		}
		
	}
}
