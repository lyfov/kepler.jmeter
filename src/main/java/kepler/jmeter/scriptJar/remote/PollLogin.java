package kepler.jmeter.scriptJar.remote;

import kepler.jmeter.scriptJar.timer.TimeTool;
import kepler.jmeter.scriptJar.utils.ScriptVariables;
import kepler.jmeter.scriptJar.utils.StringTool;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONArray;
import org.json.JSONObject;


public class PollLogin extends Poll {
    private final String VERIFICA_NAME = "VERIFICA_NAME";
    private final String VERIFICA_ID = "VERIFICA_ID";
    private final String PWD_CODE_INPUT = "PWD_CODE_INPUT";
    private final String CHOOSE_BANK_CARD = "CHOOSE_BANK_CARD";
    private final String CHOOSE_RANDOM = "CHOOSE_RANDOM";
    private final String TABLE_VERIFICA = "TABLE_VERIFICA";
    private final String LIVING_VALIDATE_START = "LIVING_VALIDATE_START";

    private String event;

    private final String[] verificas = {VERIFICA_NAME, VERIFICA_ID, PWD_CODE_INPUT, CHOOSE_BANK_CARD, CHOOSE_RANDOM, TABLE_VERIFICA, LIVING_VALIDATE_START};

    public PollLogin(JMeterVariables vars, int loopMaxCount) {
        super(vars, loopMaxCount);
    }

    public boolean postProcessorByCsv(SampleResult prev) {
        if (!isLoopContinue(prev)) {
            assertResponseByCsv();
            setLoginIndexByCsv();
            setRecordData();
        }
        return isSampleCorrect;
    }

    private boolean assertResponseByCsv() {
        if (assertJSONObject(responseData)) {
            JSONObject response = new JSONObject(responseData);
            // remoteId,businessNo,slaveId
            assertNormalKeys(response);
            // 从csv中读取的poll结果
            JSONObject casePoll = new JSONArray(getString(ScriptVariables.line_case)).getJSONObject(getInt(ScriptVariables.login_case_index)).getJSONObject("poll");
            // event
            event = casePoll.getString(key_event);
            if (assertEvent(response, event)) {
                // content
                assertContentByCsv(response, casePoll);
            }
        }
        return isSampleCorrect;
    }

    private boolean assertEvent(JSONObject response, String event) {
        String responseEvent = response.getString(key_event);
        // 如果触发二次认证，修改to_cancel，使脚本直接发送cancel，结束本次线程
        if (StringTool.inArray(responseEvent, verificas)) {
            put(ScriptVariables.to_cancel, true);
            return false;
        } else
            return (assertValue(response, key_event, event));
    }

    private boolean assertContentByCsv(JSONObject response, JSONObject casePoll) {
        if (casePoll.has(key_content)) {
            if (event.equals("ERROR")) {
                JSONObject caseContent = new JSONObject(casePoll.getString(key_content));
                // 校验响应content的JSONObject格式。content应为可以转为JSONObject的字符串
                if (assertJSONObject(response, key_content)) {
                    // 响应数据中的content字符串所构成的JSONObject
                    JSONObject responseContentJSONObject = new JSONObject(response.getString(key_content));
                    // 校验error_code
                    assertValue(responseContentJSONObject, key_errorCode, caseContent.getString(key_errorCode));
                    // 验证is_finish
                    assertValue(responseContentJSONObject, key_isFinish, caseContent.getBoolean(key_isFinish));
                    // 验证error_msg，只校验是否存在，且为String格式
                    assertValueMatch(responseContentJSONObject, key_errorMsg, ".+");
                }
            } else {
                String caseContent = casePoll.getString(key_content);
                assertValue(response, key_content, caseContent);
                if (caseContent.equals("COLLECT"))
                    new TimeTool(getVars()).setSampleTime(ScriptVariables.collect_time);
            }
            return isSampleCorrect;
        } else
            return true;
    }

    private void setLoginIndexByCsv() {
        // 处理login循环次数
        int index = getInt(ScriptVariables.login_case_index);
        index++;
        put(ScriptVariables.login_case_index, index);
        if (index >= getInt(ScriptVariables.login_case_length)) {
            // login_loop_condition
            put(ScriptVariables.login_loop_condition, false);
        }
    }

}
