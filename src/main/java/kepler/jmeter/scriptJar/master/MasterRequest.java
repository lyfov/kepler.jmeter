package kepler.jmeter.scriptJar.master;

import kepler.jmeter.scriptJar.RecordData;
import kepler.jmeter.scriptJar.Request;
import kepler.jmeter.scriptJar.utils.ScriptVariables;
import kepler.jmeter.scriptJar.utils.StringTool;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public abstract class MasterRequest extends Request {

	public MasterRequest(JMeterVariables vars) {
		super(vars);
	}

	public void requestUri(String expectedCode) {
		setMasterUri(method(), getString(ScriptVariables.master_path)+path(), requestData(expectedCode));
	}

	/**
	 * 根据expectedCode获取请求要发送的参数
	 * 
	 * @param tenantId     正确的tenantId
	 * @param expectedCode 预期返回值code
	 * @return	请求附带的数据
	 */
	protected String getQueryServerDataByCode(String tenantId, String expectedCode) {
		String requestData;
		int idLength = tenantId.length();
		switch (expectedCode) {
		case "0":
			// 参数均正确，返回0
			requestData = "tenantId=" + tenantId;
			break;
		case "1001":
			// 参数中不包含tenantId，返回1001
			requestData = "";
			break;
		case "1002":
			String id = new Random().nextBoolean() ? StringTool.randomString(new Random().nextInt(idLength - 1) + 1) : StringTool.randomString(new Random().nextInt(idLength * 2) + idLength + 1);
			requestData = "tenantId=" + id;
			break;
		case "1005":
			requestData = "tenantId=" + StringTool.randomString(idLength);
			break;
		default:
			requestData = "requestData_default";
			break;
		}
		return requestData;
	}

	/**
	 * 根据expectedCode获取请求要发送的参数 其中code=0为正确的请求，其他case可能由1001_1,1002_3等
	 * 
	 * @param tenantId     正确的tenantId
	 * @param businessNo   正确的businessNo
	 * @param expectedCode 预期返回值code
	 * @return	请求附带的参数数据
	 */
	String getQueryServerDataByCode(String tenantId, String businessNo, String expectedCode) {
		String requestData;
		int idLength = tenantId.length();
		int noLength = businessNo.length();
		Random random = new Random();
		// 长度不符的tenantId
		String randomLenTenantId = random.nextBoolean() ? StringTool.randomString(random.nextInt(idLength - 1) + 1) : StringTool.randomString(random.nextInt(idLength * 2) + idLength + 1);
		// 长度不符的businessNo
		String randomLenBusinessNo = random.nextBoolean() ? StringTool.randomString(random.nextInt(noLength - 1) + 1) : StringTool.randomString(random.nextInt(noLength * 2) + noLength + 1);
		switch (expectedCode) {
		case "0":
			// 参数均正确，返回0
			requestData = "tenantId=" + tenantId + "&businessNo=" + businessNo;
			break;
		case "1001_1":
			// 参数中不包含tenantId，businessNo错误，返回1001
			requestData = "businessNo=" + StringTool.randomString(noLength);
			break;
		case "1001_2":
			// 参数中不包含tenantId，businessNo正确，返回1001
			requestData = "businessNo=" + businessNo;
			break;
		case "1002_1":
			// 参数中tenantId长度不符，businessNo长度不符，返回1002
			requestData = "tenantId=" + randomLenTenantId + "&businessNo=" + randomLenBusinessNo;
			break;
		case "1002_2":
			// 参数中tenantId长度不符，businessNo正确，返回1002
			requestData = "tenantId=" + randomLenTenantId + "&businessNo=" + businessNo;
			break;
		case "1002_3":
			// 参数中tenantId正确，businessNo长度不符，返回1002
			requestData = "tenantId=" + tenantId + "&businessNo=" + randomLenBusinessNo;
			break;
		case "1003_1":
			// businessNo和tenantId长度合法，对应businessNo无数据，tenantId正确，返回1003
			requestData = "tenantId=" + tenantId + "&businessNo=" + StringTool.randomString(noLength);
			break;
		case "1003_2":
			// businessNo和tenantId长度合法，对应businessNo无数据，tenantId错误，返回1003
			requestData = "tenantId=" + StringTool.randomString(idLength) + "&businessNo=" + StringTool.randomString(noLength);
			break;
		case "1005":
			// tenantId长度合法，businessNo正确，tenantId错误，返回1005
			requestData = "tenantId=" + StringTool.randomString(idLength) + "&businessNo=" + businessNo;
			break;
		default:
			requestData = "requestData_default";
			break;
		}
		return requestData;
	}

	/**
	 * 后置处理，包括写入is_sample_correct、is_thread_correct 写入record_data
	 * 
	 * @param prev         JMeter的prev对象
	 * @param expectedCode 预期code
	 * @return is_sample_correct
	 */
	public boolean postProcessor(SampleResult prev, String expectedCode) {
		resetSampleAssert();
		responseData = new String(prev.getResponseData(), StandardCharsets.UTF_8);
		// expectedCode可能为1001_1等，校验前切割为1001
		expectedCode = expectedCode.length() > 4 ? expectedCode.substring(0, 4) : expectedCode;
		isSampleCorrect(responseData, expectedCode);
		setRecordData();
		return isSampleCorrect;
	}

	public abstract boolean isSampleCorrect(String responseData, String expectedCode);

	protected abstract String method();

	protected abstract String path();

	protected abstract String requestData(String expectedCode);
}
