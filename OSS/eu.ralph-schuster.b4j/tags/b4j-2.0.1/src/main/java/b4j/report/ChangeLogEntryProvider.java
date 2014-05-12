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
 * Provides additional entries for various releases.  
 * @author Ralph Schuster
 *
 */
public interface ChangeLogEntryProvider extends IConfigurable {

	/**
	 * Returns all additional entries that must be included for the release.
	 * @param release - the release where data needs to be collected.
	 * @return iterator on all additional ChangeLog entries for this release.
	 */
	public Iterator<String> getChangeLogEntries(Release release);
}
