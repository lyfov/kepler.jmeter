package kepler.jmeter.scriptJar;

import kepler.jmeter.scriptJar.utils.JSONTool;
import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author lyfov 请求的父类，包含创建URI，校验
 */
public class Request extends JMeterSample {

    protected String method;
    protected String path;
    protected String requestData;
    protected String responseData;
    public StringBuffer errorMessage = new StringBuffer();

    public Request(JMeterVariables vars) {
        super(vars);
    }

    /**
     * 生成Remote_Server的URI。用于publish、poll等接口。
     *
     * @param method 方法。POST,GET等
     * @param path   接口路径。/publish等
     */
    protected void setRemoteUri(String method, String path, String requestData) {
        setUri(method, path, requestData, ScriptVariables.remote_server_address, ScriptVariables.remote_server_port);
    }

    /**
     * 生成Query_Server的URI。用于get_remoteId等接口。
     *
     * @param method      方法。POST,GET等
     * @param path        接口路径。/get_remoteId等
     * @param requestData requestData
     */
    protected void setMasterUri(String method, String path, String requestData) {
        setUri(method, path, requestData, ScriptVariables.master_server_address, ScriptVariables.master_server_port);
    }

    private void setUri(String method, String path, String requestData, String server_address, String server_port) {
        put(ScriptVariables.is_sample_correct, true);
        put(ScriptVariables.method, method);
        put(ScriptVariables.server, getString(server_address));
        put(ScriptVariables.port, getString(server_port));
        put(ScriptVariables.path, path);
        put(ScriptVariables.request_data, requestData);
    }

    /**
     * 验证json字符串是否能符合json格式，如不符合，写入errorMessage
     *
     * @param json 字符串
     * @return 线程断言
     */
    protected boolean assertJSONObject(String json) {
        boolean isCorrect = JSONTool.isJSONValid(json);
        if (!isCorrect)
            errorMessage.append("'非JSONObject格式\r\n");
        return setSampleAssert(isCorrect);
    }

    /**
     * 验证json的key的值是否为JSONObject格式，如不符合，写入errorMessage
     *
     * @param json 父JSON
     * @param key  key
     * @return 线程断言
     */
    protected boolean assertJSONObject(JSONObject json, String key) {
        boolean isCorrect = true;
        try {
            json.getJSONObject(key);
        } catch (JSONException e) {
            errorMessage.append("'").append(key).append("'非JSONObject格式\r\n");
            isCorrect = false;
        }
        return setSampleAssert(isCorrect);
    }

    /**
     * 验证json的key的值是否为JSONArray格式，如不符合，写入errorMessage
     *
     * @param json 父JSON
     * @param key  key
     * @return 线程断言
     */
    protected boolean assertJSONArray(JSONObject json, String key) {
        boolean isCorrect = true;
        try {
            json.getJSONArray(key);
        } catch (JSONException e) {
            errorMessage.append("'").append(key).append("'非JSONArray格式\r\n");
            isCorrect = false;
        }
        return setSampleAssert(isCorrect);
    }

    protected boolean assertIntPlus(JSONObject json, String key, int min) {
        boolean isCorrect = true;
        try {
            int value = json.getInt(key);
            if (value < min) {
                errorMessage.append("'").append(key).append("'小于").append(min).append("\r\n");
                isCorrect = false;
            }
        } catch (Exception e) {
            isCorrect = false;
            errorMessage.append("'").append(key).append("'非Int类型\r\n");
        }
        return setSampleAssert(isCorrect);
    }

    /**
     * String类型，校验是否符合指定的正则表达式
     *
     * @param json  源JSONObject
     * @param key   key
     * @param regex 正则
     * @return 是否符合
     */
    protected boolean assertValueMatch(JSONObject json, String key, String regex) {
        boolean isCorrect = true;
        if (json.has(key)) {
            try {
                String value = json.getString(key);
                if (!value.matches(regex)) {
                    errorMessage.append("'").append(key).append("'数据错误\r\n");
                    isCorrect = false;
                }
            } catch (JSONException e) {
                isCorrect = false;
                errorMessage.append("'").append(key).append("'非String类型\r\n");
            }
        } else {
            errorMessage.append("无'").append(key).append("'\r\n");
            isCorrect = false;
        }
        return setSampleAssert(isCorrect);
    }

