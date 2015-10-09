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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.Transformer;

/**
 * A small repository of some meta data.
 * <p>Idea is to lazily collect data of type T that needs to be transformed into type V so that
 * subsequent calls always return the same object.</p>
 * @author ralph
 *
 */
public class MetaData<T, V> {

	private Map<T, V> map = new HashMap<T, V>();
	private Transformer transformer;
	private TypedTransformer<T,V> typedTransformer;

	/**
	 * Constructor.
	 */
	public MetaData(Transformer transformer) {
		this.transformer = transformer;
	}

	/**
	 * Constructor.
	 */
	public MetaData(TypedTransformer<T,V> typedTransformer) {
		this.typedTransformer = typedTransformer;
	}

	/**
	 * Returns the transformed object for t.
	 * @param t the object to transform
	 * @param args arguments for transformation
	 * @return transformed object
	 */
	public V get(T t, Object... args) {
		V rc = map.get(t);
		if (rc == null) {
			rc = transform(t, args);
			map.put(t, rc);
		}
		return rc;
	}

	/**
	 * Returns the transformed objects for the list of t.
	 * @param list the objects to transform
	 * @param args arguments for transformation
	 * @return transformed objects
	 */
	public Collection<V> get(Iterable<T> list, Object...args) {
		List<V> rc = new ArrayList<V>();
		for (T t : list) {
			V v = get(t, args);
			if (v != null) rc.add(v);
		}
		return rc;
	}
	
	/**
	 * Transforms the given object to the target type.
	 * @param t object to be transformed
	 * @param args arguments for transformation
	 * @return new object
	 */
	@SuppressWarnings("unchecked")
	protected V transform(T t, Object...args) {
		if (transformer != null) return (V)transformer.transform(t);
		return typedTransformer.transform(t, args);
	}
}
