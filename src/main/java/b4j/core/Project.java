/**
 * 
 */
package b4j.core;

import java.util.Collection;

/**
 * Information about a project or product.
 * @author ralph
 *
 */
public interface Project {

	/**
	 * Returns the name.
	 */
	public String getName();

	/**
	 * Returns the ID.
	 */
	public String getId();

	/**
	 * Returns the description.
	 */
	public String getDescription();

	/**
	 * Returns the versions.
	 */
	public Collection<Version> getVersions();
	
	/**
	 * Returns the components.
	 */
	public Collection<Component> getComponents();
	
	
}
