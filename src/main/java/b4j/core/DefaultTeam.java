/**
 * 
 */
package b4j.core;


/**
 * Default implementation of {@link Team}.
 * @author ralph
 * @since 2.0
 *
 */
public class DefaultTeam implements Team {

	private String name;
	
	/**
	 * Constructor.
	 */
	public DefaultTeam(String name) {
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}

}
