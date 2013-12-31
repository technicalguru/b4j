/**
 * 
 */
package b4j.core;

/**
 * Default implementation of a {@link Classification}.
 * @author ralph
 *
 */
public class DefaultClassification implements Classification {

	private String id;
	private String name;
	private String description;
	
	/**
	 * Constructor.
	 */
	public DefaultClassification() {
	}

	/**
	 * Constructor.
	 */
	public DefaultClassification(String id) {
		this(id, null, null);
	}

	/**
	 * Constructor.
	 */
	public DefaultClassification(String id, String name, String description) {
		setId(id);
		setName(name);
		setDescription(description);
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
	public String getName() {
		return name;
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

	
}
