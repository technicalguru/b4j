/*
 * This file is part of Bugzilla for Java.
 *
 *  Bugzilla for Java is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  Bugzilla for Java is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with Bugzilla for Java.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
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

	/** Transformer for versions */
	public static class Version implements Transformer {
		@Override
		public Object transform(Object input) {
			return new JiraVersion((com.atlassian.jira.rest.client.domain.Version)input);
		}
	}

}
