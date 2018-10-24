package kepler.jmeter.scriptJar;

import kepler.jmeter.scriptJar.utils.JSONTool;
import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordData extends JMeterSample {

    public RecordData(JMeterVariables vars) {
        super(vars);
    }

    /**
     * 把本次请求的记录写入record_data
     *
     * @param isCorrect
     * @param response
     * @param errorMessage
     */
    public void setRecordData(boolean isCorrect, String response, String errorMessage) {
        if (isCorrect)
            setSample2Total(response);
        else
            setSample2Total(response, errorMessage);
    }

    /**
     * 保存出错的请求及响应到reocrd_data
     *
     * @param responseData 响应信息
     * @param errorMessage 从接口对象中获得的错误信息
     */
    public void setSample2Total(String responseData, String errorMessage) {
        JSONObject sampleData = getSampleData(responseData);
        sampleData.put(ScriptVariables.error_message, errorMessage);
        sampleData.put(ScriptVariables.result_is_correct, false);
        JSONArray allData = new JSONArray(getString(ScriptVariables.record_data));
        allData.put(sampleData);
        put(ScriptVariables.record_data, allData.toString());
    }

    /**
     * 保存正确的请求及响应到reocrd_data
     *
     * @param responseData 响应信息
     */
    public void setSample2Total(String responseData) {
        JSONObject sampleData = getSampleData(responseData);
        sampleData.put(ScriptVariables.result_is_correct, true);
        JSONArray allData = new JSONArray(getString(ScriptVariables.record_data));
        allData.put(sampleData);
        put(ScriptVariables.record_data, allData.toString());
    }

    /**
     * 获取本sample的请求及响应的JSONObject
     *
     * @param responseData 响应信息
     * @return
     */
    private JSONObject getSampleData(String responseData) {
        JSONObject sampleData = new JSONObject();
        sampleData.put(ScriptVariables.method, getString(ScriptVariables.method));
        sampleData.put(ScriptVariables.result_uri, getString(ScriptVariables.protocol) + "://" + getString(ScriptVariables.server) + ":" + getString(ScriptVariables.port) + getString(ScriptVariables.path));
        String requestData = getString(ScriptVariables.request_data);
        if (JSONTool.isJSONValid(requestData))
            sampleData.put(ScriptVariables.result_request_data, new JSONObject(requestData));
        else
            sampleData.put(ScriptVariables.result_request_data, requestData);
        if (JSONTool.isJSONValid(responseData))
            sampleData.put(ScriptVariables.result_response, new JSONObject(responseData));
        else
            sampleData.put(ScriptVariables.result_response, responseData);
        return sampleData;
    }

    /**
     * 把record_data记录到文件，把断言错误的请求记录到simple文件。
     */
    public void writeResult2File() {
        Date d = new Date();
        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
        String pathSuf = File.separator + monthFormat.format(d) + File.separator + dateFormat.format(d) + File.separator;
        String time = timeFormat.format(d);
        // 路径
        String recordPath = getString(ScriptVariables.record_data_path) + pathSuf;
        String workSpacePath = getString(ScriptVariables.workspace_record_data_path) + File.separator;
        // 路径是否存在，如不存在先创建
        File file = new File(recordPath);
        if (!file.exists())
            file.mkdirs();
        file = new File(workSpacePath);
        if (!file.exists())
            file.mkdirs();
        // 数据
        saveTimeRecord();// 把用时记录加入到record_data
        String data = getString(ScriptVariables.record_data);
        String simpleData = getString(ScriptVariables.simple_record);
        writeDataToFile(recordPath + "result_" + time + ".txt", false, data);
        writeDataToFile(workSpacePath + "totalResult.txt", false, data);
        writeDataToFile(workSpacePath + "simpleResult.txt", false, simpleData);
        // 如果有错误，写simple到record路径
        if (!getBoolean(ScriptVariables.is_thread_correct))
            writeDataToFile(recordPath + "error_" + time + ".txt", false, simpleData);
    }

    private void saveTimeRecord() {
        if (getBoolean(ScriptVariables.is_thread_correct)) {
            JSONObject timeRecord = new JSONObject();
            timeRecord.put(ScriptVariables.collect_2_complete, getString(ScriptVariables.collect_2_complete));
            timeRecord.put(ScriptVariables.collect_2_zhimaComplete, getString(ScriptVariables.collect_2_zhimaComplete));
            JSONArray allData = new JSONArray(getString(ScriptVariables.record_data));
            allData.put(timeRecord);
            put(ScriptVariables.record_data, allData.toString());
        }
    }

    /**
     * @param filename
     * @param bb
     * @param ll
     */
    public static void writeDataToFile(String filename, boolean bb, String ll) {
        FileWriter fw;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(filename, bb);
            bw = new BufferedWriter(fw);
            // char c[] = ll.toCharArray();
            bw.write(ll);
            bw.flush(); // 一定要刷新缓冲区
            bw.newLine(); // 换行
            bw.close(); // 一定要关闭数据流
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
    }
}