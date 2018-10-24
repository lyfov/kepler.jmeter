package kepler.jmeter.scriptJar;

import org.apache.jmeter.threads.JMeterVariables;

import static kepler.jmeter.scriptJar.utils.ScriptVariables.*;

public class JMeterSample {

    private JMeterVariables vars;
    protected StringBuffer errorMessage = new StringBuffer();
    protected boolean isSampleCorrect;
    protected boolean isThreadContinue;

    public JMeterSample(JMeterVariables vars) {
        this.vars = vars;
    }

    protected JMeterVariables getVars() {
        return vars;
    }

    /**
     * @param key key
     * @return 如果key不存在，返回-1
     */
    protected double getDouble(String key) {
        return Double.parseDouble(vars.get(key));
    }

    /**
     * @param key key
     * @return 如果key不存在，返回-1
     */
    protected long getLong(String key) {
        return Long.parseLong(vars.get(key));
    }

    /**
     * @param key key
     * @return 如果key不存在，返回-1
     */
    protected int getInt(String key) {
        return Integer.parseInt(vars.get(key));
    }

    /**
     * @param key key
     * @return 如果key不存在，返回false
     */
    protected boolean getBoolean(String key) {
        return Boolean.parseBoolean(vars.get(key));
    }

    /**
     * @param key key
     * @return 如果key不存在，返回"null"
     */
    protected String getString(String key) {
        return vars.get(key);
    }

    protected void put(String key, Object value) {
        vars.put(key, value.toString());
    }

    /**
     * 开始验证sample断言前，需调用此方法。 重置本对象中的变量isSampleCorrect为true; 重置errorMessage为空 ;
     * 重置JMeter中sample的断言值IS_SAMPLE_CORRECT为true;
     */
    protected void resetSampleAssert() {
        put(is_sample_correct, true);
        isSampleCorrect = true;
        errorMessage.setLength(0);
    }

    /**
     * 如果b==false，IS_THREAD_CORRECT和IS_SAMPLE_CORRECT、isSampleCorrect赋值为false。
     * 如果b==true，什么都不做。
     *
     * @param b 进程断言
     * @return b
     */
    protected boolean setSampleAssert(boolean b) {
        // 如果本sample错误，则is_thread_correct=false;如果本sample正确，则不改变is_thread_correct
        if (!b) {
            isSampleCorrect = false;
            put(is_thread_correct, false);
            put(is_sample_correct, false);
            setIsThreadContinue(false);
        }
        return b;
    }

    protected boolean setIsThreadContinue(boolean b) {
        isThreadContinue = b;
        put(is_thread_continue, b);
        return b;
    }
}