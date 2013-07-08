/**
 * 
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
