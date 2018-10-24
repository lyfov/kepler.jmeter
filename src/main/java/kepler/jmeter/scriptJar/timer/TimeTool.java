package kepler.jmeter.scriptJar.timer;

import kepler.jmeter.scriptJar.JMeterSample;
import kepler.jmeter.scriptJar.utils.JSONTool;
import kepler.jmeter.scriptJar.utils.ScriptVariables;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.*;

public class TimeTool extends JMeterSample {

    public final String max = "max";
    public final String min = "min";
    public final String avg = "avg";
    public final String line90 = "line90";
    public final String line80 = "line80";
    public final String middle = "middle";

    public TimeTool(JMeterVariables vars) {
        super(vars);
    }

    public long setSampleTime(String key) {
        long time = System.currentTimeMillis();
        put(key, time);
        return time;
    }

    public long setTimeMinus(String startKey, String endKey, String minusKey) {
        long minus = getLong(endKey) - getLong(startKey);
        put(minusKey, minus);
        return minus;
    }

    public Map<String, Map<String, Long>> calulateTime(String path) {
        Map<String, Map<String, Long>> calculate = new HashMap<>();
        Map<String, Long[]> times = getAllTimes(path);
        Map<String, Long> completeTimes = getStatistic(times.get(ScriptVariables.collect_2_complete));
        calculate.put(ScriptVariables.collect_2_complete, completeTimes);
        Map<String, Long> zhimaCompleteTimes = getStatistic(times.get(ScriptVariables.collect_2_zhimaComplete));
        calculate.put(ScriptVariables.collect_2_zhimaComplete, zhimaCompleteTimes);
        return calculate;
    }

    private Map<String, Long> getStatistic(Long[] longs) {
        if (longs.length == 0)
            return null;
        Map<String, Long> statistic = new HashMap<>();
        long sumLong = 0;
        Arrays.sort(longs);
        for (Long a :
                longs) {
            sumLong += a;
        }
        int length = longs.length;

        statistic.put(max, longs[length - 1]);
        statistic.put(min, longs[0]);
        statistic.put(avg, sumLong / longs.length);
        statistic.put(line90, longs[(int) (length * 0.9) - 1]);
        statistic.put(line80, longs[(int) (length * 0.8) - 1]);
        statistic.put(middle,longs[length / 2]);
        return statistic;
    }

    private Map<String, Long[]> getAllTimes(String path) {
        List<Long> complete = new ArrayList<>();
        List<Long> zhimaComplete = new ArrayList<>();

        File filePath = new File(path);
        String[] fileList = filePath.list();
        for (String fileName : fileList
        ) {
            if (fileName.startsWith("result")) {
                String file = path + File.separator + fileName;
                JSONArray result = JSONTool.getJsonArrayfromFile(file);
                JSONObject time = result.getJSONObject(result.length() - 1);
                if (time.has(ScriptVariables.collect_2_zhimaComplete))
                    zhimaComplete.add(time.getLong(ScriptVariables.collect_2_zhimaComplete));
                if (time.has(ScriptVariables.collect_2_complete))
                    complete.add(time.getLong(ScriptVariables.collect_2_complete));
            }
        }
        Map<String, Long[]> times = new HashMap<>();
        Long[] complteArray = new Long[complete.size()];
        Long[] zhimaComplteArray = new Long[zhimaComplete.size()];
        complete.toArray(complteArray);
        zhimaComplete.toArray(zhimaComplteArray);
        times.put(ScriptVariables.collect_2_complete, complteArray);
        times.put(ScriptVariables.collect_2_zhimaComplete, zhimaComplteArray);
        return times;
    }

}
