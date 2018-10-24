package kepler.jmeter.scriptJar.remote;

import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONObject;

public class PollBegin extends Poll {
    private String event = "DEVICE_STATUS";
    private String content = "READY";

    public PollBegin(JMeterVariables vars, int loopMaxCount) {
        super(vars, loopMaxCount);
    }

    public boolean postProcessor(SampleResult prev) {
        if (!isLoopContinue(prev)){
            assertResponse();
            setRecordData();
        }
        return isSampleCorrect;
    }

    protected boolean assertResponse() {
        if (assertJSONObject(responseData)) {
            JSONObject response = new JSONObject(responseData);
            assertNormalKeys(response);
            assertValue(response, key_event, event);
            assertValue(response,key_content,content);
        }
        return isSampleCorrect;
    }
}
