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
package b4j.core.session.jira;

import java.net.URI;

import com.atlassian.jira.rest.client.domain.BasicUser;

/**
 * Represents a JIRA Filter definition.
 * @author ralph
 *
 */
public interface Filter {

	/**
	 * Returns the self.
	 * @return the self
	 */
	public URI getSelf();

	/**
	 * Returns the id.
	 * @return the id
	 */
	public String getId();

	/**
	 * Returns the name.
	 * @return the name
	 */
	public String getName();

	/**
	 * Returns the description.
	 * @return the description
	 */
	public String getDescription();
	
	/**
	 * Returns the owner.
	 * @return the owner
	 */
	public BasicUser getOwner();

	/**
	 * Returns the jql.
	 * @return the jql
	 */
	public String getJql();

	/**
	 * Returns the viewUrl.
	 * @return the viewUrl
	 */
	public URI getViewUrl();

	/**
	 * Returns the searchUrl.
	 * @return the searchUrl
	 */
	public URI getSearchUrl();

	/**
	 * Returns the favourite.
	 * @return the favourite
	 */
	public boolean isFavourite();


}
