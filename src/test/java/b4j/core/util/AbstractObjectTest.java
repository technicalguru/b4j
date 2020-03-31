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

package b4j.core.util;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.LoggerFactory;

import b4j.core.BugzillaObject;
import rs.baselib.io.FileFinder;
import rs.baselib.lang.ReflectionUtils;
import rs.baselib.util.CommonUtils;

/**
 * Abstract implementation of correctness tests.
 * <p>The class will test whether the attributes/properties of a specific object are equal to expected
 * values.</p>
 * @author ralph
 *
 */
public abstract class AbstractObjectTest<T extends BugzillaObject> {

	private Class<T> testClass;

	/**
	 * Constructor.
	 */
	@SuppressWarnings("unchecked")
	public AbstractObjectTest() {
		testClass = (Class<T>)ReflectionUtils.getTypeArguments(AbstractObjectTest.class, getClass()).get(0);
	}

	/**
	 * Saves the properties in an XML file in <code>src/test/resources</code>.
	 * @param testObject object to be tested
	 */
	public void save(T testObject) {
		save("src/test/resources", testObject);
	}

	/**
	 * Saves the properties in an XML file.
	 * @param pathPrefix the path where to store
	 * @param testObject object to be stored
	 */
	public void save(String pathPrefix, T testObject) {
		Properties actual = createComparable(testObject);
		String filename = getFilename(testObject);
		if (filename != null) {
			FileOutputStream out = null;
			try {
				File f = new File(pathPrefix, filename);
				out = new FileOutputStream(f);
				actual.storeToXML(out, "Saved "+(new Date().toString())+": "+getId(testObject));
				out.flush();
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				if (out != null) try {
					out.close();
				} catch (IOException e) {
					throw new RuntimeException("Cannot close stream", e);
				}
			}
		}
	}

