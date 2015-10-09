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

import b4j.util.TypedTransformer;

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
	public static class IssueType implements TypedTransformer<BasicIssueType,JiraIssueType> {
		@Override
		public JiraIssueType transform(BasicIssueType input, Object... args) {
			return new JiraIssueType((BasicIssueType)input);
		}
	}

	/** Transformer for priorities */
	public static class Priority implements TypedTransformer<BasicPriority,JiraPriority> {
		@Override
		public JiraPriority transform(BasicPriority input, Object... args) {
			return new JiraPriority(input);
		}
	}

	/** Transformer for resolutions */
	public static class Resolution implements TypedTransformer<BasicResolution,JiraResolution> {
		@Override
		public JiraResolution transform(BasicResolution input, Object... args) {
			return new JiraResolution(input);
		}
	}
	
	/** Transformer for status */
	public static class Status implements TypedTransformer<BasicStatus,JiraStatus> {
		@Override
		public JiraStatus transform(BasicStatus input, Object... args) {
			return new JiraStatus(input);
		}
	}

	/** Transformer for severities */
	public static class Severity implements TypedTransformer<BasicPriority,JiraSeverity> {
		@Override
		public JiraSeverity transform(BasicPriority input, Object... args) {
			return new JiraSeverity(input);
		}
	}

	/** Transformer for users */
	public static class User implements TypedTransformer<BasicUser,JiraUser> {
		@Override
		public JiraUser transform(BasicUser input, Object... args) {
			return new JiraUser(input);
		}
	}

	/** Transformer for projects */
	public static class Project implements TypedTransformer<BasicProject,JiraProject> {
		@Override
		public JiraProject transform(BasicProject input, Object... args) {
			return new JiraProject(input);
		}
	}

	/** Transformer for components */
	public static class Component implements TypedTransformer<BasicComponent,JiraComponent> {
		@Override
		public JiraComponent transform(BasicComponent input, Object... args) {
			return new JiraComponent((b4j.core.Project)args[0], input);
		}
	}

	/** Transformer for versions */
	public static class Version implements TypedTransformer<com.atlassian.jira.rest.client.domain.Version,JiraVersion> {
		@Override
		public JiraVersion transform(com.atlassian.jira.rest.client.domain.Version input, Object... args) {
			return new JiraVersion((b4j.core.Project)args[0], input);
		}
	}

}
