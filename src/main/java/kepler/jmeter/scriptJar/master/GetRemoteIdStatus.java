package kepler.jmeter.scriptJar.master;

import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONObject;

public class GetRemoteIdStatus extends MasterRequest {

	public GetRemoteIdStatus(JMeterVariables vars) {
		super(vars);
	}

	@Override
	public boolean isSampleCorrect(String responseData, String expectedCode) {
		// JSON格式校验
		if (assertJSONObject(responseData)) {
			JSONObject response = new JSONObject(responseData);
		
			// code
			assertValue(response, "code", Integer.parseInt(expectedCode));
		}
		return isSampleCorrect;
	}

	@Override
	protected String method() {
		return "post";
	}

	@Override
	protected String path() {
		return "/get_remoteId_status";
	}

	@Override
	protected String requestData(String expectedCode) {
		return "remoteId=" + getString(ScriptVariables.remote_id);
	}
}
