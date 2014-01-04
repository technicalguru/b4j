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
 * @see <a href="http://www.bugzilla.org/docs/4.4/en/html/api/Bugzilla/WebService/Classification.html">Bugzilla::WebService::Classification</a>
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
	
	/**
	 * Retrieves information a classification.
	 *
	 * @param name name of classification
	 * @return information about a classification
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Classification getClassificationByName(String name);

	/**
	 * Retrieves information classifications.
	 *
	 * @param names names of classification
	 * @return information about a classification
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Classification>> getClassificationsByName(String... names);

	/**
	 * Retrieves information classifications.
	 *
	 * @param names names of classification
	 * @return information about a classification
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Classification>> getClassificationsByName(Collection<String> names);

}
