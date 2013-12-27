/**
 * 
 */
package b4j.core.session.jira;

import com.atlassian.jira.rest.client.domain.BasicIssueType;

import b4j.core.IssueType;

/**
 * Jira implementation of an {@link IssueType}.
 * @author ralph
 *
 */
public class JiraIssueType implements IssueType {

	private BasicIssueType issueType;
	
	/**
	 * Constructor.
	 */
	public JiraIssueType(BasicIssueType issueType) {
		this.issueType = issueType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return issueType.getName();
	}

}
