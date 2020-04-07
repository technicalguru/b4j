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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.joda.time.DateTime;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.domain.BasicComponent;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.BasicIssueType;
import com.atlassian.jira.rest.client.domain.BasicPriority;
import com.atlassian.jira.rest.client.domain.BasicProject;
import com.atlassian.jira.rest.client.domain.BasicResolution;
import com.atlassian.jira.rest.client.domain.BasicStatus;
import com.atlassian.jira.rest.client.domain.BasicUser;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.domain.Version;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

import b4j.core.Attachment;
import b4j.core.DefaultAttachment;
import b4j.core.DefaultComment;
import b4j.core.Issue;
import b4j.core.SearchData;
import b4j.core.SearchResultCountCallback;
import b4j.core.session.jira.AsynchronousFilterRestClient;
import b4j.core.session.jira.JiraComponent;
import b4j.core.session.jira.JiraIssueType;
import b4j.core.session.jira.JiraPriority;
import b4j.core.session.jira.JiraProject;
import b4j.core.session.jira.JiraResolution;
import b4j.core.session.jira.JiraSeverity;
import b4j.core.session.jira.JiraStatus;
import b4j.core.session.jira.JiraTransformer;
import b4j.core.session.jira.JiraUser;
import b4j.core.session.jira.JiraUtils;
import b4j.core.session.jira.JiraVersion;
import b4j.util.MetaData;
import rs.baselib.lang.LangUtils;

/**
 * Provides access to JIRA issues.
 * @author ralph
 *
 */
public class JiraRpcSession extends AbstractAtlassianHttpClientSession {

	private boolean loggedIn;
	private URL baseUrl;
	private URI jiraServerUri;
	private String jiraVersion;
	private JiraRestClient jiraClient;
	private AsynchronousFilterRestClient filterClient;
	private MetaData<BasicIssueType, JiraIssueType> issueTypes = new MetaData<BasicIssueType, JiraIssueType>(new JiraTransformer.IssueType());
	private MetaData<BasicStatus, JiraStatus> status = new MetaData<BasicStatus, JiraStatus>(new JiraTransformer.Status());
	private MetaData<BasicPriority, JiraPriority> priorities = new MetaData<BasicPriority, JiraPriority>(new JiraTransformer.Priority());
	private MetaData<BasicPriority, JiraSeverity> severities = new MetaData<BasicPriority, JiraSeverity>(new JiraTransformer.Severity());
	private MetaData<BasicResolution, JiraResolution> resolutions = new MetaData<BasicResolution, JiraResolution>(new JiraTransformer.Resolution());
	private MetaData<BasicUser, JiraUser> users = new MetaData<BasicUser, JiraUser>(new JiraTransformer.User());
	private MetaData<BasicProject, JiraProject> projects = new MetaData<BasicProject, JiraProject>(new JiraTransformer.Project());
	private MetaData<BasicComponent, JiraComponent> components = new MetaData<BasicComponent, JiraComponent>(new JiraTransformer.Component());
	private MetaData<Version, JiraVersion> versions = new MetaData<Version, JiraVersion>(new JiraTransformer.Version());

	/**
	 * Default constructor
	 */
	public JiraRpcSession() {
		getHttpSessionParams().setBasicAuthentication(true);
	}

	/**
	 * Configuration allows:<br>
	 * &lt;jira-home&gt;URL&lt;/jira-home&gt; - the JIRA base URL<br>
	 * &lt;proxy-host&gt; - HTTP proxy (optional)<br>
	 * &lt;ProxyAuthorization&gt; - HTTP proxy authentication (optional)
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		super.configure(config);
		try {
			String home = config.getString("jira-home");
			if (home == null) home = config.getString("bugzilla-home");
			setBaseUrl(new URL(home));

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
			jiraServerUri = getBaseUrl().toURI();

			HttpClient httpClient = getHttpClient(jiraServerUri);
			jiraClient = factory.create(jiraServerUri, httpClient);

			jiraVersion = jiraClient.getMetadataClient().getServerInfo().get().getVersion();
			filterClient = new AsynchronousFilterRestClient(jiraServerUri, httpClient, jiraClient.getSearchClient());
			setLoggedIn(true);
		} catch (Exception e) {
			getLog().error("Cannot connect to JIRA:", e);
		}
		return isLoggedIn();
	}

	/**
	 * Returns Atlassian's REST filter client.
	 * @return the REST filter client
	 * @since 2.0.3
	 */
	protected AsynchronousFilterRestClient getFilterClient() {
		return filterClient;
	}

	/**
	 * Returns Atlassian's Jira client.
	 * @return the Jira client
	 * @since 2.0.3
	 */
	protected JiraRestClient getJiraClient() {
		return jiraClient;
	}

	/**
	 * Returns the Jira's server URI.
	 * @return the server URI
	 * @since 2.0.3
	 */
	protected URI getJiraServerUri() {
		return jiraServerUri;
	}

