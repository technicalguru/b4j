/**
 * 
 */
package b4j.core;

import java.util.Date;

/**
 * Version information.
 * @author ralph
 *
 */
public interface Version {

	/**
	 * Returns the ID.
	 */
	public Long getId();
	
	/**
	 * Returns the name.
	 */
	public String getName();

	/**
	 * Returns the release date.
	 */
	public Date getReleaseDate();

}
