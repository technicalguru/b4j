package b4j.core;

/**
 * Describes a link to another issue
 * @author Ralph Schuster
 *
 */
public class DefaultLink implements IssueLink {

	private int linkType;
	private String linkTypeName;
	private boolean inward;
	private String linkTypeDescription;
	private String issueId;

	/**
	 * Constructor.
	 */
	public DefaultLink() {

	}

	/**
	 * Constructor.
	 * @param linkType type of link
	 * @param linkTypeName name of link type
	 * @param inward incoming?
	 * @param linkTypeDescription description of link
	 * @param issueId issue linked
	 */
	public DefaultLink(int linkType, String linkTypeName, boolean inward, String linkTypeDescription, String issueId) {
		super();
		this.linkType = linkType;
		this.linkTypeName = linkTypeName;
		this.inward = inward;
		this.linkTypeDescription = linkTypeDescription;
		this.issueId = issueId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getLinkType() {
		return linkType;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLinkTypeName() {
		return linkTypeName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLinkTypeName(String linkTypeName) {
		this.linkTypeName = linkTypeName;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isInward() {
		return inward;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setInward(boolean inward) {
		this.inward = inward;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getLinkTypeDescription() {
		return linkTypeDescription;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setLinkTypeDescription(String linkTypeDescription) {
		this.linkTypeDescription = linkTypeDescription;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getIssueId() {
		return issueId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
}