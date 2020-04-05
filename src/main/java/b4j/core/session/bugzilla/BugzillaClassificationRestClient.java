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
package b4j.core.session.bugzilla;

import java.util.Collection;

import b4j.core.Classification;

import com.atlassian.util.concurrent.Promise;

/**
 * Interface for classification rest clients.
 * @author ralph
 * @see <a href="http://www.bugzilla.org/docs/4.4/en/html/api/Bugzilla/WebService/Classification.html">Bugzilla::WebService::Classification</a>
 * @since 2.0
 *
 */
public interface BugzillaClassificationRestClient {

	/**
	 * Retrieves information a classification.
	 *
	 * @param id ID of classification
	 * @return information about a classification
	 * @since 2.0
	 */
	public Classification getClassification(long id);

	/**
	 * Retrieves information classifications.
	 *
	 * @param ids IDs of classification
	 * @return information about a classification
	 * @since 2.0
	 */
	public Promise<Iterable<Classification>> getClassifications(long... ids);

	/**
	 * Retrieves information classifications.
	 *
	 * @param ids IDs of classification
	 * @return information about a classification
	 * @since 2.0
	 */
	public Promise<Iterable<Classification>> getClassifications(Collection<Long> ids);
	
	/**
	 * Retrieves information a classification.
	 *
	 * @param name name of classification
	 * @return information about a classification
	 * @since 2.0
	 */
	public Classification getClassificationByName(String name);

	/**
	 * Retrieves information classifications.
	 *
	 * @param names names of classification
	 * @return information about a classification
	 * @since 2.0
	 */
	public Promise<Iterable<Classification>> getClassificationsByName(String... names);

	/**
	 * Retrieves information classifications.
	 *
	 * @param names names of classification
	 * @return information about a classification
	 * @since 2.0
	 */
	public Promise<Iterable<Classification>> getClassificationsByName(Collection<String> names);

}
