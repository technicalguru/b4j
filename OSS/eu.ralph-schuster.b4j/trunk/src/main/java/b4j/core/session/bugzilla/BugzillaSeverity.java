/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.Severity;

/**
 * Bugzilla implementation of a severity.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaSeverity implements Severity {

	private String severity;
	
	/**
	 * Constructor.
	 */
	public BugzillaSeverity(String severity) {
		this.severity = severity;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return severity;
	}

}
