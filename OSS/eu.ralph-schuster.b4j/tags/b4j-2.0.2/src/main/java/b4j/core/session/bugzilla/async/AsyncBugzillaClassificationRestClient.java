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
package b4j.core.session.bugzilla.async;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import b4j.core.Classification;
import b4j.core.session.bugzilla.BugzillaClassificationRestClient;
import b4j.core.session.bugzilla.json.BugzillaClassificationParser;

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
	public AsyncBugzillaClassificationRestClient(AsyncBugzillaRestClient mainClient) {
		super(mainClient, "Classification");
		classificationParser = new BugzillaClassificationParser(getLazyRetriever());
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Classification getClassificationByName(String name) {
		try {
			for (Classification rc : getClassificationsByName(name).get()) {
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
	public Promise<Iterable<Classification>> getClassificationsByName(String... names) {
		List<String> l = new ArrayList<String>();
		for (int i=0; i<names.length; i++) l.add(names[i]);
		return getClassificationsByName(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Classification>> getClassificationsByName(Collection<String> names) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("names", names);
		return postAndParse("get", params, classificationParser);
	}

	
}
