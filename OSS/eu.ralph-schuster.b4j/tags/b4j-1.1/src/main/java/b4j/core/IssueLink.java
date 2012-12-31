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
	 * @return the linkType
	 */
	public int getLinkType();

	/**
	 * @param linkType the linkType to set
	 */
	public void setLinkType(int linkType);

	/**
	 * @return the linkTypeName
	 */
	public String getLinkTypeName();

	/**
	 * @param linkTypeName the linkTypeName to set
	 */
	public void setLinkTypeName(String linkTypeName);

	/**
	 * @return the inward
	 */
	public boolean isInward();

	/**
	 * @param inward the inward to set
	 */
	public void setInward(boolean inward);

	/**
	 * @return the linkTypeDescription
	 */
	public String getLinkTypeDescription();

	/**
	 * @param linkTypeDescription the linkTypeDescription to set
	 */
	public void setLinkTypeDescription(String linkTypeDescription);

	/**
	 * @return the issueId
	 */
	public String getIssueId();

	/**
	 * @param issueId the issueId to set
	 */
	public void setIssueId(String issueId);

}
