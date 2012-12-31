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

/**
 * Used as callback to report the number of found bugs in a search result.
 * Instantiate an implementation in case you want to be notified about the
 * number of bugs that match your search query.
 * @see Session#searchBugs(SearchData, SearchResultCountCallback)
 * @author Ralph Schuster
 *
 */
public interface SearchResultCountCallback {

	/**
	 * Sets the number of found search results.
	 * @param resultCount - the number of bugs found in search query.
	 */
	public void setResultCount(int resultCount);
}
