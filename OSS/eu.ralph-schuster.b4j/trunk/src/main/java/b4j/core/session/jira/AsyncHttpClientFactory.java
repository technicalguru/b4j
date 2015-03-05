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

package b4j.core.session.jira;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.event.api.EventPublisher;
import com.atlassian.httpclient.apache.httpcomponents.DefaultHttpClient;
import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.httpclient.api.Request;
import com.atlassian.httpclient.api.factory.HttpClientOptions;
import com.atlassian.httpclient.spi.ThreadLocalContextManagers;
import com.atlassian.jira.rest.client.AuthenticationHandler;
import com.atlassian.sal.api.ApplicationProperties;
import com.atlassian.util.concurrent.Effect;

/**
 * Class is copied from Atlassian's {@link com.atlassian.jira.rest.client.internal.async.AsynchronousHttpClientFactory AsynchronousHttpClientFactory} 
 * class due to inability of configuring socket timeouts.
 * @author ralph
 *
 */
public class AsyncHttpClientFactory {

	private HttpClientOptions options = new HttpClientOptions();
	
	public AsyncHttpClientFactory() {
		options.setConnectionTimeout(60, TimeUnit.SECONDS);
		options.setRequestTimeout(60, TimeUnit.SECONDS);
		options.setSocketTimeout(60, TimeUnit.SECONDS);
	}
	
	/**
	 * Creates the client.
	 * @param serverUri uri of server
	 * @param authenticationHandler auth handler
	 * @return the client
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HttpClient createClient(final URI serverUri, final AuthenticationHandler authenticationHandler) {
		HttpClientOptions options = getHttpClientOptions();
		options.setRequestPreparer(new Effect<Request>() {
			@Override
			public void apply(final Request request) {
				authenticationHandler.configure(request);
			}
		});
		return new DefaultHttpClient(new NoOpEventPublisher(),
				new RestClientApplicationProperties(serverUri),
				ThreadLocalContextManagers.noop(), options);
	}

	/**
	 * Returns the options being used for config of {@link HttpClient}.
	 * @return the options
	 */
	public HttpClientOptions getHttpClientOptions() {
		return options;
	}
	
	private static class NoOpEventPublisher implements EventPublisher {
		@Override
		public void publish(Object o) {
		}

		@Override
		public void register(Object o) {
		}

		@Override
		public void unregister(Object o) {
		}

		@Override
		public void unregisterAll() {
		}
	}

	/**
	 * These properties are used to present JRJC as a User-Agent during http requests.
	 */
	private static class RestClientApplicationProperties implements ApplicationProperties {

		private final String baseUrl;

		private RestClientApplicationProperties(URI jiraURI) {
			this.baseUrl = jiraURI.getPath();
		}

		@Override
		public String getBaseUrl() {
			return baseUrl;
		}

		@Override
		public String getDisplayName() {
			return "Atlassian JIRA Rest Java Client";
		}

		@Override
		public String getVersion() {
			return MavenUtils.getVersion("com.atlassian.jira", "jira-rest-java-client");
		}

		@Override
		public Date getBuildDate() {
			// TODO implement using MavenUtils, JRJC-123
			throw new UnsupportedOperationException();
		}

		@Override
		public String getBuildNumber() {
			// TODO implement using MavenUtils, JRJC-123
			return String.valueOf(0);
		}

		@Override
		public File getHomeDirectory() {
			return new File(".");
		}

		@SuppressWarnings("deprecation")
		@Override
		public String getPropertyValue(final String s) {
			throw new UnsupportedOperationException("Not implemented");
		}
	}

	private static final class MavenUtils {
		private static final Logger logger = LoggerFactory.getLogger(MavenUtils.class);

		private static final String UNKNOWN_VERSION = "unknown";

		static String getVersion(String groupId, String artifactId) {
			final Properties props = new Properties();
			InputStream resourceAsStream = null;
			try {
				resourceAsStream = MavenUtils.class.getResourceAsStream(String
						.format("/META-INF/maven/%s/%s/pom.properties", groupId, artifactId));
				props.load(resourceAsStream);
				return props.getProperty("version", UNKNOWN_VERSION);
			} catch (Exception e) {
				logger.debug("Could not find version for maven artifact {}:{}", groupId, artifactId);
				logger.debug("Got the following exception", e);
				return UNKNOWN_VERSION;
			} finally {
				if (resourceAsStream != null) {
					try {
						resourceAsStream.close();
					} catch (IOException ioe) {
						// ignore
					}
				}
			}
		}
	}
}
