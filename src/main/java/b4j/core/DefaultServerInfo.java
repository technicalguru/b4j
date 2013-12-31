/**
 * 
 */
package b4j.core;

import java.net.URI;

/**
 * Default implementation of {@link ServerInfo}.
 * @author ralph
 * @since 2.0
 *
 */
public class DefaultServerInfo implements ServerInfo {

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
