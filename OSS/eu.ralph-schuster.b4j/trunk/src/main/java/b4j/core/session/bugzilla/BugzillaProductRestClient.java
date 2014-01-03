/**
 * 
 */
package b4j.core.session.bugzilla;

import java.util.Collection;

import b4j.core.Project;

import com.atlassian.util.concurrent.Promise;

/**
 * Interface for product rest clients.
 * @author ralph
 * @see <a href="http://www.bugzilla.org/docs/4.4/en/html/api/Bugzilla/WebService/Product.html">Bugzilla::WebService::Product</a>
 * @since 2.0
 *
 */
public interface BugzillaProductRestClient {

	/**
	 * Retrieves information a product.
	 *
	 * @param id ID of product
	 * @return information about a product
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Project getProduct(long id);

	/**
	 * Retrieves information about products.
	 *
	 * @param ids IDs of products
	 * @return information about a products
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Project>> getProducts(long... ids);

	/**
	 * Retrieves information about products.
	 *
	 * @param ids IDs of products
	 * @return information about products
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Project>> getProducts(Collection<Long> ids);

	/**
	 * Retrieves information about selectable product.
	 *
	 * @return information about products
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Project>> getSelectableProducts();

	/**
	 * Retrieves information about enterable product.
	 *
	 * @return information about products
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Project>> getEnterableProducts();


	/**
	 * Retrieves information about accessible product.
	 *
	 * @return information about products
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 * @since 2.0
	 */
	public Promise<Iterable<Project>> getAccessibleProducts();
}
