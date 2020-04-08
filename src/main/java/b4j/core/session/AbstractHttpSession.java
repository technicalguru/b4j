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

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.lang.LangUtils;
import rs.baselib.util.CommonUtils;
import b4j.core.DefaultIssue;
import b4j.core.Issue;
import b4j.core.Session;
import b4j.core.UnsupportedVersionException;
import b4j.util.HttpSessionParams;

/**
 * This is the base class for all authorization-based sessions
 * (which usually is required for Bugzilla access, regardless what
 * way you use).
 * This abstract implementation takes care of login and password
 * retrieval out of configuration definitions. Configuration usually
 * tells what classes shall be used for callback. Each callback itself
 * has it's own configuration methodology.
 * @author Ralph Schuster
 *
 */
public abstract class AbstractHttpSession implements Session {

	private Logger log = null;
	private HttpSessionParams httpSessionParams;
	private Class<?> bugzillaBugClass;
	
	/**
	 * Constructor.
	 */
	public AbstractHttpSession() {
		log = LoggerFactory.getLogger(getClass());
		httpSessionParams = new HttpSessionParams();
	}

	/**
	 * Configures the session.
	 * The method is called to initialize the session object from a configuration.
	 * This implementation looks for an element &lt;AuthorizationCallback&gt;. It
	 * defines the implementation class that will deliver login name and password.
	 * If no class was given or class is "null" then the content of the tag
	 * must contain two elements &lt;login&gt; and &lt;password&gt;.
	 * <P>
	 * The default {@link Issue} implementation class to be used can also
	 * be configured using the &lt;Issue&gt; tag, e.g.
	 * </p>
	 * <p>
	 * &lt;Issue class="b4j.core.DefaultIssue"/&gt;
	 * </p>
	 * <p>
	 * If the tag is missing or class is empty then {@link DefaultIssue} is assumed.
	 * </p>
	 * @param config - configuration object
	 * @throws ConfigurationException - when configuration fails
	 */
	@Override
	public void configure(Configuration config) throws ConfigurationException {
		String className = null;
		try {
			httpSessionParams.configure(config);
			
			className = config.getString("Issue[@class]");
			if ((className == null) || (className.trim().length() == 0)) className = DefaultIssue.class.getName();
			Class<?> bugzillaBugClass = LangUtils.forName(className);
			setBugzillaBugClass(bugzillaBugClass);
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException("Cannot find class: "+className, e);
		}
	}

	/**
	 * Returns the bugzillaBugClass.
	 * @return the bugzillaBugClass
	 */
	public Class<?> getBugzillaBugClass() {
		if (bugzillaBugClass == null) return DefaultIssue.class;
		return bugzillaBugClass;
	}

	/**
	 * Sets the bugzillaBugClass.
	 * @param bugzillaBugClass the bugzillaBugClass to set
	 */
	public void setBugzillaBugClass(Class<?> bugzillaBugClass) {
		this.bugzillaBugClass = bugzillaBugClass;
	}

	/**
	 * Creates an instance of Issue.
	 * This method returns a fresh instance of Issue. The implementation class is
	 * defined by the configuration.
	 * @return new instance of Issue
	 */
	public Issue createIssue() {
		try {
			return (Issue)getBugzillaBugClass().getConstructor().newInstance();
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Cannot access constructor: "+getBugzillaBugClass().getName(), e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("Cannot find default constructor: "+getBugzillaBugClass().getName(), e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException("Cannot invocate constructor: "+getBugzillaBugClass().getName(), e);
		} catch (InstantiationException e) {
			throw new IllegalStateException("Cannot instantiate class: "+getBugzillaBugClass().getName(), e);
		}
	}
	
	
	/**
	 * Returns the logger.
	 * @return the log
	 */
	public Logger getLog() {
		return log;
	}

	/**
	 * Debugs information into log.
	 */
	@Override
	public void dump() {
		if (!getLog().isDebugEnabled()) return;
		getLog().debug("bugzilla-login="+getHttpSessionParams().getLogin());
		getLog().debug("bugzilla-password=<hidden>");
	}

	/**
	 * Checks the Bugzilla version connected to.
	 * The method will throw an {@link UnsupportedVersionException} if the version does not match.
	 */
	protected void checkBugzillaVersion() {
		if (!CommonUtils.isCompatibleVersion(getMinimumBugzillaVersion(), getMaximumBugzillaVersion(), getBugzillaVersion())) {
			String req = "required:";
			if (getMinimumBugzillaVersion() != null) req += " min. "+getMinimumBugzillaVersion(); 
			if (getMaximumBugzillaVersion() != null) req += " max. "+getMaximumBugzillaVersion(); 
			throw new UnsupportedVersionException("Incompatible version: "+getBugzillaVersion()+" ("+req+")");
		}
	}

	/**
	 * Throws a {@link IllegalStateException} when the session was not opened yet.
	 */
	protected void checkLoggedIn() {
		if (!isLoggedIn()) {
			throw new IllegalStateException("No session available");
		}
	}
	
	/**
	 * Returns the httpSessionParams.
	 * @return the httpSessionParams
	 */
	public HttpSessionParams getHttpSessionParams() {
		return httpSessionParams;
	}

	/**
	 * Sets the httpSessionParams.
	 * @param httpSessionParams the httpSessionParams to set
	 */
	public void setHttpSessionParams(HttpSessionParams httpSessionParams) {
		this.httpSessionParams = httpSessionParams;
	}
	
	
}
