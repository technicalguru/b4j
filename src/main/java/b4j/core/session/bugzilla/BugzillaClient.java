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
	 * @return client for performing operations on meta data
	 */
	BugzillaMetadataRestClient getMetadataClient();

}
