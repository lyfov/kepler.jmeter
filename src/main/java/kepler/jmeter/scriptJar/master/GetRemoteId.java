package kepler.jmeter.scriptJar.master;

import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONObject;

public class GetRemoteId extends MasterRequest {
	/**
	 * @param vars JMETER的vars对象
	 */
	public GetRemoteId(JMeterVariables vars) {
		super(vars);
	}

	@Override
	protected String method() {
		return "post";
	}

	@Override
	protected String path() {
		return "/get_remoteId";
	}

	@Override
	protected String requestData(String expectedCode) {
		return getQueryServerDataByCode(getString(ScriptVariables.tenant_id), expectedCode);
	}

	@Override
	public boolean isSampleCorrect(String responseData, String expectedCode) {
		// JSON格式校验
		assertJSONObject(responseData);

		// 响应为JSONObject格式，继续验证字段
		if (isSampleCorrect) {
			JSONObject response = new JSONObject(responseData);

			// code字段校验
			assertValue(response, "code", Integer.parseInt(expectedCode));

			// result字段校验
			assertJSONObject(response, "result");

			// remoteId字段校验
			if (isSampleCorrect) {
				JSONObject result = response.getJSONObject("result");

				// 如果预期的响应代码不为"0",则应为此次请求缺少参数或参数错误，remote_id应为""
				if (!expectedCode.equals("0")) {
					assertValue(result, ScriptVariables.remote_id, "");
					setIsThreadContinue(true);
				} else {
					// 如果预期的响应代码为"0",则应为此次请求参数正确,remote_id以tenant_id开头
					assertValueStartsWith(result, ScriptVariables.remote_id, getString(ScriptVariables.tenant_id));
					// 写入remote_id
					if (isSampleCorrect)
						put(ScriptVariables.remote_id, result.getString(ScriptVariables.remote_id));
				}
			}
		}
		return isSampleCorrect;
	}
}
