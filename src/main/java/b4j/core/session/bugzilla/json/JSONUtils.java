/**
 * 
 */
package b4j.core.session.bugzilla.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Some util menthods for JSON.
 * @author ralph
 *
 */
public class JSONUtils {

	/**
	 * Deep-convert the collection into a JSON array.
	 * @param collection collection to convert
	 * @return JSON array
	 * @throws JSONException when the collection cannot be converted
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray convert(Collection<?> collection) throws JSONException {
		JSONArray rc = new JSONArray();
		for (Object o : collection) {
			if (o instanceof Collection) {
				rc.put(convert((Collection<?>)o));
			} else if (o instanceof Map) {
				rc.put(convert((Map<String,Object>)o));
			} else if (o.getClass().isArray()) {
				rc.put(convert((Object[])o));
			} else {
				rc.put(o);
			}
		}
		return rc;
	}
	
	/**
	 * Deep-convert the map into a JSON object.
	 * @param map map to convert
	 * @return JSON object
	 * @throws JSONException when the map cannot be converted
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject convert(Map<String,Object> map) throws JSONException {
		JSONObject rc = new JSONObject();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			if (value instanceof Collection) {
				value = convert((Collection<?>)value);
			} else if (value instanceof Map) {
				value = convert((Map<String,Object>)value);
			} else if (value.getClass().isArray()) {
				value = convert((Object[])value);
			}
			rc.put(entry.getKey(), value);
		}
		return rc;
	}
	
	/**
	 * Deep-convert the array into a JSON array.
	 * @param arr array to convert
	 * @return JSON array
	 * @throws JSONException when the array cannot be converted
	 */
	public static JSONArray convert(Object arr[]) throws JSONException {
		Collection<Object> collection = new ArrayList<Object>();
		for (Object o : arr) collection.add(o);
		return convert(collection);
	}
	
}
