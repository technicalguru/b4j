/**
 * 
 */
package b4j.core.session.bugzilla;


/**
 * The REST client for Bugzilla.
 * @author ralph
 * @since 2.0
 *
 */
public interface BugzillaClient {

	/**
	 * Returns the Metadata client.
	 * @return client for performing operations on meta data
	 */
	public BugzillaMetadataRestClient getMetadataClient();

	/**
	 * Returns the Classification client.
	 * @return client for performing operations on classifications
	 */
	public BugzillaClassificationRestClient getClassificationClient();

	/**
	 * Returns the Product client.
	 * @return client for performing operations on products
	 */
	public BugzillaProductRestClient getProductClient();

}
