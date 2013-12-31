/**
 * 
 */
package b4j.core.session.bugzilla;

import java.util.Collection;

import b4j.core.Classification;

import com.atlassian.util.concurrent.Promise;

/**
 * Interface for classification rest clients.
 * @author ralph
 * @since 2.0
 *
 */
public interface BugzillaClassificationRestClient {

	/**
	 * Retrieves information a classification.
	 *
	 * @param id ID of classification
	 * @return information about a classification
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Classification getClassification(long id);

	/**
	 * Retrieves information classifications.
	 *
	 * @param ids IDs of classification
	 * @return information about a classification
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Classification>> getClassifications(long... ids);

	/**
	 * Retrieves information classifications.
	 *
	 * @param ids IDs of classification
	 * @return information about a classification
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Classification>> getClassifications(Collection<Long> ids);

}
