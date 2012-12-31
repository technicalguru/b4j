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
	 * Adds a search parameter with specified values.
	 * Existing values of this parameter will not be overridden.
	 * @param parameter - name of parameter
	 * @param values - array of additional values for parameter
	 */
	@Override
	public void add(String parameter, String values[]) {
		for (int i=0; i<values.length; i++) add(parameter, values[i]);
	}
	
	/**
	 * Adds a search parameter with specified values.
	 * Existing values of this parameter will not be overridden.
	 * @param parameter - name of parameter
	 * @param values - list of additional values for parameter
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
	 * Adds a value for a specific search parameter.
	 * @param parameter - name of parameter
	 * @param value - value to add for parameter
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
	 * Returns the number of parameter names.
	 * @return number of parameter names.
	 */
	@Override
	public int getParameterCount() {
		return parameters.size();
	}
	
	/**
	 * Returns an iterator of all parameter names.
	 * @return iterator on parameter names
	 */
	@Override
	public Iterator<String> getParameterNames() {
		return parameters.keySet().iterator();
	}
	
	/**
	 * Returns an iterator of all values for specified parameter key.
	 * This methods returns null if no such parameter exists.
	 * @param parameter - name of parameter.
	 * @return iterator on values of parameter
	 */
	@Override
	public Iterator<String> get(String parameter) {
		return parameters.get(parameter).iterator();
	}

	/**
	 * Debug information into log.
	 * This method supports debugging functionality.
	 * @param log - log to dump into.
	 */
	@Override
	public void dump(Logger log) {
		if (!log.isDebugEnabled()) return;
		Iterator<String> keys = parameters.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			Iterator<String> i = get(key);
			while (i.hasNext()) log.debug("search-"+key.toLowerCase()+"="+i.next());
			
		}
	}

	/**
	 * Returns true when given parameter name exists.
	 * @see b4j.core.SearchData#hasParameter(String)
	 * @param name parameter name
	 * @return true or false
	 */
	@Override
	public boolean hasParameter(String name) {
		return parameters.containsKey(name);
	}

}
