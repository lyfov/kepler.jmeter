package kepler.jmeter.scriptJar.master;

import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONObject;

public class QueryZhimaScore extends MasterRequest {
    static String scoreRegex = "信用.+分数\\D*\\d+评估时间:\\d{4}-\\d{2}-\\d{2}";

    public QueryZhimaScore(JMeterVariables vars) {
        super(vars);
    }

    @Override
    public boolean isSampleCorrect(String responseData, String expectedCode) {
        // JSON格式校验
        if (assertJSONObject(responseData)) {
            JSONObject response = new JSONObject(responseData);

            // code字段校验
            if (assertValue(response, "code", Integer.parseInt(expectedCode))) {

                // business_no、remote_id、zhima_socre字段校验
                if (expectedCode.equals("0")) {
                    // 校验business_no
                    assertValue(response, "business_no", getString(ScriptVariables.business_no));

                    // 校验remote_id
                    assertValue(response, "remote_id", getString(ScriptVariables.remote_id));

                    // 校验zhima_socre
                    assertValueMatch(response, "zhima_socre", scoreRegex);
                }
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
        return "/query_zhima_score";
    }

    @Override
    protected String requestData(String expectedCode) {
        return getQueryServerDataByCode(getString(ScriptVariables.tenant_id), getString(ScriptVariables.business_no), expectedCode);
    }
}