	/**
	 * Checks whether the test object is identical to what have been expected.
	 * @param testObject object to be tested
	 * @return <code>true</code> when object was tested
	 * @throws exception when the object is not equal to expected values
	 */
	public boolean test(T testObject) {
		Properties actual = createComparable(testObject);
		if (actual != null) {
			Properties expected = getExpected(testObject);
			if (expected != null) {
				test(testObject, expected, actual);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns all IDs that can be tested.
	 * @param filter a filter to be used (optional)
	 * @return the IDs
	 * @throws URISyntaxException
	 */
	public Iterable<String> getTestables(FilenameFilter filter) throws URISyntaxException {
		List<String> rc = new ArrayList<String>();
		URI uri = FileFinder.find(getCategoryPath(testClass)).toURI();
		File f = new File(uri);
		File files[] = f.listFiles(filter);
		if (files != null) {
			for (File child : files) {
				String name = child.getName();
				if (name.endsWith(".xml") && child.canRead()) rc.add(name.substring(0, name.length()-4));
			}
		}
		return rc;
	}

	/**
	 * Will convert the test object into a list of property-value pairs
	 * which must be identical when later comparing the object to
	 * expected values.
	 * @param object object to convert
	 * @return the property/value pairs
	 */
	public abstract Properties createComparable(T object);

	/**
	 * Returns the expected values.
	 * @param object object to load for
	 * @return the expected values
	 */
	protected Properties getExpected(T object) {
		URL url = find(object);
		Properties rc = null;
		if (url != null) {
			rc = new Properties();
			try {
				rc.loadFromXML(url.openStream());
			} catch (IOException e) {
				LoggerFactory.getLogger(getClass()).error(e.getLocalizedMessage(), e);
				fail("Cannot load from stream: "+url.toString());
			}
		}
		return rc;
	}


	/**
	 * Tests the object according to expected and actual values.
	 * @param testObject object that is being tested
	 * @param expected expected values
	 * @param actual actual values
	 */
	protected void test(T testObject, Properties expected, Properties actual) {
		for (Map.Entry<Object, Object> entry : expected.entrySet()) {
			String expectedValue = (String)entry.getValue();
			String actualValue = (String)actual.get(entry.getKey());
			if (expectedValue.startsWith("list:") && (actualValue != null)) {
				expectedValue = expectedValue.substring(5);
				actualValue = actualValue.substring(5);
				// List comparison
				String expList[] = expectedValue.split(",");
				String actList[] = actualValue.split(",");
				if (expList.length != actList.length) {
					fail(testClass.getSimpleName()+"["+getId(testObject)+"]["+entry.getKey()+"] does not match. expected="+expectedValue+", actual="+actualValue);
				} else {
					for (String e : expList) {
						boolean found = false;
						for (String a : actList) {
							if (e.equals(a)) {
								found = true;
								break;
							}
						}
						if (!found) {
							fail(testClass.getSimpleName()+"["+getId(testObject)+"]["+entry.getKey()+"] does not match. expected="+expectedValue+", actual="+actualValue);
						}
					}
				}
			} else {
				actualValue = cleanString(actualValue);
				expectedValue = cleanString(expectedValue);
				
				if (!CommonUtils.equals(actualValue, expectedValue)) {		
					fail(testClass.getSimpleName()+"["+getId(testObject)+"]["+entry.getKey()+"] does not match. expected="+expectedValue+", actual="+actualValue);
				}
			}
		}
	}

	protected String cleanString(String s) {
		if (s == null) return null;
		s = s.replaceAll("[\\n\\r]+", " ");
		return s;
	}
	
	/**
	 * Returns the filename to be used to store expected values.
	 * @param object object that need to be evaluated
	 * @return the filename
	 */
	protected String getFilename(T object) {
		StringBuilder rc = new StringBuilder();

		rc.append(getCategoryPath(testClass));
		rc.append("/");
		rc.append(getId(object));
		rc.append(".xml");
		return rc.toString();
	}

	/**
	 * Returns the category of this class to be used for path prefix.
	 * <p>The default implementation uses the clazz name</p>
	 * @param clazz the clazz object
	 * @return the category path
	 */
	protected String getCategoryPath(Class<T> clazz) {
		return clazz.getSimpleName().toLowerCase();
	}

	/**
	 * Returns the ID of the object that is unique within its class.
	 * @param object object to be retrieved ID for
	 * @return the ID
	 */
	protected abstract String getId(T object);

	/**
	 * Find the URL for expected properties of given object.
	 * @param object the object to be tested
	 * @return the URL that has the expected properties or <code>null</code> if no expected values are defined
	 */
	protected URL find(T object) {
		String filename = getFilename(object);
		if (filename != null) {
			return FileFinder.find(getClass(), filename);
		}
		return null;
	}

	/**
	 * Joins the collection by using the property of the collection items.
	 * @param coll collectin of objects
	 * @param property name of property of object to be joined
	 * @return the string
	 */
	protected static String join(Collection<?> coll, String property) {
		if ((coll == null) || (coll.size() == 0)) return "list:null";
		StringBuilder rc = new StringBuilder();
		rc.append("list:");
		for (Object o : coll) {
			if (rc.length() > 5) rc.append(',');
			try {
				Object value = property != null ? PropertyUtils.getProperty(o, property) : o.toString();
				if (value != null) rc.append(value.toString());
				else rc.append("null");
			} catch (Exception e) {
				throw new IllegalArgumentException("No such property: " +property, e);
			}
		}
		return rc.toString();
	}

	/**
	 * Returns a string representation of given property of object.
	 * @param o the object to be represented
	 * @param property the property to be used
	 * @return the string representation 
	 */
	protected static String toString(Object o, String property) {
		if (o != null) try {
			Object value = PropertyUtils.getProperty(o, property);
			if (value != null) return value.toString();
		} catch (Exception e) {
			throw new IllegalArgumentException("No such property:" +property, e);
		}
		return "null";
	}

	/**
	 * Returns a string representation of object.
	 * @param o the object to be represented
	 * @return the string representation 
	 */
	protected static String toString(Object o) {
		if (o == null) return "null";
		return o.toString();
	}

	/**
	 * Dumps the properties on console.
	 * @param props the properties
	 */
	public static void dump(Properties props) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			props.storeToXML(out, "Expected values");
			System.out.println(out.toString(StandardCharsets.UTF_8.name()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
