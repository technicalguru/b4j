package b4j.core;

import java.net.URI;

import com.sun.jersey.core.util.Base64;

import rs.baselib.util.RsDate;

/**
 * An attachment definition.
 * This object represents an attachment for a bug.
 * @author Ralph Schuster
 *
 */
public class DefaultAttachment extends AbstractBugzillaObject implements Attachment {

	/**
	 * 
	 */
	private String issueId;
	private String id;
	private RsDate date;
	private String description;
	private String filename;
	private String type;
	private URI uri;
	private int length;
	private byte content[];
	
	/**
	 * Default constructor.
	 */
	public DefaultAttachment(String issueId) {
		this.issueId = issueId;
		date = new RsDate(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getIssueId() {
		return issueId;
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
	public RsDate getDate() {
		return date;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDate(RsDate date) {
		this.date = new RsDate(date);
		
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
		if (getId() != null) return getId();
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

	/**
	 * Sets the content of the attachment.
	 * @param length the length
	 * @param bytes the bytes
	 */
	public void setContent(int length, String bytes) {
		this.length = length;
		this.content = Base64.decode(bytes);
	}

	/**
	 * Returns the content length.
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns the content.
	 * @return the content
	 */
	public byte[] getContent() {
		return content;
	}
	
	
}