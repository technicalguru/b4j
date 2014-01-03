/**
 * 
 */
package b4j.core.session.bugzilla.async;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.http.cookie.Cookie;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.httpclient.api.Request;

/**
 * Wraps an HTTP client and adds cookies to each request.
 * @author ralph
 *
 */
public class BugzillaHttpClient implements HttpClient {

	private HttpClient client;
	private Collection<Cookie> cookies = new ArrayList<Cookie>();
	
	/**
	 * Constructor.
	 */
	public BugzillaHttpClient(HttpClient client) {
		this.client = client;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Request newRequest() {
		return applyCookies(client.newRequest());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Request newRequest(URI uri) {
		return applyCookies(client.newRequest(uri));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Request newRequest(String uri) {
		return applyCookies(client.newRequest(uri));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Request newRequest(URI uri, String contentType, String entity) {
		return applyCookies(client.newRequest(uri, contentType, entity));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Request newRequest(String uri, String contentType, String entity) {
		return applyCookies(client.newRequest(uri, contentType, entity));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flushCacheByUriPattern(Pattern uriPattern) {
		client.flushCacheByUriPattern(uriPattern);
	}

	/**
	 * Sets the cookies to be applied for all REST calls.
	 * @param cookies cookies to be set
	 */
	public void setCookies(Collection<Cookie> cookies) {
		this.cookies.addAll(cookies);
	}
		
	/**
	 * Removes all cookies to be applied for REST calls.
	 */
	public void removeCookies() {
		this.cookies.clear();
	}
		
	/**
	 * Applies the cookies.
	 * @param request request to be modified
	 * @return the request object
	 */
	protected Request applyCookies(Request request) {
		StringBuilder s = new StringBuilder();
		for (Cookie cookie : cookies) {
			if (s.length() > 0) s.append(';');
			s.append(cookie.getName());
			s.append('=');
			s.append(cookie.getValue());
		}
		System.out.println("Cookie is "+s.toString());
		if (s.length() > 0) request.setHeader("Cookie", s.toString());
		return request;
	}
}
