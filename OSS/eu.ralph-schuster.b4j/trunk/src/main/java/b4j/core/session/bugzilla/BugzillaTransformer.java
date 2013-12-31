/**
 * 
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
