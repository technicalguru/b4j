/**
 * 
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

	/**
	 * Constructor.
	 */
	public MetaData(Transformer transformer) {
		this.transformer = transformer;
	}

	/**
	 * Returns the transformed object for t.
	 * @param t the object to transform
	 * @return transformed object
	 */
	public V get(T t) {
		V rc = map.get(t);
		if (rc == null) {
			rc = transform(t);
			map.put(t, rc);
		}
		return rc;
	}

	/**
	 * Returns the transformed objects for the list of t.
	 * @param list the objects to transform
	 * @return transformed objects
	 */
	public Collection<V> get(Iterable<T> list) {
		List<V> rc = new ArrayList<V>();
		for (T t : list) {
			V v = get(t);
			if (v != null) rc.add(v);
		}
		return rc;
	}
	
	/**
	 * Transforms the given object to the target type.
	 * @param t object to be transformed
	 * @return new object
	 */
	@SuppressWarnings("unchecked")
	protected V transform(T t) {
		return (V)transformer.transform(t);
	}
}
