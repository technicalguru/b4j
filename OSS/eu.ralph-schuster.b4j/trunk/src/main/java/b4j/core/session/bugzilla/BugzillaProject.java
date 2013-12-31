/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.Project;

/**
 * Bugzilla implementation of {@link Project}.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaProject implements Project {

	private String name;
	
	/**
	 * Constructor.
	 */
	public BugzillaProject(String name) {
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
