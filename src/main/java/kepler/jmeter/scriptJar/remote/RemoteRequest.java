package kepler.jmeter.scriptJar.remote;

import kepler.jmeter.scriptJar.Request;
import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.HashMap;
import java.util.Map;

public abstract class RemoteRequest extends Request {

    protected final String key_errorCode = "error_code";
    protected final String key_isFinish = "is_finish";
    protected final String key_errorMsg = "error_msg";
    protected final String key_test = "test";
    protected final String key_businessNo = "businessNo";
    protected final String key_remoteId = "remoteId";
    protected final String key_slaveId = "slaveId";
    protected final String key_content = "content";
    protected final String key_event = "event";

    protected Map<String,String> requestKey2scriptKey;

    public RemoteRequest(JMeterVariables vars) {
        super(vars);
        requestKey2scriptKey = new HashMap<>();
        requestKey2scriptKey.put(key_businessNo,ScriptVariables.business_no);
        requestKey2scriptKey.put(key_remoteId,ScriptVariables.remote_id);
        requestKey2scriptKey.put(key_slaveId,ScriptVariables.slave_id);
    }

    public void requestUri() {
        setRemoteUri(method, getString(ScriptVariables.remote_path) + path, requestData);
    }

}
