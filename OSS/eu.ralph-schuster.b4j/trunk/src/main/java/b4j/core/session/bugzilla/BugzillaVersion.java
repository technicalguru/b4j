/**
 * 
 */
package b4j.core.session.bugzilla;

import java.util.Date;

import b4j.core.Version;

/**
 * Bugzilla implementation of a {@link Version}.
 * @author ralph
 *
 */
public class BugzillaVersion implements Version {

	private Long id;
	private String name;
	private Date releaseDate;
	
	/**
	 * Constructor.
	 */
	public BugzillaVersion() {
		this(null, null);
	}

	/**
	 * Constructor.
	 */
	public BugzillaVersion(Long id, String name) {
		setId(id);
		setName(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getId() {
		return id;
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
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * Sets the id.
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Sets the name.
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the releaseDate.
	 * @param releaseDate the releaseDate to set
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	
}
