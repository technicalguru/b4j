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
package b4j.core.session.bugzilla;

import org.apache.commons.collections.Transformer;

import b4j.core.DefaultClassification;

/**
 * The transformations required for Bugzilla.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaTransformer {

	/** Transformer for issue types */
	public static class IssueType implements Transformer {
		@Override
		public Object transform(Object input) {
			return new BugzillaIssueType((String)input);
		}
	}

	/** Transformer for priorities */
	public static class Priority implements Transformer {
		@Override
		public Object transform(Object input) {
			return new BugzillaPriority((String)input);
		}
	}

	/** Transformer for resolutions */
	public static class Resolution implements Transformer {
		@Override
		public Object transform(Object input) {
			return new BugzillaResolution((String)input);
		}
	}
	
	/** Transformer for status */
	public static class Status implements Transformer {
		@Override
		public Object transform(Object input) {
			return new BugzillaStatus((String)input);
		}
	}

	/** Transformer for severities */
	public static class Severity implements Transformer {
		@Override
		public Object transform(Object input) {
			return new BugzillaSeverity((String)input);
		}
	}

	/** Transformer for projects */
	public static class Project implements Transformer {
		@Override
		public Object transform(Object input) {
			return new BugzillaProject((String)input);
		}
	}

	/** Transformer for severities */
	public static class Component implements Transformer {
		@Override
		public Object transform(Object input) {
			return new BugzillaComponent((String)input);
		}
	}

	/** Transformer for severities */
	public static class Classification implements Transformer {
		@Override
		public Object transform(Object input) {
			return new DefaultClassification((String)input);
		}
	}

}
