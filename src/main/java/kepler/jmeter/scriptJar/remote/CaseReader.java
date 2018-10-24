package kepler.jmeter.scriptJar.remote;

import kepler.jmeter.scriptJar.JMeterSample;
import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONArray;

import kepler.jmeter.scriptJar.Request;

public class CaseReader extends JMeterSample {
	public int caseLength;

	public CaseReader(JMeterVariables vars) {
		super(vars);
	}

	/**
	 * 本线程读取的case的JSONArray的长度。 并写入case_length
	 * @return case总数
	 */
	public int caseLength() {
		caseLength = new JSONArray(getString(ScriptVariables.line_case)).length();
		
		// case长度
		put(ScriptVariables.login_case_length, caseLength);
		
		// case_index
		put(ScriptVariables.login_case_index, 0);

		// to_cancel
		put(ScriptVariables.to_cancel,getBoolean(ScriptVariables.case_to_cancel));
		
		// login_loop_condition
		put(ScriptVariables.login_loop_condition, true);
		return caseLength;
	}
}