/**
 * 
 */
package b4j.core;

/**
 * Information about components.
 * @author ralph
 * @since 2.0
 *
 */
public interface Component {

	/**
	 * Returns the project for this component.
	 */
	public Project getProject();
	
	/**
	 * Returns the name.
	 */
	public String getId();

	/**
	 * Returns the name.
	 */
	public String getName();

	/**
	 * Returns the name.
	 */
	public String getDescription();

}
