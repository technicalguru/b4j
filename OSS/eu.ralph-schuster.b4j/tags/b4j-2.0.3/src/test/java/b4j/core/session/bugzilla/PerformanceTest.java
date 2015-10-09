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

import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import b4j.core.Project;

import com.atlassian.util.concurrent.Promise;

/**
 * Tests a few APIs for performance.
 * @author ralph
 *
 */
public class PerformanceTest extends AbstractRestClientTest {

	private static Logger log = LoggerFactory.getLogger(PerformanceTest.class);
	
	private static BugzillaProductRestClient myClient;

	@BeforeClass
	public static void setup() throws Exception {
		AbstractRestClientTest.setup(); // Our own server has no classifications
		myClient = client.getProductClient();		
	}

	@Test
	public void testGetSelectableProducts() throws Exception {
		long startTime = System.currentTimeMillis();
		Promise<Iterable<Project>> promise = myClient.getSelectableProducts(); 
		long time = System.currentTimeMillis() - startTime;
		time += testGetProducts(promise);
		log.info("getSelectableProducts: "+time+" ms");
	}

	@Test
	public void testGetEnterableProducts() throws Exception {
		long startTime = System.currentTimeMillis();
		Promise<Iterable<Project>> promise = myClient.getEnterableProducts(); 
		long time = System.currentTimeMillis() - startTime;
		time += testGetProducts(promise);
		log.info("getEnterableProducts: "+time+" ms");
	}

	@Test
	public void testGetAcessibleProducts() throws Exception {
		long startTime = System.currentTimeMillis();
		Promise<Iterable<Project>> promise = myClient.getAccessibleProducts(); 
		long time = System.currentTimeMillis() - startTime;
		time += testGetProducts(promise);
		log.info("getAccessibleProducts: "+time+" ms");
	}

	@SuppressWarnings("unused")
	public long testGetProducts(Promise<Iterable<Project>> promise) throws Exception {
		assertNotNull("No promise", promise);
		
		int cnt = 0;
		long startTime = System.currentTimeMillis();
		for (Project p : promise.get()) {
			p.getName();
		}
		return System.currentTimeMillis() - startTime;
	}

}
