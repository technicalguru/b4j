/**
 * 
 */
package b4j.core.session.bugzilla;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import b4j.core.Component;
import b4j.core.Project;
import b4j.core.Version;

/**
 * Bugzilla implementation of {@link Project}.
 * @author ralph
 * @since 2.0
 *
 */
public class BugzillaProject implements Project {

	private String id;
	private String name;
	private String description;
	private List<Version> versions;
	private List<Component> components;
	
	/**
	 * Constructor.
	 */
	public BugzillaProject(String name) {
		versions = new ArrayList<Version>();
		components = new ArrayList<Component>();
		setName(name);
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
	public Collection<Version> getVersions() {
		return Collections.unmodifiableList(versions);
	}

	/**
	 * Adds versions
	 * @param versions versions to add
	 */
	public void addVersions(Version... versions) {
		for (Version v : versions) {
			this.versions.add(v);
		}
	}
	
	/**
	 * Removes versions
	 * @param versions versions to remove
	 */
	public void removeVersions(Version... versions) {
		for (Version v : versions) {
			this.versions.remove(v);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<Component> getComponents() {
		return Collections.unmodifiableList(components);
	}

	/**
	 * Adds components.
	 * @param components components to add
	 */
	public void addComponents(Component... components) {
		for (Component c : components) {
			this.components.add(c);
		}
	}

	/**
	 * Removes components.
	 * @param components components to remove
	 */
	public void removeComponents(Component... components) {
		for (Component c : components) {
			this.components.remove(c);
		}
	}
	
}
