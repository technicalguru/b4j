package b4j.core;

import java.net.URI;
import java.util.Date;

/**
 * An attachment definition.
 * This object represents an attachment for a bug.
 * @author Ralph Schuster
 *
 */
public class DefaultAttachment implements Attachment {

	/**
	 * 
	 */
	private final Issue issue;
	private String id;
	private Date date;
	private String description;
	private String filename;
	private String type;
	private URI uri;

	/**
	 * Default constructor.
	 */
	public DefaultAttachment(Issue issue) {
		this.issue = issue;
		date = new Date(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Issue getIssue() {
		return issue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate() {
		return date;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDate(Date date) {
		this.date.setTime(date.getTime());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFilename() {
		return filename;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getType() {
		return type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URI getUri() {
		return uri;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUri(URI uri) {
		this.uri = uri;
	}

	/**
	 * Returns the best attribute for using in {@link #hashCode()} and {@link #equals(Object)} methods.
	 * @return {@link #id} or {@link #filename}
	 */
	private Object getHashAttribute() {
		if (getId() != null) return getHashAttribute();
		return getFilename();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getHashAttribute() == null) ? 0 : getHashAttribute().hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		DefaultAttachment other = (DefaultAttachment) obj;
		if (getHashAttribute() == null) {
			if (other.getHashAttribute() != null) return false;
		} else if (!getHashAttribute().equals(other.getHashAttribute())) return false;
		return true;
	}


}