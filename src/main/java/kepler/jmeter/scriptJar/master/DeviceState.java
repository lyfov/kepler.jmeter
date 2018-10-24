package kepler.jmeter.scriptJar.master;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONArray;
import org.json.JSONObject;

import kepler.jmeter.scriptJar.RecordData;
import kepler.jmeter.scriptJar.Request;

public class DeviceState extends Request {

	public DeviceState(JMeterVariables vars) {
		super(vars);
	}

	public void requestUri() {
		setMasterUri("post", "/device_state", "");
	}

	public boolean postProcessor(SampleResult prev) {
		String response = new String(prev.getResponseData(), StandardCharsets.UTF_8);
		RecordData rd = new RecordData(getVars());
		rd.setRecordData(isSampleCorrect(response), response, errorMessage.toString());
		return isSampleCorrect;
	}

	/**
	 * @param responseData	响应数据
	 * @return	断言
	 */
	private boolean isSampleCorrect(String responseData) {
		resetSampleAssert();

		int lowIdleSlaveNum = getInt(ScriptVariables.low_idle_slave_num);
		int lowSlaveNum = getInt(ScriptVariables.low_slave_num);

		int idleSlaveNum;
		int connectingSlaveNum = 0;
		int slaveNum = 0;
		// 校验JSON格式
		if (assertJSONObject(responseData)) {

			JSONObject response = new JSONObject(responseData);

			// 校验code
			assertValue(response, "code", 0);

			// 校验result的JSONObject格式
			if (assertJSONObject(response, "result")) {
				JSONObject result = response.getJSONObject("result");

				// 校验"设备状态"的JSONArray格式
				if (assertJSONArray(result, "设备状态")) {
					JSONArray stateArray = result.getJSONArray("设备状态");
					slaveNum = getSlaveNum(stateArray);
				}

				// 连接状态
				if (assertJSONArray(result, "连接状态")) {
					JSONArray connectionStatus = result.getJSONArray("连接状态");
					connectingSlaveNum = getSlaveNum(connectionStatus);
				}
			}
		}

		idleSlaveNum = slaveNum - connectingSlaveNum;

		put(ScriptVariables.slave_num, slaveNum);
		put(ScriptVariables.idle_slave_num, idleSlaveNum);

		if (isSampleCorrect) {
			if (slaveNum <= lowSlaveNum) {
				setSampleAssert(false);
				errorMessage.append("环境：").append(getString(ScriptVariables.server)).append(":").append(getString(ScriptVariables.port)).append("\r\n告警:设备总数：").append(slaveNum);
			}
			if (idleSlaveNum <= lowIdleSlaveNum) {
				setSampleAssert(false);
				if (slaveNum >= lowSlaveNum) {
					errorMessage.append("环境：").append(getString(ScriptVariables.server)).append(":").append(getString(ScriptVariables.port));
				}
				errorMessage.append("\r\n告警:空闲设备总数：").append(idleSlaveNum);
			}
		}

		put("message", errorMessage.toString());
		return isSampleCorrect;
	}

	private int getSlaveNum(JSONArray stateArray) {
		int slaveNum = 0;
		for (int i = 0; i < stateArray.length(); i++) {
			JSONObject state = stateArray.getJSONObject(i);

			// 校验每个设备状态
			if (assertValueMatch(state, "slaveId", ".+")) {
				String slaveId = state.getString("slaveId");

				// 非mock设备
				if (!slaveId.startsWith("mock")) {

					slaveNum++;
				}
			}
		}
		return slaveNum;
	}

	public void writeDataToFile(String path) {
		String data = getString(ScriptVariables.record_data);
		JSONObject t = new JSONObject();
		// 使用Date
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-ss");
		String time = sdf.format(d);
		t.put(time, new JSONArray(data));
		RecordData.writeDataToFile(path, true, t.toString());
	}
}
