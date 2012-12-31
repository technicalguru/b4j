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
package b4j.util;

/**
 * Provides some useful methods for all classes.
 * @author Ralph Schuster
 *
 */
public class BugzillaUtils {

	/**
	 * Returns true if version is in range of minVersion and maxVersion.
	 * Note that 1.2.0 is greater than 1.2
	 * @param minVersion - minimum required version (can be null)
	 * @param maxVersion - maximum required version (can be null)
	 * @param version - version to check
	 * @return true when version is within range
	 */
	public static boolean isCompatibleVersion(String minVersion, String maxVersion, String version) {
		if (version == null) return true;
		String vParts[] = version.split("\\.");
		String minParts[] = null;
		String maxParts[] = null;
		if (minVersion != null) minParts = minVersion.split("\\.");
		if (maxVersion != null) maxParts = maxVersion.split("\\.");
		
		// Check minimum version
		if ((minParts != null) && (compareVersion(minParts, vParts) > 0)) return false;
		// Check maximum version
		if ((maxParts != null) && (compareVersion(maxParts, vParts) < 0)) return false;
		
		return true;
	}
	
	/**
	 * Compares versions.
	 * @param v1 - version 1 divided into separate parts
	 * @param v2 - version 2 divided into separate parts
	 * @return -1 if v1 < v2, 1 if v1 > v2, 0 if v1 = v2
	 */
	public static int compareVersion(String v1[], String v2[]) {
		int maxCount = Math.max(v1.length, v2.length);
		for (int i=0; i<maxCount; i++) {
			try {
				long l1 = 0;
				if (i < v1.length) l1 = Long.parseLong(v1[i]);
				long l2 = 0;
				if (i <v2.length) l2 = Long.parseLong(v2[i]);
				if (l1 < l2) return -1;
				if (l1 > l2) return 1;
			} catch (NumberFormatException e) {
				int rc = v1[i].compareTo(v2[i]);
				if (rc != 0) return rc;
			}
		}
		
		// Usually equal here, but check remaining minor versions
		if (v1.length > v2.length) return 1;
		if (v1.length < v2.length) return -1;
		
		// Equal now
		return 0;
	}
	
	/**
	 * Makes a join of a string array.
	 * @param separator - the string to be used inbetween parts
	 * @param parts - the parts to join
	 * @return the joined string
	 */
	public static String join(String separator, String parts[]) {
		String s = "";
		for (int i=0; i<parts.length; i++) s += separator + parts[i];
		if (s.length() > 0) s = s.substring(separator.length());
		return s;
	}
}
