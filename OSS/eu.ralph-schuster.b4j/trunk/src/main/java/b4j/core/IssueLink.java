/*
 * This file is part of Bugzilla for Java.
 *
 *  Bugzilla for Java is free software: you can redistribute it 
 *  and/or modify it under the terms of version 3 of the GNU 
 *  Lesser General Public  License as published by the Free Software 
 *  Foundation.
 *  
 *  Bugzilla for Java is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with Bugzilla for Java.  If not, see 
 *  <http://www.gnu.org/licenses/lgpl-3.0.html>.
 */
package b4j.core;

/**
 * Interface describing a link between two issues.
 * @author Ralph Schuster
 *
 */
public interface IssueLink extends BugzillaObject {
	
	public static enum Type {
		/** No specification made */
		UNSPECIFIED,
		/** This marks a duplicate issue */
		DUPLICATE,
		/** The source depends on the target issue */
		DEPENDS_ON
	}
	
//	public static final String LINK_NAME_UNSPECIFIED = "Unspecified";
//	public static final String LINK_NAME_CHILD = "Child";
//	public static final String LINK_NAME_DUPLICATE = "Duplicate";
//	public static final String LINK_NAME_DEPENDENCY_OF = "Dependency of";
//	public static final String LINK_NAME_DEPENDS_ON = "Depends on";

	/**
	 * Returns the link type.
	 * @return the linkType
	 */
	public Type getLinkType();

	/**
	 * Sets the lnk type.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param linkType the linkType to set
	 */
	public void setLinkType(Type linkType);

	/**
	 * Returns the link type name.
	 * @return the linkTypeName
	 */
	public String getLinkTypeName();

	/**
	 * Sets the link type name.
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
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
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
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
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
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
	 * <p><b>Notice to implementors:</b> This method is for internal use only.</p>
	 * @param issueId the issueId to set
	 */
	public void setIssueId(String issueId);

}
