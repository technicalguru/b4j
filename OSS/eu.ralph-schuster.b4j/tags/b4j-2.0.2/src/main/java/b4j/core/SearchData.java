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

import java.util.List;

import org.slf4j.Logger;

import rs.baselib.configuration.IConfigurable;


/**
 * Defines search parameters that a Session object can use
 * to query bugs.
 * @author Ralph Schuster
 */
public interface SearchData extends IConfigurable {
	
	/**
	 * Returns the number of parameter names.
	 * @return number of parameter names.
	 */
	public int getParameterCount();
	
	/**
	 * Returns an iterator of all parameter names.
	 * @return iterator on parameter names
	 */
	public Iterable<String> getParameterNames();
	
	/**
	 * Returns true if parameter name is set.
	 * @param name parameter name
	 * @return true or false
	 */
	public boolean hasParameter(String name);
	
	/**
	 * Returns an iterator of all values for specified parameter key.
	 * This methods must return null if no such parameter exists.
	 * @param parameter - name of parameter.
	 * @return iterator on values of parameter
	 */
	public Iterable<String> get(String parameter);

	/**
	 * Adds a search parameter with specified values.
	 * Existing values of this parameter must not be overridden.
	 * @param parameter - name of parameter
	 * @param values - array of additional values for parameter
	 */
	public void add(String parameter, String values[]);
	
	/**
	 * Adds a search parameter with specified values.
	 * Existing values of this parameter must not be overridden.
	 * @param parameter - name of parameter
	 * @param values - list of additional values for parameter
	 */
	public void add(String parameter, List<String> values);

	/**
	 * Adds a value for a specific search parameter.
	 * @param parameter - name of parameter
	 * @param value - value to add for parameter
	 */
	public void add(String parameter, String value);

	/**
	 * Debug information into log.
	 * This method supports debugging functionality.
	 * @param log - log to dump into.
	 */
	public void dump(Logger log);
}
