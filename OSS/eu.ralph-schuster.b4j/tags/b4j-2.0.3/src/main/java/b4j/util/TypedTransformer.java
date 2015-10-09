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

package b4j.util;


/**
 * Defines a functor interface implemented by classes that transform one
 * object into another.
 * <p>
 * A <code>TypedTransformer</code> converts the input object to the output object using the specified arguments.
 * The input object should be left unchanged.
 * Transformers are typically used for type conversions, or extracting data
 * from an object.
 * </p>
 * 
 * @since 2.0
 * @author ralph
 * @param <F> from class
 * @param <T> to class
 */
public interface TypedTransformer<F,T> {

	/**
	 * Constructor.
	 */
	public T transform(F object, Object... args);

}
