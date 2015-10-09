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
package b4j.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;


/**
 * Default implementation of SearchData. This class can
 * read it's parameters and values from a configuration object.
 * @author Ralph Schuster
 *
 */
public class DefaultSearchData implements SearchData {

	private Map<String,List<String>> parameters;

	/**
	 * Default constructor.
	 */
	public DefaultSearchData() {
		parameters = new HashMap<String,List<String>>();
	}

	/**
	 * Configures the search data from configuration.
	 * All elements of the configuration are taken as search parameters.
	 * For XML documents this means: &lt;parameter-name&gt;parameter-value&lt;/parameter-name&gt;
	 * Multiple values are allowed.
	 * @param config - configuration data
	 * @throws ConfigurationException - if configuration fails
	 */
	public void configure(Configuration config) throws ConfigurationException {
		// Others
		Iterator<?> keys = config.getKeys();
		while (keys.hasNext()) {
			String key = keys.next().toString();
			Iterator<?> values = config.getList(key).iterator();
			while (values.hasNext()) add(key.replaceAll("\\.", "/"), values.next().toString());
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(String parameter, String values[]) {
		for (int i=0; i<values.length; i++) add(parameter, values[i]);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(String parameter, List<String> values) {
		List<String> v = parameters.get(parameter);
		if (v == null) {
			v = new ArrayList<String>();
			parameters.put(parameter, v);
		}
		if (values != null) {
			Iterator<String> i = values.iterator();
			while (i.hasNext()) v.add(i.next());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(String parameter, String value) {
		List<String> values = parameters.get(parameter);
		if (values == null) {
			values = new ArrayList<String>();
			parameters.put(parameter, values);
		}
		values.add(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getParameterCount() {
		return parameters.size();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<String> getParameterNames() {
		return parameters.keySet();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<String> get(String parameter) {
		return parameters.get(parameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void dump(Logger log) {
		if (!log.isDebugEnabled()) return;
		Iterator<String> keys = parameters.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			for (String v : get(key)) {
				log.debug("search-"+key.toLowerCase()+"="+v);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasParameter(String name) {
		return parameters.containsKey(name);
	}

}
