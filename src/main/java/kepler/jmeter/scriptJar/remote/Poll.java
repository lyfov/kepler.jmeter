package kepler.jmeter.scriptJar.remote;

import kepler.jmeter.scriptJar.timer.TimeTool;
import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

/**
 * 用于轮询ZHIMA_COMPLETE和COMPLETE的poll类
 */
public class Poll extends RemoteRequest {

    protected int loopMaxCount;

    /** 创建用于校验响应的实例时，使用这个构造函数
     * @param vars         JMeter的vars对象
     * @param loopMaxCount poll最大循环次数,决定了轮询等待的时间
     */
    public Poll(JMeterVariables vars, int loopMaxCount) {
        super(vars);
        setUpRequest();
        this.loopMaxCount = loopMaxCount;
    }

    /**创建用于发送请求的实例时，使用这个构造函数
     * @param vars  vars
     */
    public Poll(JMeterVariables vars) {
        super(vars);
        setUpRequest();
        put(ScriptVariables.poll_loop_condition,true);
        put(ScriptVariables.poll_loop_count, 0);
    }

    private void setUpRequest() {
        method = "post";
        path = "/poll";
        requestData = "{remoteId: \"" + getString(ScriptVariables.remote_id) + "\"}";
    }

    /**
     * sample后置处理
     *
     * @param prev  JMeter的prev对象
     * @param event 响应中event字段预期值
     * @return 断言
     */
    public boolean postProcessor(SampleResult prev, String event) {
        if (isLoopContinue(prev))
            return true;
        assertResponse(responseData, event);
        setRecordData();
        return isSampleCorrect;
    }

    /**
     * 判断是否继续循环。 当循环达到最大次数，或响应值不为{}时结束循环 结束循环后应调用isSampleCorrect方法判断该请求的响应是否正确
     * 并为responseData赋值
     *
     * @param prev prev
     * @return 断言
     */
    public boolean isLoopContinue(SampleResult prev) {
        resetSampleAssert();
        responseData = new String(prev.getResponseData(), StandardCharsets.UTF_8);

        boolean isLoopContinue = true;
        if (!responseData.equals("{}")) {
            put(ScriptVariables.poll_loop_condition, false);
            isLoopContinue = false;
        }
        int loopCount = getInt(ScriptVariables.poll_loop_count);
        put(ScriptVariables.poll_loop_count, ++loopCount);
        if (loopCount >= loopMaxCount) {
            put(ScriptVariables.poll_loop_condition, false);
            isLoopContinue = false;
            errorMessage.append("poll循环次数超过").append(loopMaxCount).append(";");
        }
        return isLoopContinue;
    }

    /**
     * 判断返回响应是否正确，并赋值给脚本中的is_thread_correct和is_sample_correct;
     * 不会将is_thread_correct从false赋值为true 如business_no及slave_id在脚本中为default，则将赋值
     *
     * @param responseData 响应数据
     * @param event        预期的event字段
     * @return isSampleCorrect
     */
    protected boolean assertResponse(String responseData, String event) {
        // json格式校验
        if (assertJSONObject(responseData)) {
            JSONObject response = new JSONObject(responseData);
            // businessNo,remoteId,slaveId
            assertNormalKeys(response);
            // event字段校验
            assertEvent(response,event);
        }
        return isSampleCorrect;
    }

    private boolean assertEvent(JSONObject response, String event) {
        if (assertValue(response, key_event, event)) {
            TimeTool timeTool = new TimeTool(getVars());
            // 如果为COMPLETE，计算爬取时间
            if (event.equals("COMPLETE")) {
                timeTool.setSampleTime(ScriptVariables.complete_time);
                timeTool.setTimeMinus(ScriptVariables.collect_time, ScriptVariables.complete_time, ScriptVariables.collect_2_complete);
            } else if (event.equals("ZHIMA_COMPLETE")) { // 如果为ZHIMA_COMPLETE，计算爬取时间
                timeTool.setSampleTime(ScriptVariables.zhima_complete_time);
                timeTool.setTimeMinus(ScriptVariables.collect_time, ScriptVariables.zhima_complete_time, ScriptVariables.collect_2_zhimaComplete);
            }
        }
        return isSampleCorrect;
    }

    /**
     * 校验remoteId,businessNo,slaveId
     *
     * @param response 响应的JSONObject
     * @return isSampleCorrect
     */
    protected boolean assertNormalKeys(JSONObject response) {
        assertResponseKey(response, key_remoteId);
        assertResponseKey(response, key_businessNo);
        assertResponseKey(response, key_slaveId);
        return isSampleCorrect;
    }


    /**
     * @param response    响应的JSONObject
     * @param responseKey 要校验的key(response中)，仅包含remoteId,businessNo,slaveId
     * @return isSampleCorrect
     */
    protected boolean assertResponseKey(JSONObject response, String responseKey) {
        if (this instanceof PollBegin) {
            if (responseKey.equals(key_remoteId)) {
                // remoteId字段校验
                assertValue(response, key_remoteId, getString(ScriptVariables.remote_id));
            }
            if (assertValueMatch(response, responseKey, ".+")) {
                put(requestKey2scriptKey.get(responseKey), response.getString(responseKey));
            }
        } else {
            assertValue(response, responseKey, getString(requestKey2scriptKey.get(responseKey)));
        }
        return isSampleCorrect;
    }
}
