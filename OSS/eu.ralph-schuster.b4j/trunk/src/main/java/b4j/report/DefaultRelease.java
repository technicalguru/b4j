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
package b4j.report;

import java.util.Date;

import rs.baselib.lang.HashCodeUtil;

/**
 * Default implementation of a release.
 * @author Ralph Schuster
 *
 */
public class DefaultRelease implements Release {

	private Date releaseTime = new Date();
	private String releaseName;
	
	/**
	 * Default constructor.
	 */
	public DefaultRelease() {
	}

	/**
	 * Constructor.
	 */
	public DefaultRelease(String releaseName, Date releaseTime) {
		setReleaseName(releaseName);
		setReleaseTime(releaseTime);
	}

	/**
	 * Returns the time of the release.
	 * @return the release time
	 */
	public Date getReleaseTime() {
		return releaseTime;
	}

	/**
	 * Sets the time of the release.
	 * @param releaseTime - the release time to set
	 */
	public void setReleaseTime(Date releaseTime) {
		this.releaseTime.setTime(releaseTime.getTime());
	}

	/**
	 * Returns the release name.
	 * @return the releaseName
	 */
	public String getReleaseName() {
		return releaseName;
	}

	/**
	 * Sets the release name.
	 * @param releaseName - the release name to set
	 */
	public void setReleaseName(String releaseName) {
		this.releaseName = releaseName;
	}

	/**
	 * Returns the name.
	 * @return name of the release
	 */
	@Override
	public String toString() {
		return getReleaseName();
	}

	/**
	 * Checks if given object is equal to this release.
	 * Equality is checked using the release names only.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Release) {
			return getReleaseName().equals(((Release)obj).getReleaseName());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return HashCodeUtil.hash(HashCodeUtil.SEED, getReleaseName());
	}
	
	
}
