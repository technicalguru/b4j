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

/**
 * Provides information about a specific release.
 * This interface is used in Change Log generation. It gives basic information
 * about releases that are relevant for a Change Log.
 * @see ChangeLogReport
 * @author Ralph Schuster
 *
 */
public interface Release {

	/**
	 * Returns the release name.
	 * @return name of the release
	 */
	public String getReleaseName();
	/**
	 * Returns the time of the release.
	 * @return time of release
	 */
	public Date getReleaseTime();
}
