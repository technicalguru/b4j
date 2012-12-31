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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Offers utilities to find files in classpaths and directories.
 * @author Ralph Schuster
 *
 */
public class FileFinder {

	private static Log log = LogFactory.getLog(FileFinder.class);
	
	/**
	 * Tries to find the file specified from filesystem or classpath.
	 * @param name - name of file, can be fully qualified
	 * @return stream to the file
	 */
	public static InputStream find(String name) {
		InputStream rc = null;
		
		// try to find as simple file in file system
		try {
			File f = new File(name);
			if (f.exists() && f.isFile() && f.canRead()) {
				rc = new FileInputStream(f);
			}
		} catch (Exception e) {
			if (log.isDebugEnabled()) 
				log.debug("No such local file: "+name, e);
		}
		
		// get it from classpath
		if (rc == null) {
			try {
				ClassLoader loader = FileFinder.class.getClassLoader();
				rc = loader.getResourceAsStream(name);
			} catch (Exception e) {
				if (log.isDebugEnabled()) 
					log.debug("No such classpath file: "+name, e);
			}
		}
		return rc;
	}
	
	/**
	 * Finds and loads a file.
	 * @param name - name of file
	 * @return contents of file
	 */
	public static String findAndLoad(String name) {
		InputStream in = find(name);
		return load(in);
	}
		
	/**
	 * Loads a file.
	 * @param in - input stream
	 * @return contents of stream
	 */
	public static String load(InputStream in) {
		String rc = "";
		if (in != null) try {
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			while (r.ready()) {
				rc += r.readLine()+"\n";
			}
			r.close();
		} catch (Exception e) {
			log.error("Cannot load file", e);
		}
		return rc;
	}
}
