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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

import b4j.core.Attachment;
import b4j.core.DefaultAttachment;
import b4j.core.Issue;
import b4j.core.SearchData;
import b4j.core.SearchResultCountCallback;
import b4j.core.ServerInfo;
import b4j.core.session.bugzilla.BugzillaClient;
import b4j.core.session.bugzilla.BugzillaRestClientFactory;
import b4j.core.session.bugzilla.async.AsyncBugzillaRestClientFactory;
import b4j.util.HttpClients;
import b4j.util.HttpSessionParams;

import com.atlassian.httpclient.api.HttpClient;

/**
 * Accesses Bugzilla via REST.
 * <p>Please read carefully documentation at 
 * <a href="http://www.bugzilla.org/docs/4.4/en/html/api/Bugzilla/WebService/Bug.html#search">Bugzilla::WebService:Bug</a> interface
 * in order to understand what parameters are valid for searching (e.g. "classification" is not allowed as of 4.4).</p>
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaRpcSession extends AbstractHttpSession {

	public static final String BUGZILLA_MINIMUM_VERSION = "4.4.0";

	private BugzillaClient client = null;
	private ServerInfo serverInfo = null;
	private URL baseUrl;

	/**
	 * Constructor.
	 */
	public BugzillaRpcSession() {
	}

	/**
	 * Configuration allows:<br/>
	 * &lt;bugzilla-home&gt;URL&lt;/bugzilla-home&gt; - the Bugzilla base URL<br/>
	 * &lt;proxy-host&gt; - HTTP proxy (optional)<br/>
	 * &lt;ProxyAuthorization&gt; - HTTP proxy authentication (optional)<br/>
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		super.configure(config);
		try {
			String home = config.getString("bugzilla-home");
			setBaseUrl(new URL(home));
		} catch (MalformedURLException e) {
			throw new ConfigurationException("Malformed JIRA URL: ", e);
		}
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
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLoggedIn() {
		return client != null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean open() {
		if (isLoggedIn()) return true;
		if (client == null) {
			client = createClient();
		}
		checkBugzillaVersion();

		HttpSessionParams sessionParams = getHttpSessionParams();
		if (sessionParams == null) sessionParams = new HttpSessionParams();
		if (sessionParams.hasProxy()) {
			if ((getLog() != null) && getLog().isDebugEnabled()) {
				getLog().debug("Using HTTP Proxy: "+sessionParams.getProxyHost()+":"+sessionParams.getProxyPort());
			}
		}
		
		if (sessionParams.hasAuthorization()) {
			getLog().debug("Authenticate with: "+sessionParams.getLogin());
			client.login(sessionParams.getLogin(), sessionParams.getPassword());
		}

		return isLoggedIn();
	}

	/**
	 * Creates the {@link BugzillaClient}.
	 * @return the client created
	 */
	protected BugzillaClient createClient() {
		BugzillaRestClientFactory factory = new AsyncBugzillaRestClientFactory();
		URI uri;
		try {
			uri = getBaseUrl().toURI();
		} catch (URISyntaxException e) {
			throw new RuntimeException("Cannot create URI", e);
		}
		HttpSessionParams sessionParams = getHttpSessionParams();
		if (sessionParams == null) sessionParams = new HttpSessionParams();

		HttpClient httpClient = HttpClients.createAtlassianClient(uri, sessionParams);
		return factory.create(uri, httpClient);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		if (client == null) return;
		if (client.getUser() != null) {
			client.logout();
		}
		client = null;
		serverInfo = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<Issue> searchBugs(SearchData searchData, SearchResultCountCallback callback) {
		checkLoggedIn();
		try {
			Map<String,Object> criteria = new HashMap<String, Object>();
			for (String key : searchData.getParameterNames()) {
				List<String> values = new ArrayList<String>();
				for (String value : searchData.get(key)) {
					values.add(value);
				}
				criteria.put(key, values);
			}
			return client.getBugClient().findBugs(criteria).get();
		} catch (Exception e) {
			throw new RuntimeException("Cannot search issues", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Issue getIssue(String id) {
		checkLoggedIn();
		try {
			Iterable<Issue> i = client.getBugClient().getBugs(Long.parseLong(id)).get();
			for (Issue rc : i) {
				return rc;
			}
		} catch (Exception e) {
			throw new RuntimeException("Cannot retrieve issue: "+id, e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getAttachment(Attachment attachment) throws IOException {
		checkLoggedIn();
		if (attachment instanceof DefaultAttachment) {
			return new ByteArrayInputStream(((DefaultAttachment)attachment).getContent());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMinimumBugzillaVersion() {
		return BUGZILLA_MINIMUM_VERSION;
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
	public String getBugzillaVersion() {
		checkLoggedIn();
		if (serverInfo == null) {
			try {
				serverInfo = client.getMetadataClient().getServerInfo().get();
			} catch (Exception e) {
				throw new RuntimeException("Cannot retrieve Server Information", e);
			}
		}
		if (serverInfo == null) return null;
		return serverInfo.getVersion();
	}

	/**
	 * Returns the client.
	 * @return the client
	 * @since 2.0.3
	 */
	protected BugzillaClient getClient() {
		return client;
	}

	/**
	 * Returns the serverInfo.
	 * @return the serverInfo
	 * @since 2.0.3
	 */
	protected ServerInfo getServerInfo() {
		return serverInfo;
	}


}
