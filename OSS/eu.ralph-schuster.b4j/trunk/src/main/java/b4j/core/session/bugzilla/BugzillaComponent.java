/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.Component;

/**
 * Bugzilla implementation of {@link Component}.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaComponent implements Component {

	private String name;
	
	/**
	 * Constructor.
	 */
	public BugzillaComponent(String name) {
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
