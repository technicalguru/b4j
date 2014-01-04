/**
 * 
 */
package b4j.core.session.bugzilla;

import b4j.core.Component;
import b4j.core.Project;

/**
 * Bugzilla implementation of {@link Component}.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaComponent implements Component {

	private String id;
	private String name;
	private String description;
	private BugzillaProject project;
	
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Project getProject() {
		return project;
	}

	/**
	 * Sets the project.
	 * @param project the project to set
	 */
	public void setProject(BugzillaProject project) {
		this.project = project;
	}

	
}
