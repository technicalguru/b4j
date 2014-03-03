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
 * Filter Implementation.
 * @author ralph
 *
 */
public class BasicFilter implements Filter {

	private URI self;
	private String id;
	private String name;
	private String description;
	private BasicUser owner;
	private String jql;
	private URI viewUrl;
	private URI searchUrl;
	private boolean favourite;

	/**
	 * Constructor.
	 */
	public BasicFilter() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URI getSelf() {
		return self;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BasicUser getOwner() {
		return owner;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJql() {
		return jql;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URI getViewUrl() {
		return viewUrl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URI getSearchUrl() {
		return searchUrl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFavourite() {
		return favourite;
	}

	/**
	 * Sets the self.
	 * @param self the self to set
	 */
	public void setSelf(URI self) {
		this.self = self;
	}

	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the description.
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the owner.
	 * @param owner the owner to set
	 */
	public void setOwner(BasicUser owner) {
		this.owner = owner;
	}

	/**
	 * Sets the jql.
	 * @param jql the jql to set
	 */
	public void setJql(String jql) {
		this.jql = jql;
	}

	/**
	 * Sets the viewUrl.
	 * @param viewUrl the viewUrl to set
	 */
	public void setViewUrl(URI viewUrl) {
		this.viewUrl = viewUrl;
	}

	/**
	 * Sets the searchUrl.
	 * @param searchUrl the searchUrl to set
	 */
	public void setSearchUrl(URI searchUrl) {
		this.searchUrl = searchUrl;
	}

	/**
	 * Sets the favourite.
	 * @param favourite the favourite to set
	 */
	public void setFavourite(boolean favourite) {
		this.favourite = favourite;
	}

	
}
