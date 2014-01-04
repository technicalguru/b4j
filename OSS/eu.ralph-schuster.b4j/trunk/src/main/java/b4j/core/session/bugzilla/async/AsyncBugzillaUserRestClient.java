/**
 * 
 */
package b4j.core.session.bugzilla.async;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.codehaus.jettison.json.JSONObject;

import b4j.core.User;
import b4j.core.session.bugzilla.BugzillaUserRestClient;
import b4j.core.session.bugzilla.json.BugzillaIdParser;
import b4j.core.session.bugzilla.json.BugzillaUserParser;
import b4j.util.LazyRetriever;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.RestClientException;
import com.atlassian.util.concurrent.Promise;

/**
 * The client responsible for getting classification information.
 * @author ralph
 * @since 2.0
 *
 */
public class AsyncBugzillaUserRestClient extends AbstractAsyncRestClient implements BugzillaUserRestClient {

	private BugzillaUserParser userParser;
	private BugzillaIdParser idParser;

	/**
	 * Constructor.
	 */
	public AsyncBugzillaUserRestClient(URI baseUri, HttpClient client, LazyRetriever lazyRetriever) {
		super(baseUri, "User", client, lazyRetriever);
		userParser = new BugzillaUserParser(lazyRetriever);
		idParser = new BugzillaIdParser(lazyRetriever);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User login(String user, String password) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("login", user);
		params.put("password", password);
		params.put("remember", "False");
		try {
			Long id = postAndParse("login", params, idParser).get();
			return getUsers(id).get().iterator().next();
		} catch (InterruptedException e) {
			throw new RestClientException("Cannot login", e);
		} catch (ExecutionException e) {
			throw new RestClientException("Cannot login", e);
		}
			
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void logout() {
		postAndParse("logout", (JSONObject)null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<User>> getUsers(long... ids) {
		List<Long> l = new ArrayList<Long>();
		for (int i=0; i<ids.length; i++) l.add(ids[i]);
		return getUsers(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<User>> getUsers(Collection<Long> ids) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return postAndParse("get", params, userParser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<User>> getUsersByName(String... names) {
		List<String> l = new ArrayList<String>();
		for (int i=0; i<names.length; i++) l.add(names[i]);
		return getUsersByName(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<User>> getUsersByName(Collection<String> names) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("names", names);
		return postAndParse("get", params, userParser);
	}

}
