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
package b4j.core;

import java.net.URI;

/**
 * Default implementation of {@link ServerInfo}.
 * @author ralph
 * @since 2.0
 *
 */
public class DefaultServerInfo extends AbstractBugzillaObject implements ServerInfo {

	private URI baseUri;
	private String version;
	
	/**
	 * Constructor.
	 */
	public DefaultServerInfo() {
		this(null, null);
	}

	/**
	 * Constructor.
	 * @param baseUri - URI of server
	 * @param version - version of server
	 */
	public DefaultServerInfo(URI baseUri, String version) {
		setBaseUri(baseUri);
		setVersion(version);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URI getBaseUri() {
		return baseUri;
	}

	/**
	 * Sets the baseUri.
	 * @param baseUri the baseUri to set
	 */
	public void setBaseUri(URI baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	
}
