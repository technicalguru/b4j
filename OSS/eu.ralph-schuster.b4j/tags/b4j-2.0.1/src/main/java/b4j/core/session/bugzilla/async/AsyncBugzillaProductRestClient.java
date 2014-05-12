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

import org.codehaus.jettison.json.JSONObject;

import b4j.core.Project;
import b4j.core.session.bugzilla.BugzillaProductRestClient;
import b4j.core.session.bugzilla.json.BugzillaIdListParser;
import b4j.core.session.bugzilla.json.BugzillaProductParser;

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
	public AsyncBugzillaProductRestClient(AsyncBugzillaRestClient mainClient) {
		super(mainClient, "Product");
		productParser = new BugzillaProductParser(getLazyRetriever());
		idParser = new BugzillaIdListParser(getLazyRetriever());
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
	public Project getProduct(String name) {
		try {
			for (Project rc : getProductsByName(name).get()) {
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
	public Promise<Iterable<Project>> getProductsByName(String... names) {
		List<String> l = new ArrayList<String>();
		for (int i=0; i<names.length; i++) l.add(names[i]);
		return getProductsByName(l);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Promise<Iterable<Project>> getProductsByName(Collection<String> names) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("names", names);
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
