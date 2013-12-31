/**
 * 
 */
package b4j.core;

import java.net.URI;

/**
 * Metadata about a server.
 * @author ralph
 * @since 2.0
 *
 */
public interface ServerInfo {

	/**
	 * Returns the Base URI.
	 */
	public URI getBaseUri();


	/**
	 * Returns the version.
	 */
	public String getVersion();

}
