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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implements templating methods similar to Typo3.
 * Simple tag markers must be upper-case. The must appear in
 * templates embraced by three hash signs, e.g. ###MARKER###
 * @author Ralph Schuster
 *
 */
public class Templating {

	/**
	 * Replaces a simple marker by its value.
	 * Simple tag markers must be upper-case. The must appear in
	 * templates embraced by three hash signs, e.g. ###MARKER###
	 * @param template - text template
	 * @param marker - marker name, e.g. MARKER
	 * @param value - value to be replaced
	 * @return the template string with all marker's occurrences replaced
	 */
	public static String replace(String template, String marker, Object value) {
		if (value == null) value = "";
		return replaceAll(template, "###"+marker.toUpperCase()+"###", value.toString());
	}
	
	/**
	 * Replaces multiple simple tag markers.
	 * Simple tag markers must be upper-case. The must appear in
	 * templates embraced by three hash signs, e.g. ###MARKER###
	 * @param template - text template
	 * @param markers - markers map
	 * @return - replaced template text
	 */
	public static String replace(String template, Map<String,Object> markers) {
		if (template == null) return null;
		
		// Iterate while there are changes
		// Necessary due to unpredictable iterating order
		while (true) {
			String pre = template;
			String lastMarker = "";
			Iterator<String> i = markers.keySet().iterator();
			while (i.hasNext()) {
				String marker = (String)i.next();
				Object value  = markers.get(marker);
				if (value == null) value = "";
				try {
					template = replaceAll(template, "###"+marker.toUpperCase()+"###", value.toString());
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					System.out.println("\n\nmarker="+marker+" value="+value);
					System.out.println("\n\nlastMarker="+lastMarker);
				}
				lastMarker = marker+" value="+value;
			}
			if (pre.equals(template)) break;
		}
		return template;
	}
	
	/**
	 * Returns a sub template.
	 * Sub templates are enclosed by XML comment tags as known from TYPO3:
	 * &lt;!-- ###MARKER### begin&gt; my sub template text &lt;!-- ###MARKER### end&gt;
	 * Sub templates of same name are not allowed 
	 * @param template - parent template
	 * @param name - name of sub template, e.g. MARKER
	 * @return sub template, or null if not found
	 */
	public static String getSubTemplate(String template, String name) {
		String startTag = "<!--\\s*###"+name+"###\\s+begin\\s*-->\\s*\\n?";
		String endTag   = "<!--\\s*###"+name+"###\\s+end\\s*-->";
		String pattern = startTag+"(.*)"+endTag;
		Pattern p = Pattern.compile(".*"+pattern+".*", Pattern.DOTALL);
		Matcher m = p.matcher(template);
		if (m.matches()) return m.group(1);
		return null;
	}
	
	/**
	 * Returns a template from a file.
	 * @param filename - filename
	 * @return template's content
	 * @throws IOException when an error occurred
	 */
	public static String getTemplate(String filename) throws IOException {
		return getTemplate(new File(filename));
	}
	
	/**
	 * Returns a template from a file.
	 * @param f - file to load
	 * @return template's content
	 * @throws IOException when an error occurred
	 */
	public static String getTemplate(File f) throws IOException {
		return FileFinder.load(new FileInputStream(f));
	}
	
	/**
	 * Replaces all occurrences in a string.
	 * @param s - the string to change
	 * @param marker - the marker to be changed
	 * @param value - the value to be set in
	 * @return given string with marker replaced
	 */
	private static String replaceAll(String s, String marker, String value) {
		if (s == null) return s;
		StringBuffer buf = new StringBuffer();
		buf.append(s);
		int pos = 0;
		int markerLen = marker.length();
		while (pos >= 0) {
			pos = buf.indexOf(marker, pos);
			if (pos >= 0) {
				buf.replace(pos, pos+markerLen, value);
			}
		}
		return buf.toString();
	}
}
