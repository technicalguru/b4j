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

package b4j.core.session.jira;

import java.util.TimeZone;

import org.joda.time.DateTime;

import rs.baselib.util.RsDate;

/**
 * Helper functions specific for Jira
 * @author ralph
 *
 */
public class JiraUtils {

	/**
	 * Convert the Jira time zoned time to RsDate.
	 * @param dt - the Jira datetime
	 * @return - the converted RsDate object
	 */
	public static RsDate convertDate(DateTime dt) {
		long millis = dt.getMillis();
		TimeZone tz = TimeZone.getTimeZone(dt.getZone().getID());
		RsDate rc = new RsDate(tz, millis);
		return rc;
	}
}
