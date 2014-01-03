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

import b4j.core.Classification;
import b4j.core.session.bugzilla.BugzillaClassificationRestClient;
import b4j.core.session.bugzilla.LazyRetriever;
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

	private BugzillaClassificationParser classificationParser;

	/**
	 * Constructor.
	 */
	public AsyncBugzillaClassificationRestClient(URI baseUri, HttpClient client, LazyRetriever lazyRetriever) {
		super(baseUri, "Classification", client, lazyRetriever);
		classificationParser = new BugzillaClassificationParser(lazyRetriever);
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
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return postAndParse("get", params, classificationParser);
	}

	
}
