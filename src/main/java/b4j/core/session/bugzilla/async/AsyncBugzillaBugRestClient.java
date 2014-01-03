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

import b4j.core.Attachment;
import b4j.core.Comment;
import b4j.core.Issue;
import b4j.core.session.bugzilla.BugzillaBugRestClient;
import b4j.core.session.bugzilla.LazyRetriever;
import b4j.core.session.bugzilla.json.BugzillaBugParser;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.util.concurrent.Promise;

/**
 * The client responsible for getting metadata information.
 * @author ralph
 * @since 2.0
 *
 */
public class AsyncBugzillaBugRestClient extends AbstractAsyncRestClient implements BugzillaBugRestClient {

	private BugzillaBugParser bugParser;

	/**
	 * Constructor.
	 * @param client
	 */
	public AsyncBugzillaBugRestClient(URI baseUri, HttpClient client, LazyRetriever lazyRetriever) {
		super(baseUri, "Bug", client, lazyRetriever);
		bugParser = new BugzillaBugParser(lazyRetriever);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Issue>> getBugs(long... ids) {
		List<Long> l = new ArrayList<Long>();
		for (int i=0; i<ids.length; i++) l.add(ids[i]);
		return getBugs(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Issue>> getBugs(Collection<Long> ids) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
		return postAndParse("get", params, bugParser);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Issue>> findBugs(Map<String, Object> criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Attachment>> getAttachments(long... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Attachment>> getAttachments(Collection<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Comment>> getComments(long... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Comment>> getComments(Collection<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
