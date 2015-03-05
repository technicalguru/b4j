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

import java.util.Iterator;

import rs.baselib.configuration.IConfigurable;

/**
 * Provides information about software releases.
 * The interface is used within the {@link ChangeLogReport} to allow
 * flexible configuration of a Change Log.
 * @author Ralph Schuster
 *
 */
public interface ReleaseProvider extends IConfigurable {

	/**
	 * Returns all available releases.
	 * This method returns the releases that will appear in a Change Log.
	 * @return iterator on all releases
	 */
	public Iterator<Release> getReleases();
}
