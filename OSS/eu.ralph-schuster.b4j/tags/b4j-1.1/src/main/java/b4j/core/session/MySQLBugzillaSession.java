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
package b4j.core.session;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;

import b4j.core.DefaultSearchData;
import b4j.core.Issue;
import b4j.core.SearchData;
import b4j.core.SearchResultCountCallback;

/**
 * Implements Bugzilla session via direct MySQL Connection.
 * @author Ralph Schuster
 *
 */
public class MySQLBugzillaSession extends AbstractAuthorizedSession {

	private static final String MINIMUM_BUGZILLA_VERSION = "2.20";
	private static final String MAXIMUM_BUGZILLA_VERSION = "3.0.999";
	private static final String DEFAULT_MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	
	private String mysqlDriver;
	private String mysqlHost;
	private int mysqlPort;
	private String mysqlDatabase;
	private Connection mysqlConnection;
	
	/**
	 * Default constructor.
	 */
	public MySQLBugzillaSession() {
		throw new IllegalStateException("Class is draft only");
	}

	/**
	 * Configures the session.
	 * The method is called to initialize the session object from a configuration.
	 * The configuration object must ensure to contain the elements "login",
	 * "password", "mysql-driver", "mysql-host", "mysql-port" and "mysql-database" to contain 
	 * MySQL account information and MySQL database instance information.
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		super.configure(config);
		mysqlDriver = config.getString("mysql-driver");
		mysqlHost = config.getString("mysql-host");
		if ((mysqlDriver == null) || (mysqlDriver.trim().length() == 0)) mysqlDriver = DEFAULT_MYSQL_DRIVER;
		try {
			mysqlPort = 3306;
			String s = config.getString("mysql-port");
			if ((s != null) && (s.trim().length() > 0)) mysqlPort = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new ConfigurationException("Malformed Bugzilla URL: ", e);
		}
		mysqlDatabase = config.getString("mysql-database");
	}
	
	/**
	 * Opens the session with configured Bugzilla instance.
	 * @return true when session could be established successfully, false otherwise
	 */
	@Override
	public boolean open() {
		String driverUrl = "jdbc:mysql://"+mysqlHost+":"+mysqlPort+"/"+mysqlDatabase;
		try {
			Class.forName(mysqlDriver);
			mysqlConnection = DriverManager.getConnection(driverUrl, getLogin(), getPassword());
		} catch (ClassNotFoundException e) {
			getLog().error("Cannot find driver: ", e);
		} catch (SQLException e) {
			getLog().error("Cannot establish connection: ", e);
		}
		return false;
	}

	/**
	 * Closes the previously established Bugzilla session.
	 */
	@Override
	public void close() {
		if (!isLoggedIn()) return;
		try {
			mysqlConnection.close();
		} catch (SQLException e) {
			
		}
		mysqlConnection = null;
	}

	/**
	 * Returns the bugzilla version this session object is connected to.
	 * Can return null if version is unknown.
	 * @return version number or null if unknown or not connected
	 */
	@Override
	public String getBugzillaVersion() {
		return null;
	}

	/**
	 * Returns the minimum Bugzilla version this session class supports.
	 * @see #MINIMUM_BUGZILLA_VERSION
	 * @return minimum version of supported Bugzilla software.
	 */
	@Override
	public String getMinimumBugzillaVersion() {
		return MINIMUM_BUGZILLA_VERSION;
	}

	/**
	 * Returns the maximum Bugzilla version this session class supports.
	 * @see #MAXIMUM_BUGZILLA_VERSION
	 * @return maximum version of supported Bugzilla software.
	 */
	@Override
	public String getMaximumBugzillaVersion() {
		return MAXIMUM_BUGZILLA_VERSION;
	}

	/**
	 * Returns true when session is connected to Bugzilla.
	 * @return true if session was successfully established, false otherwise
	 */
	@Override
	public boolean isLoggedIn() {
		try {
			return (mysqlConnection != null) && mysqlConnection.isValid(10);
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * @see b4j.core.Session#getIssue(java.lang.String)
	 */
	@Override
	public Issue getIssue(String id) {
		SearchData search = new DefaultSearchData();
		search.add("issueId", id);
		Iterator<Issue> i = searchBugs(search, null);
		if ((i != null) && i.hasNext()) return i.next();
		return null;
	}

	/**
	 * Performs a search for Bugzilla bugs.
	 * This method returns an iterator over all bug records found. The returned
	 * iterator will query its data when the first call to its {@link Iterator#next()}
	 * method is made. A separate thread will then be spawned to retrieve the
	 * required details.
	 * @param searchData - all search parameters
	 * @param callback - a callback object that will retrieve the number of bugs 
	 * found for this search
	 * @return iterator on all bugs fulfilling the criteria expressed by search parameters.
	 */
	@Override
	public Iterator<Issue> searchBugs(SearchData searchData, SearchResultCountCallback callback) {
		
		return null;
	}

}

/*

query_format=advanced
short_desc_type=allwordssubstr
short_desc=
classification=
product=
long_desc_type=substring
long_desc=
bug_file_loc_type=allwordssubstr
bug_file_loc=
keywords_type=allwords
keywords=
bug_status=
emailassigned_to1=1
emailtype1=substring
email1=
emailassigned_to2=1
emailreporter2=1
emailqa_contact2=1
emailcc2=1
emailtype2=substring
email2=
bugidtype=include
bug_id=
chfieldfrom=
chfieldto=Now
chfieldvalue=
cmdtype=doit
order=Bug+Number
field0-0-0=noop
type0-0-0=noop
value0-0-0=

*/
