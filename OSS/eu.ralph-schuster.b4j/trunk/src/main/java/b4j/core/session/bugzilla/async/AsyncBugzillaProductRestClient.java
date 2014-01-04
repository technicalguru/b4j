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

import org.codehaus.jettison.json.JSONObject;

import b4j.core.Project;
import b4j.core.session.bugzilla.BugzillaProductRestClient;
import b4j.core.session.bugzilla.json.BugzillaIdListParser;
import b4j.core.session.bugzilla.json.BugzillaProductParser;
import b4j.util.LazyRetriever;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.RestClientException;
import com.atlassian.util.concurrent.Promise;

/**
 * The client responsible for getting product information.
 * @author ralph
 *
 */
public class AsyncBugzillaProductRestClient extends AbstractAsyncRestClient implements BugzillaProductRestClient {

	private BugzillaProductParser productParser;
	private BugzillaIdListParser idParser;

	/**
	 * Constructor.
	 */
	public AsyncBugzillaProductRestClient(URI baseUri, HttpClient client, LazyRetriever lazyRetriever) {
		super(baseUri, "Product", client, lazyRetriever);
		productParser = new BugzillaProductParser(lazyRetriever);
		idParser = new BugzillaIdListParser(lazyRetriever);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProduct(long id) {
		try {
			for (Project rc : getProducts(id).get()) {
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
	public Promise<Iterable<Project>> getProducts(long... ids) {
		List<Long> l = new ArrayList<Long>();
		for (int i=0; i<ids.length; i++) l.add(ids[i]);
		return getProducts(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Project>> getProducts(Collection<Long> ids) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return postAndParse("get", params, productParser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Project>> getSelectableProducts() {
		return getProducts("get_selectable_products");
	}

	protected Promise<Iterable<Project>> getProducts(String method) {
		try {
			List<Long> l = new ArrayList<Long>();
			Iterable<Long> i = postAndParse(method, (JSONObject)null, idParser).get();
			for (Long obj : i) {
				l.add(obj);
			}
			return getProducts(l);
		} catch (Exception e) {
			throw new RestClientException("Cannot retrieve IDs", e);
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Project>> getEnterableProducts() {
		return getProducts("get_enterable_products");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Project>> getAccessibleProducts() {
		return getProducts("get_accessible_products");
	}

	
}