    protected boolean assertValueMatch(String value, String regex) {
        boolean isCorrect = true;
        if (!value.matches(regex)) {
            errorMessage.append("'").append(value).append("'数据错误\r\n");
            isCorrect = false;
        }
        return setSampleAssert(isCorrect);
    }

    /**
     * 验证JSON的key的值是否以startsWith开头，如不符合，写入errorMessage。
     *
     * @param json       父JSON
     * @param key        key
     * @param startsWith key的值预期以startsWith开头
     * @return 是否已startsWith开头
     */
    protected boolean assertValueStartsWith(JSONObject json, String key, String startsWith) {
        boolean isCorrect = true;
        if (json.has(key)) {
            try {
                String value = json.getString(key);
                if (!value.startsWith(startsWith)) {
                    errorMessage.append("'").append(key).append("数据错误\r\n");
                    isCorrect = false;
                }
            } catch (JSONException e) {
                isCorrect = false;
                errorMessage.append("'").append(key).append("'非String类型\r\n");
            }
        } else {
            errorMessage.append("无'").append(key).append("'\r\n");
            isCorrect = false;
        }
        return setSampleAssert(isCorrect);
    }

    /**
     * 判断两个JONSObject是否相同
     *
     * @param json1 作为基准的JSONObject
     * @param json2 要比较的JSONObject
     * @return 是否相同
     */
    protected boolean compareJSONObject(JSONObject json1, JSONObject json2) {
        return setSampleAssert(JSONTool.compareJson(json1, json2));
    }

    /**
     * 判断json1与json2构成的JSON是否相同
     *
     * @param json1 源JSON
     * @param json2 要被比较的JSON
     * @return 是否相同
     */
    protected boolean compareJSONObject(JSONObject json1, String json2) {
        if (JSONTool.isJSONValid(json2)) {
            setSampleAssert(JSONTool.compareJson(json1, new JSONObject(json2)));
        } else
            setSampleAssert(false);
        return isSampleCorrect;
    }

    /***
     * 验证json的key的值是否为expectedValue，如不符合，写入errorMessage。
     * 目前expectedValue的类型仅支持String,boolean,int,null
     *
     * @param json          父JSON
     * @param key           key
     * @param expectedValue key的预期值
     * @return 是否符合
     */
    protected boolean assertValue(JSONObject json, String key, Object expectedValue) {
        boolean isCorrect;
        if (json.has(key)) {
            Object jsonValue = json.get(key);
            if (!(isCorrect = jsonValue.equals(expectedValue)))
                errorMessage.append(key).append("不为").append(expectedValue).append(";");
        } else {
            errorMessage.append("无").append(key).append(";");
            isCorrect = false;
        }
        return setSampleAssert(isCorrect);
    }

    protected void setRecordData(){
        RecordData rd = new RecordData(getVars());
        rd.setRecordData(isSampleCorrect, responseData, errorMessage.toString());
        if (!isSampleCorrect)
            saveSimpleError(responseData);
    }

    protected void saveSimpleError(String response) {
        JSONArray simpleRecord = new JSONArray(getString(ScriptVariables.simple_record));
        JSONObject simple = new JSONObject();
        simple.put(ScriptVariables.result_uri, getString(ScriptVariables.protocol) + getString(ScriptVariables.server) + ":" + getString(ScriptVariables.port) + getString(ScriptVariables.path));
        simple.put(ScriptVariables.request_data, getString(ScriptVariables.request_data));
        if (JSONTool.isJSONValid(response))
            simple.put(ScriptVariables.result_response, new JSONObject(response));
        else
            simple.put(ScriptVariables.result_response, response);
        simple.put(ScriptVariables.result_error_message, errorMessage.toString());
        simpleRecord.put(simple);
        put(ScriptVariables.simple_record, simpleRecord.toString());
    }
}
