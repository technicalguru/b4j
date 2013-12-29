/**
 * 
 */
package b4j.core.session.jira;

import org.apache.commons.collections.Transformer;

import com.atlassian.jira.rest.client.domain.BasicComponent;
import com.atlassian.jira.rest.client.domain.BasicIssueType;
import com.atlassian.jira.rest.client.domain.BasicPriority;
import com.atlassian.jira.rest.client.domain.BasicProject;
import com.atlassian.jira.rest.client.domain.BasicResolution;
import com.atlassian.jira.rest.client.domain.BasicStatus;
import com.atlassian.jira.rest.client.domain.BasicUser;

/**
 * The transformations required for Jira.
 * @author ralph
 *
 */
public class JiraTransformer {

	/** Transformer for issue types */
	public static class IssueType implements Transformer {
		@Override
		public Object transform(Object input) {
			return new JiraIssueType((BasicIssueType)input);
		}
	}

	/** Transformer for priorities */
	public static class Priority implements Transformer {
		@Override
		public Object transform(Object input) {
			return new JiraPriority((BasicPriority)input);
		}
	}

	/** Transformer for resolutions */
	public static class Resolution implements Transformer {
		@Override
		public Object transform(Object input) {
			return new JiraResolution((BasicResolution)input);
		}
	}
	
	/** Transformer for status */
	public static class Status implements Transformer {
		@Override
		public Object transform(Object input) {
			return new JiraStatus((BasicStatus)input);
		}
	}

	/** Transformer for severities */
	public static class Severity implements Transformer {
		@Override
		public Object transform(Object input) {
			return new JiraSeverity((BasicPriority)input);
		}
	}

	/** Transformer for users */
	public static class User implements Transformer {
		@Override
		public Object transform(Object input) {
			return new JiraUser((BasicUser)input);
		}
	}

	/** Transformer for projects */
	public static class Project implements Transformer {
		@Override
		public Object transform(Object input) {
			return new JiraProject((BasicProject)input);
		}
	}

	/** Transformer for components */
	public static class Component implements Transformer {
		@Override
		public Object transform(Object input) {
			return new JiraComponent((BasicComponent)input);
		}
	}

}
