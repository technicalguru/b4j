/**
 * 
 */
package b4j.core;

/**
 * @author Ralph Schuster
 *
 */
public interface IssueLink {
	
	public static final int LINK_UNSPECIFIED = 0;
	public static final int LINK_CHILD = 1;
	public static final int LINK_DUPLICATE = 1;
	public static final int LINK_DEPENDENCY_OF = 3;
	public static final int LINK_DEPENDS_ON = 4;
	
	public static final String LINK_NAME_UNSPECIFIED = "Unspecified";
	public static final String LINK_NAME_CHILD = "Child";
	public static final String LINK_NAME_DUPLICATE = "Duplicate";
	public static final String LINK_NAME_DEPENDENCY_OF = "Dependency of";
	public static final String LINK_NAME_DEPENDS_ON = "Depends on";

	/**
	 * Returns the link type.
	 * @return the linkType
	 */
	public int getLinkType();

	/**
	 * Sets the lnk type.
	 * @param linkType the linkType to set
	 */
	public void setLinkType(int linkType);

	/**
	 * Returns the link type name.
	 * @return the linkTypeName
	 */
	public String getLinkTypeName();

	/**
	 * Sets the link type name.
	 * @param linkTypeName the linkTypeName to set
	 */
	public void setLinkTypeName(String linkTypeName);

	/**
	 * Is this link incoming?
	 * @return the inward
	 */
	public boolean isInward();

	/**
	 * Sets whether the link is incoming.
	 * @param inward the inward to set
	 */
	public void setInward(boolean inward);

	/**
	 * Returns the description.
	 * @return the linkTypeDescription
	 */
	public String getLinkTypeDescription();

	/**
	 * Sets the description.
	 * @param linkTypeDescription the linkTypeDescription to set
	 */
	public void setLinkTypeDescription(String linkTypeDescription);

	/**
	 * Returns the linked issue ID.
	 * @return the issueId
	 */
	public String getIssueId();

	/**
	 * Sets the linked issue ID.
	 * @param issueId the issueId to set
	 */
	public void setIssueId(String issueId);

}
