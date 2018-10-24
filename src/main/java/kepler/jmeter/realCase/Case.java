package kepler.jmeter.realCase;

import org.json.JSONArray;
import org.json.JSONObject;

import kepler.jmeter.scriptJar.utils.JSONTool;

public class Case {
	final String line_case = "line_case";
	final String to_cancel = "to_cancel";
	final String line_result = "line_result";

	public Case() {
	}

	public void createCaseFile(String sourceFilePath, String targetFilePath) {
		
		
		// 源JSONArray
		JSONArray sourceJSONArray = JSONTool.getJsonArrayfromFile(sourceFilePath);
		// 遍历源数组
		for (int sourceIndex = 0; sourceIndex < sourceJSONArray.length(); sourceIndex++) {
			// 一次登录的case
			JSONObject sourceCase = sourceJSONArray.getJSONObject(sourceIndex);
			
			boolean toCancel = sourceCase.getBoolean(to_cancel);
			JSONObject publish = sourceCase.getJSONObject("publish");
			JSONObject poll = sourceCase.getJSONObject("poll");
		}
	}
}
