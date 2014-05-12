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
package b4j.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b4j.core.SearchData;

/**
 * Collects information about URL parameters.
 * The object can be used to produce the URL-encoded-scheme form
 * of parameters. It supports multiple values per parameter.
 * @author Ralph Schuster
 *
 */
public class UrlParameters {
	
	private static Logger log = LoggerFactory.getLogger(UrlParameters.class);
	private Map<String, List<String>> parameters;
	
	/**
	 * Default constructor.
	 */
	public UrlParameters() {
		parameters = new HashMap<String, List<String>>();
	}

	/**
	 * Sets a specific parameter.
	 * Actually just adds it.
	 * @param key  - name of parameter
	 * @param value - value to add
	 */
	public void setParameter(String key, String value) {
		List<String> values = parameters.get(key);
		if (values == null) {
			values = new ArrayList<String>();
			parameters.put(key, values);
		}
		values.add(value);
	}

	/**
	 * Returns a list of all values of this parameter (or null)
	 * @param key - name of parameter
	 * @return list of values for this parameter
	 */
	public List<String> getParameter(String key) {
		return parameters.get(key);
	}
	
	/**
	 * Returns iterator of all parameter names.
	 * @return iterator on all parameter names
	 */
	public Iterator<String> getParameters() {
		return parameters.keySet().iterator();
	}
	
	/**
	 * Adds all values of a parameter.
	 * @param key - name of parameter
	 * @param values - values to add
	 */
	public void addAll(String key, List<String> values) {
		List<String> values2 = parameters.get(key);
		if (values2 == null) {
			values2 = new ArrayList<String>();
			parameters.put(key, values2);
		}
		if (values != null) values2.addAll(values);
	}
	
	/**
	 * Adds all parameters.
	 * @param p - list of parameters
	 */
	public void addAll(Map<String, List<String>> p) {
		Iterator<String> keys = p.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			addAll(key, p.get(key));
		}
	}
	
	/**
	 * Removes a value from a parameter.
	 * @param key - name of parameter
	 * @param value - value to remove
	 */
	public void removeParameter(String key, String value) {
		List<String> values = parameters.get(key);
		if (values == null) return;
		values.remove(value);
		if (values.size() == 0) parameters.remove(key);
	}
	
	/**
	 * Removes a parameter.
	 * @param key - parameter to remove
	 */
	public void removeParameter(String key) {
		List<String> values = parameters.get(key);
		if (values == null) return;
		parameters.remove(key);
	}
	
	/**
	 * Returns the URL encoded string of this parameters.
	 * @return the URL encoded string of the parameters
	 */
	public String getUrlEncodedString() {
		return getUrlEncodedString(parameters);
	}
	
	/**
	 * Adds default parameters.
	 * Only thos parameters will be applied that are not set yet.
	 * @param defaults - default parameters to apply.
	 */
	public void addDefaultParameters(UrlParameters defaults) {
		Iterator<String> keys = defaults.getParameters();
		while (keys.hasNext()) {
			String key = keys.next();
			if (!parameters.containsKey(key)) {
				addAll(key, defaults.getParameter(key));
			}
		}
	}
	
	/**
	 * Creates URL parameters out of the Bugzilla search data.
	 * This method returns an {@link UrlParameters} object created from
	 * the search parameters and their respective values.
	 * @param searchData - the search data to transform
	 * @return URL parameters object
	 */
	public static UrlParameters createUrlParameters(SearchData searchData) {
		UrlParameters rc = new UrlParameters();
		for (String key : searchData.getParameterNames()) {
			for (String value : searchData.get(key)) {
				rc.setParameter(key, value);
			}
		}
		return rc;
	}

	/**
	 * Returns the URL encoded string of the parameters.
	 * @param parameters - parameter list
	 * @return URL encoded string
	 */
	public static String getUrlEncodedString(Map<String, List<String>> parameters) {
		StringBuffer rc = new StringBuffer();
		try {
			for (Map.Entry<String, List<String>> entry : parameters.entrySet()) {
				String key = entry.getKey();
				for (String value : entry.getValue()) {
					if (rc.length() != 0) rc.append('&');
					rc.append(URLEncoder.encode(key, "UTF-8"));
					rc.append("=");
					rc.append(URLEncoder.encode(value, "UTF-8"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			log.error("Encoding Error:", e);
		}
		if (rc.length() > 0) return rc.toString();
		return null;
	}
}
