package kepler.jmeter.scriptJar.master;

import java.util.Iterator;

import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONArray;
import org.json.JSONObject;

public class QueryDataByBusinessno extends MasterRequest {

	public QueryDataByBusinessno(JMeterVariables vars) {
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

				// result
				if (assertJSONObject(response, "result")) {
					JSONObject result = response.getJSONObject("result");
 
					// pay_bill_list
					if (assertJSONObject(result, "pay_bill_list")) {
						JSONObject payBillList = result.getJSONObject("pay_bill_list");

						// datalist
						if (assertJSONArray(payBillList, "datalist")) {
							JSONArray dataList = payBillList.getJSONArray("datalist");

							// datalist
							if(!setSampleAssert(dataList.length() >= 1)) {
								errorMessage.append("datalist.length < 1");
							}
						}

						// remove pay_bill_list
						result.remove("pay_bill_list");
					}

					// zhima_data
					if (assertJSONObject(result, "zhima_data")) {
						JSONObject zhimaData = result.getJSONObject("zhima_data");

						// zhima_score
						assertValueMatch(zhimaData, "zhima_center", QueryZhimaScore.scoreRegex);

						// remove zhima_data
						result.remove("zhima_data");
					}

					// other -- expectedResult
					JSONObject expectedResultJSON = new JSONObject(getString(ScriptVariables.line_result));
					if(!compareJSONObject(expectedResultJSON, result)) {
						Iterator<String> keys = expectedResultJSON.keys();
						while(keys.hasNext()) {
							errorMessage.append(keys.next()).append(";");
						}
						errorMessage.append("其中之一错误");
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
		return "/query_data_by_businessno";
	}

	@Override
	protected String requestData(String expectedCode) {
		return getQueryServerDataByCode(getString(ScriptVariables.tenant_id), getString(ScriptVariables.business_no), expectedCode);
	}
}
