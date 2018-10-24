package kepler.jmeter.scriptJar.remote;

import kepler.jmeter.scriptJar.RecordData;
import kepler.jmeter.scriptJar.Request;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import static kepler.jmeter.scriptJar.utils.ScriptVariables.*;

public class Publish extends RemoteRequest {
    private JSONObject requestData;
    public StringBuffer errorMessage = new StringBuffer();

    public Publish(JMeterVariables vars) {
        super(vars);
    }

    /**
     * 使用从csv中读取的case创建uri时，调用此方法。 将根据当前执行到的case的index读取数据创建uri
     */
    @Override
    public void requestUri() {
        // 当前执行到的case的index
        int caseIndex = getInt(login_case_index);
        // 如果index超出，则已经没有case要继续执行
        if (caseIndex < getInt(login_case_length)) {
            // login_loop_condition
            put(login_loop_condition, true);

            // 当前case
            JSONObject currentCase = new JSONArray(getString(line_case)).getJSONObject(caseIndex).getJSONObject("publish");

            if (currentCase.has(key_content))
                requestUri(currentCase.getString(key_event), currentCase.getString(key_content));
            else
                requestUri(currentCase.getString(key_event));
        } else {
            // login_loop_condition
            put(login_loop_condition, false);
        }
    }

    /**
     * 生成URI及请求参数。并重置is_sample_correct为true
     *
     * @param event 参数中的event字段
     */
    public void requestUri(String event) {
        setRequestData(event);
        setRemoteUri("post", getString(remote_path)+"/publish", requestData.toString());
    }


    /**
     * 生成URI及请求参数。并重置is_sample_correct为true
     *
     * @param event   参数中的event字段
     * @param content 参数中的content字段
     */
    public void requestUri(String event, String content) {
        setRequestData(event);
        requestData.put(key_content, content);
        setRemoteUri("post", getString(remote_path)+"/publish", requestData.toString());
    }

    /**
     * 后置处理，包括写入is_sample_correct、is_thread_correct、写入record_data
     *
     * @param prev jmeter的prev对象
     * @return is_sample_correct
     */
    public boolean postProcessor(SampleResult prev) {
        String response = new String(prev.getResponseData(), StandardCharsets.UTF_8);
        boolean isCorrect = isSampleCorrect(response);
        RecordData rd = new RecordData(getVars());
        rd.setRecordData(isCorrect, response, errorMessage.toString());
        if (!isSampleCorrect)
            saveSimpleError(response);
        return isCorrect;
    }

    /**
     * 生成URI及请求参数。并重置is_sample_correct为true
     *
     * @param event event
     */
    private void setRequestData(String event) {
        requestData = new JSONObject();
        requestData.put("isTest", false);
        requestData.put("appName", "alipay");
        requestData.put("event", event);
        requestData.put("remoteId", getString(remote_id));
        requestData.put("tenantId", getString(tenant_id));
        if (!getString(business_no).equals("default"))
            requestData.put("businessNo", getString("business_no"));
    }

    /**
     * 判断返回响应是否正确(即=={})，并赋值给脚本中的is_thread_correct和is_sample_correct;
     * 不会将is_thread_correct从false赋值为true
     *
     * @param responseData responseData
     * @return assert
     */
    public boolean isSampleCorrect(String responseData) {
        resetSampleAssert();
        boolean isCorrect = "{}".equals(responseData);
        // 如果本sample错误，则is_thread_correct=false;如果本sample正确，则不改变is_thread_correct
        if (!isCorrect) {
            put("is_thread_correct", "false");
            errorMessage.append("响应不为'{}'; ");
        }
        put("is_sample_correct", String.valueOf(isCorrect));
        return isCorrect;
    }
}