	/**
	 * Returns the {@link b4j.core.IssueType}  mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<BasicIssueType, JiraIssueType> getIssueTypes() {
		return issueTypes;
	}

	/**
	 * Returns the {@link b4j.core.Status} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<BasicStatus, JiraStatus> getStatus() {
		return status;
	}

	/**
	 * Returns the {@link b4j.core.Priority} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<BasicPriority, JiraPriority> getPriorities() {
		return priorities;
	}

	/**
	 * Returns the {@link b4j.core.Severity} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<BasicPriority, JiraSeverity> getSeverities() {
		return severities;
	}

	/**
	 * Returns the {@link b4j.core.Resolution} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<BasicResolution, JiraResolution> getResolutions() {
		return resolutions;
	}

	/**
	 * Returns the {@link b4j.core.User} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<BasicUser, JiraUser> getUsers() {
		return users;
	}

	/**
	 * Returns the {@link b4j.core.Project} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<BasicProject, JiraProject> getProjects() {
		return projects;
	}

	/**
	 * Returns the {@link b4j.core.Component} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<BasicComponent, JiraComponent> getComponents() {
		return components;
	}

	/**
	 * Returns the {@link b4j.core.Version} mappings.
	 * @return the mappings
	 * @since 2.0.3
	 */
	protected MetaData<Version, JiraVersion> getVersions() {
		return versions;
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
		setHttpClient(null);
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
	 * Creates an issue from the Jira issue.
	 * @param issue the JIRA issue
	 * @return the B4J issue 
	 * @since 2.0.3
	 */
	protected Issue createIssue(BasicIssue issue) {
		Issue rc = createIssue();
		rc.setId(issue.getKey());
		rc.setUri(issue.getSelf().toString());
		rc.setServerUri(jiraServerUri.toString());
		rc.setServerVersion(getBugzillaVersion());
		return rc;
	}

	/**
	 * Creates a issue from the Jira issue.
	 * @param issue the JIRA issue
	 * @return the B4J issue 
	 */
	protected Issue createIssue(com.atlassian.jira.rest.client.domain.Issue issue) {
		Issue rc = createIssue();
		rc.setId(issue.getKey());
		rc.setSummary(issue.getSummary());
		rc.setDescription(issue.getDescription());
		rc.setProject(projects.get(issue.getProject()));
		rc.addComponents(components.get(issue.getComponents(), rc.getProject()));
		rc.setAssignee(users.get(issue.getAssignee()));
		rc.setCreationTimestamp(JiraUtils.convertDate(issue.getCreationDate()));
		rc.setUri(issue.getSelf().toString());
		rc.setServerUri(jiraServerUri.toString());
		rc.setServerVersion(getBugzillaVersion());
		rc.setPriority(priorities.get(issue.getPriority()));
		rc.setReporter(users.get(issue.getReporter()));
		rc.setResolution(resolutions.get(issue.getResolution()));
		rc.setStatus(status.get(issue.getStatus()));
		rc.setSeverity(severities.get(issue.getPriority()));
		rc.setType(issueTypes.get(issue.getIssueType()));
		rc.addFixVersions(versions.get(issue.getFixVersions(), rc.getProject()));
		rc.addAffectedVersions(versions.get(issue.getAffectedVersions(), rc.getProject()));
		Iterable<com.atlassian.jira.rest.client.domain.Attachment> attachments = issue.getAttachments();
		if (attachments != null) {
			for (com.atlassian.jira.rest.client.domain.Attachment attachment : attachments) {
				Attachment a = new DefaultAttachment(rc.getId());
				a.setId(attachment.getSelf().toString());
				a.setFilename(attachment.getFilename());
				a.setDescription(attachment.getFilename());
				a.setDate(JiraUtils.convertDate(attachment.getCreationDate()));
				a.setType(attachment.getMimeType());
				a.setUri(attachment.getContentUri());
				rc.addAttachments(a);
			}
		}
		Iterable<com.atlassian.jira.rest.client.domain.Comment> comments = issue.getComments();
		if (comments != null) {
			for (com.atlassian.jira.rest.client.domain.Comment comment : comments) {
				DefaultComment desc = new DefaultComment(rc.getId());
				desc.setId(""+comment.getId());
				desc.setTheText(comment.getBody());
				desc.setCreationTimestamp(JiraUtils.convertDate(comment.getCreationDate()));
				desc.setAuthor(users.get(comment.getAuthor()));
				desc.setUpdateAuthor(users.get(comment.getAuthor()));
				desc.setUpdateTimestamp(JiraUtils.convertDate(comment.getUpdateDate()));
				rc.addComments(desc);
			}
		}
		rc.setUpdateTimestamp(JiraUtils.convertDate(issue.getUpdateDate()));
		DateTime d = issue.getDueDate();
		if (d != null) rc.set(Issue.DEADLINE, d.toDate());
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
			return getHttpClient().newRequest(attachment.getUri()).get().get().getEntityStream();
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
	 * Following parameters were added in V2.0.3:
	 * <ul>
	 * <li><code>startAt</code> - start at the given index when retrieving issues</li>
	 * <li><code>maxResults</code> - return at most that number of issues</li>
	 * <li><code>basicIssueOnly</code> - when present, returns issues that have Key and URI set only (for performance reasons)</li>
	 * </ul>
	 * Search queries take quite some time when used in default configuration. Jira returns key and URI in a search result which
	 * requires B4J to make separate calls for each individual issue found. You can control this by parameter {@code basicIssueOnly}
	 * which boosts performance a lot in case you are only interested in Issue keys.
	 */
	@Override
	public Iterable<Issue> searchBugs(SearchData searchData, SearchResultCountCallback callback) {
		checkLoggedIn();

		if (!searchData.hasParameter("filterId") && !searchData.hasParameter("jql") && !searchData.hasParameter("key")) {
			getLog().error("no search data given");
		} else {
			try {
				return new SearchIterator(searchData, callback);
			} catch (Exception e) {
				throw new RuntimeException("Cannot retrieve issues", e);
			}
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
	 * Return class for search operations.
	 * The class will issue several REST calls if required due to Jira's automatic search limitation.
	 * @author ralph
	 *
	 */
	protected class SearchIterator implements Iterable<Issue>, Iterator<Issue> {

		private SearchData searchData;
		private SearchResultCountCallback callback;

		private SearchResult result;
		private Iterator<BasicIssue> issues;
		private int resultCount = 0;
		private int maxResults = -1;
		private int startAt = 0;
		private int total = 0;
		private boolean basicOnly = false;
		
		public SearchIterator(SearchData searchData, SearchResultCountCallback callback) throws InterruptedException, ExecutionException {
			this.searchData = searchData;
			this.callback = callback;
			this.basicOnly = searchData.hasParameter("basicIssueOnly");
		}

		protected void retrieveNext() {
			try {
				if (result == null) {
					// First search call

					// Remind maximum number of records
					maxResults = -1;
					if (searchData.hasParameter("maxResults")) {
						maxResults = LangUtils.getInt(searchData.get("maxResults").iterator().next());
					}
					int max = maxResults > 0 ? maxResults : 50;
					startAt = 0;
					if (searchData.hasParameter("startAt")) {
						startAt = LangUtils.getInt(searchData.get("startAt").iterator().next());
					}

					// Create the promise
					result = getResult(startAt, max);

					// Callback for total results
					total = result.getTotal();
					if (callback != null) {
						callback.setResultCount(total);
					}

					// Get issues
					issues = result.getIssues().iterator();
					resultCount = 0;
				} else if (issues != null) {
					// Make the next call when no more issues in the current batch result
					if (!issues.hasNext()) {
						if (((maxResults < 0) || (resultCount < maxResults)) && (resultCount+startAt < total)) {
							int start = resultCount + startAt;
							int max   = maxResults < 0 ? 50 : maxResults - resultCount;

							if (max > 0) {
								// Create the promise
								result = getResult(start, max);

								// Get issues
								issues = result.getIssues().iterator();
							} else {
								// No more results
								issues = null;
							}
						} else {
							// No more results
							issues = null;
						}
					} else {
						// Just check we didn't exceed a maxCount
						if ((maxResults > 0) && (resultCount >= maxResults)) {
							// No more results
							issues = null;
						}
					}
				} // else: No more search results
			} catch (Exception e) {
				throw new RuntimeException("Cannot retrieve issues", e);
			}
		}
		
		/**
		 * Create the Jira REST call and get the result.
		 * @param startAt parameter for search
		 * @param maxResults parameter for search
		 * @return the search result
		 * @throws Exception when REST call fails
		 * @since 2.0.3
		 */
		protected SearchResult getResult(int startAt, int maxResults) throws Exception {
			// Create the promise
			Promise<SearchResult> promise = null;
			if (searchData.hasParameter("filterId")) {
				promise = filterClient.search(searchData.get("filterId").iterator().next(), maxResults, startAt);
			} else if (searchData.hasParameter("jql")) {
				String jql = searchData.get("jql").iterator().next();
				promise = jiraClient.getSearchClient().searchJql(jql, maxResults, startAt);
			} else if (searchData.hasParameter("key")) {
				promise = jiraClient.getSearchClient().searchJql("key in ("+join(searchData.get("key"))+")", maxResults, startAt);
			}
			if (promise != null) return promise.get();
			throw new Exception("No JQL parameters given in searchData (key, filzterId or jql");
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNext() {
			retrieveNext();
			if (issues != null) return issues.hasNext();
			return false;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Issue next() {
			resultCount++;
			BasicIssue issue = issues.next();
			if (!basicOnly) {
				String rc = issue.getKey();
				return getIssue(rc);
			}
			return createIssue(issue);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void remove() {
			throw new RuntimeException("Remove is not supported");
		}

		@Override
		public Iterator<Issue> iterator() {
			return this;
		}

	}
}
