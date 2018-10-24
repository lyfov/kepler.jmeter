package kepler.jmeter.scriptJar.master;

import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONArray;
import org.json.JSONObject;

public class QueryH5CollectAllByBusinessNo extends MasterRequest {

	public QueryH5CollectAllByBusinessNo(JMeterVariables vars) {
		super(vars);
	}

	@Override
	public boolean isSampleCorrect(String responseData, String expectedCode) {
		if (expectedCode.equals("0")) {
			// JSON格式校验
			if (assertJSONObject(responseData)) {
				JSONObject response = new JSONObject(responseData);

				// business_no
				assertValue(response, "business_no", getString(ScriptVariables.business_no));
				
				// remote_id
				assertValue(response, "remote_id", getString(ScriptVariables.remote_id));
				
				// h5_ys
				if (assertJSONObject(response, "h5_ys")) {
					JSONObject h5Ys = response.getJSONObject("h5_ys");
 
					// h5_ys_list
					if(assertJSONArray(h5Ys, "h5_ys_list")) {
						JSONArray h5YsList = h5Ys.getJSONArray("h5_ys_list");
						
						// h5_ys_list
						if(!setSampleAssert(h5YsList.length() >= 1)) {
							errorMessage.append("h5_ys_list.length < 1");
						}
					}
				}
			}
		} else {
			// JSON格式校验
			if (assertJSONObject(responseData)) {
				JSONObject response = new JSONObject(responseData);

				// code
				assertValue(response, "code", Integer.parseInt(expectedCode));
			}
		}
		return isSampleCorrect;
	}

	@Override
	protected String method() {
		return "post";
	}

	@Override
	protected String path() {
		return "/query_h5_collect_all_by_businessNo";
	}

	@Override
	protected String requestData(String expectedCode) {
		return getQueryServerDataByCode(getString(ScriptVariables.tenant_id), getString(ScriptVariables.business_no), expectedCode);
	}

}
