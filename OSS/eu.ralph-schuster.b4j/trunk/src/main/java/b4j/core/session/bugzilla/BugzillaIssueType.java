/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.IssueType;

/**
 * Bugzilla implementation of an {@link IssueType}.
 * @author ralph
 *
 */
public class BugzillaIssueType implements IssueType {

	private String issueType;
	
	/**
	 * Constructor.
	 */
	public BugzillaIssueType(String issueType) {
		this.issueType = issueType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return issueType;
	}

}
