/**
 * 
 */
package b4j.core.session.bugzilla.json;

import java.util.Iterator;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import b4j.util.LazyRetriever;

/**
 * Basic implementation of JSON methods.
 * @author ralph
 * @since 2.0
 *
 */
public abstract class AbstractJsonParser {

	private LazyRetriever lazyRetriever;
	
	/**
	 * Constructor.
	 * @param lazyRetriever the retriever to be used
	 */
	public AbstractJsonParser(LazyRetriever lazyRetriever) {
		this.lazyRetriever = lazyRetriever;
	}
	
	/**
	 * Returns the error if the JSON object contains one.
	 * @param json the JSON response
	 * @return the error string
	 * @throws JSONException
	 */
	public BugzillaJsonError getError(JSONObject json) throws JSONException {
		String s = json.getString("error");
		if ((s != null) && !"null".equals(s)) {
			JSONObject err = json.getJSONObject("error");
			return new BugzillaJsonError(err.getString("code"), err.getString("message"));
		}
		return null;
	}

	/**
	 * Chekc sthe response for errors. 
	 * @param json the JSON response
	 * @throws JSONException when the response has errors
	 */
	public void checkError(JSONObject json) throws JSONException {
		BugzillaJsonError error = getError(json);
		if (error != null){
			throw new JSONException("Error: "+error);
		}
	}
	
	/**
	 * Returns the result of the response.
	 * @param json JSON response
	 * @return result object
	 * @throws JSONException
	 */
	public JSONObject getResult(JSONObject json) throws JSONException {
		return json.getJSONObject("result");
	}
	
	/**
	 * Returns the result of the response.
	 * @param json JSON response
	 * @return result object
	 * @throws JSONException
	 */
	public JSONArray getResultAsArray(JSONObject json) throws JSONException {
		return json.getJSONArray("result");
	}
	
	/** Debugs the JSON object */
	public void debug(JSONObject json) throws JSONException {
		Iterator<?> i = json.keys();
		while (i.hasNext()) {
			String key = i.next().toString();
			System.out.println(key+"="+json.getString(key));
		}
	}

	/**
	 * Returns the lazyRetriever.
	 * @return the lazyRetriever
	 */
	protected LazyRetriever getLazyRetriever() {
		return lazyRetriever;
	}
	
	
}