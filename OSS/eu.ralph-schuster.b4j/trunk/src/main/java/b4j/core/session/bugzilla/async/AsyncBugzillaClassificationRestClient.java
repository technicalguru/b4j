/**
 * 
 */
package b4j.core.session.bugzilla.async;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import b4j.core.Classification;
import b4j.core.session.bugzilla.BugzillaClassificationRestClient;
import b4j.core.session.bugzilla.json.BugzillaClassificationParser;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.RestClientException;
import com.atlassian.util.concurrent.Promise;

/**
 * The client responsible for getting classification information.
 * @author ralph
 * @since 2.0
 *
 */
public class AsyncBugzillaClassificationRestClient extends AbstractAsyncRestClient implements BugzillaClassificationRestClient {

	private static String CGI_PARAMS[] = {
		"method", "Classification.get"
	};
	
	private BugzillaClassificationParser classificationParser = new BugzillaClassificationParser();

	/**
	 * Constructor.
	 */
	public AsyncBugzillaClassificationRestClient(URI baseUri, HttpClient client) {
		super(baseUri, client);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Classification getClassification(long id) {
		try {
			for (Classification rc : getClassifications(id).get()) {
				return rc;
			}
		} catch (Exception e) {
			throw new RestClientException(e);
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Classification>> getClassifications(long... ids) {
		List<Long> l = new ArrayList<Long>();
		for (int i=0; i<ids.length; i++) l.add(ids[i]);
		return getClassifications(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Classification>> getClassifications(Collection<Long> ids) {
		URI serverInfoUri = build(CGI_PARAMS).queryParam("params", joinParameterLists(createParameterList("ids", ids.toArray()))).build();
		return getAndParse(serverInfoUri, classificationParser);
	}

	
}
