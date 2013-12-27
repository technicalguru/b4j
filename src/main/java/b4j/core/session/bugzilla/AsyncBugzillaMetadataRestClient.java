/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.IssueType;
import b4j.core.Priority;
import b4j.core.Resolution;
import b4j.core.ServerInfo;
import b4j.core.Severity;
import b4j.core.Status;

import com.atlassian.httpclient.api.HttpClient;
import com.atlassian.jira.rest.client.internal.async.AbstractAsynchronousRestClient;
import com.atlassian.util.concurrent.Promise;

/**
 * The client responsible for getting metadata information.
 * @author ralph
 * @since 2.0
 *
 */
public class AsyncBugzillaMetadataRestClient extends AbstractAsynchronousRestClient implements BugzillaMetadataRestClient {

	/**
	 * Constructor.
	 * @param client
	 */
	public AsyncBugzillaMetadataRestClient(HttpClient client) {
		super(client);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<IssueType>> getIssueTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Status> getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Priority>> getPriorities() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Severity>> getSeverities() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Resolution>> getResolutions() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<ServerInfo> getServerInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
