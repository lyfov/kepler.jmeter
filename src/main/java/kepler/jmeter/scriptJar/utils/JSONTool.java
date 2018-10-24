package kepler.jmeter.scriptJar.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONTool {

	/** 判断两个JONSObject是否相同
	 * @param json1	作为基准的JSONObject
	 * @param json2	要比较的JSONObject
	 * @return	是否相同
	 */
	public static boolean compareJson(JSONObject json1, JSONObject json2) {
		if (!(json1.length() == json2.length()))
			return false;
		boolean same = true;
		for (String key : json1.keySet()) {
			// 判断json2中是否存在key，如果same已经为false，不改变false，否则重新赋值
			same = same && json2.has(key);
			if (same) {
				// 判断json1的key对应value是否为null，如果为null，则判断json2是否为null。
				if (json1.isNull(key))
					same = json2.isNull(key);
				else
					same = compareJson(json1.get(key), json2.get(key));
			}
		}
		return same;
	}

	public static boolean compareJson(Object json1, Object json2) {
		boolean same;
		try {
			if (json1 == null)
				same = json2 == null;
			else if (json1 instanceof JSONObject)
				same = compareJson((JSONObject) json1, (JSONObject) json2);
			else if (json1 instanceof JSONArray)
				same = compareJson((JSONArray) json1, (JSONArray) json2);
			else if (json1 instanceof Boolean)
				same = (boolean) json1 == (boolean) json2;
			else if (json1 instanceof String)
				same = json1.equals(json2);
			else if (json1 instanceof Double)
				same = (double) json1 == (double) json2;
			else if (json1 instanceof Integer)
				same = (int) json1 == (int) json2;
			else if (json1 instanceof Long)
				same = (long) json1 == (long) json2;
			else
				same = false;
		} catch (Exception e) {
			same = false;
		}
		return same;
	}

	public static boolean compareJson(JSONArray json1, JSONArray json2) {
		boolean same = true;
		if (json1 != null && json2 != null && json1.length() == json2.length()) {
			for (int i = 0; i < json1.length(); i++) {
				if (same) {
					if (json1.isNull(i))
						same = json2.isNull(i);
					else
						same = compareJson(json1.get(i), json2.get(i));
				}
			}
		} else
			same = false;
		return same;
	}

	public static JSONArray getJsonArrayfromFile(String path) {
		return new JSONArray(StringTool.getStringFromFile(path));
	}

	public static JSONObject getJsonObjectfromFile(String path) {
		return new JSONObject(StringTool.getStringFromFile(path));
	}

	/**
	 * 把toAdd中的数据加入到result中去。toAdd中必须只包含JSONObject
	 * 
	 * @param result    result
	 * @param toAdd toAdd
	 * @return  JSONArray
	 */
	public static JSONArray addJSONObject(JSONArray result, JSONArray toAdd) {
		for (int i = 0; i < toAdd.length(); i++) {
			result.put(toAdd.getJSONObject(i));
		}
		return result;
	}

	/**
	 * 把toAdd中的数据加入到result中去。toAdd中必须只包含JSONArray
	 * 
	 * @param result    result
	 * @param toAdd toAdd
	 * @return  JSONArray
	 */
	public static JSONArray addJSONArray(JSONArray result, JSONArray toAdd) {
		for (int i = 0; i < toAdd.length(); i++) {
			result.put(toAdd.getJSONArray(i));
		}
		return result;
	}

	/**
	 * 判断一个字符串是否能构成JSONObject
	 *
	 * @param jsonString 要转变为JSONObject的字符串
	 * @return	是否能构成JSONObject
	 */
	public static boolean isJSONValid(String jsonString) {
		try {
			new JSONObject(jsonString);
			return true;
		} catch (JSONException e) {
			return false;
		}
	}
}
