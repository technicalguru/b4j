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
