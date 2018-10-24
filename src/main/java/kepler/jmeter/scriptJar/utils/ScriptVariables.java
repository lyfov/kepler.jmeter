package kepler.jmeter.scriptJar.utils;

public class ScriptVariables {

    // 时间记录
    public static final String collect_time = "collect_time";
    public static final String complete_time = "complete_time";
    public static final String zhima_complete_time = "zhima_complete_time";
    public static final String collect_2_zhimaComplete = "collect_2_zhimaComplete";
    public static final String collect_2_complete = "collect_2_complete";

    // 线程控制
    public static final String is_sample_correct = "is_sample_correct";
    public static final String is_thread_correct = "is_thread_correct";
    public static final String is_thread_continue = "is_thread_continue";
    public static final String login_case_index = "login_case_index";
    public static final String login_case_length = "login_case_length";
    public static final String login_loop_condition = "login_loop_condition";
    public static final String poll_loop_condition = "poll_loop_condition";
    public static final String poll_loop_count = "poll_loop_count";
    public static final String to_cancel = "to_cancel";
    public static final String case_to_cancel = "case_to_cancel";

    // 数据
    public static final String tenant_id = "tenant_id";
    public static final String remote_id = "remote_id";
    public static final String slave_id = "slave_id";
    public static final String business_no = "business_no";
    public static final String line_case = "line_case";
    public static final String line_result = "line_result";
    public static final String error_message = "error_message";
    public static final String record_data = "record_data";
    public static final String simple_record = "simple_record";
    public static final String request_data = "request_data";
    public static final String low_slave_num = "low_slave_num";
    public static final String low_idle_slave_num = "low_idle_slave_num";
    public static final String slave_num = "slave_num";
    public static final String idle_slave_num = "idle_slave_num";

    // RESULT数据
    public static final String result_is_correct = "is_correct";
    public static final String result_uri = "uri";
    public static final String result_request_data = "requestData";
    public static final String result_response = "response";
    public static final String result_error_message = "error_message";

    // 文件路径
    public static final String remote_path = "remote_path";
    public static final String master_path = "master_path";
    public static final String record_data_path = "record_data_path";
    public static final String workspace_record_data_path = "workspace_record_data_path";

    // SERVER
    public static final String method = "method";
    public static final String protocol = "protocol";
    public static final String server = "server";
    public static final String port = "port";
    public static final String path = "path";
    public static final String remote_server_address = "remote_server_address";
    public static final String remote_server_port = "remote_server_port";
    public static final String master_server_address = "master_server_address";
    public static final String master_server_port = "master_server_port";

}
