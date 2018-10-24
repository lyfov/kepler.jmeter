package kepler.jmeter.queryInterface;

import kepler.jmeter.scriptJar.RecordData;
import kepler.jmeter.scriptJar.master.GetRemoteId;
import kepler.jmeter.scriptJar.master.QueryDataByBusinessno;
import kepler.jmeter.scriptJar.remote.CaseReader;
import kepler.jmeter.scriptJar.remote.Poll;
import kepler.jmeter.scriptJar.timer.TimeTool;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class STest {

    public static void main(String[] args) {
        JMeterVariables vars = getVars();
//        new CaseReader(vars).caseLength();
        String str ="{\"result\":{\"address_list\":[],\"huabei\":{\"amount\":\"\",\"freeze\":\"\",\"balance\":\"\",\"bill\":\"\"},\"zhima_data\":{\"zhima_center\":\"信用极好分数787评估时间:2018-10-06\"},\"person_info\":{\"name\":\"李思维\",\"credentials_no\":\"1****************1\"},\"bad_credit_recoder\":[],\"pay_bill_list\":{\"datalist\":[{\"bizType\":\"TRADE\",\"tagStatus\":0,\"recordType\":\"CONSUME\",\"gmtCreate\":1534836003000,\"consumeStatus\":\"2\",\"oppositeLogo\":\"https://t.alipayobjects.com/images/logoimg/TB12zPqXkRDDuNkUvLzwu1PTpXa.png_[pixelWidth]x.png\",\"bizInNo\":\"2018082121001004290570726914\",\"bizSubType\":\"1\",\"createTime\":\"15:20\",\"consumeTitle\":\"百度网盘超级会员(1个月)\",\"createDesc\":\"08-21\",\"canDelete\":true,\"consumeFee\":\"-20.00\",\"actionParam\":{\"autoJumpUrl\":\"alipays://platformapi/startapp?appId=66666676&appClearTop=false&url=%2Fwww%2Findex.html%3FtradeNo%3D2018082121001004290570726914%26bizType%3DTRADE%26appClearTop%3Dfalse%26startMultApp%3DYES\",\"type\":\"APP\"},\"isAggregatedRec\":false},{\"bizType\":\"TRADE\",\"tagStatus\":0,\"recordType\":\"CONSUME\",\"gmtCreate\":1534835638000,\"consumeStatus\":\"2\",\"oppositeLogo\":\"https://t.alipayobjects.com/images/logoimg/TB12zPqXkRDDuNkUvLzwu1PTpXa.png_[pixelWidth]x.png\",\"bizInNo\":\"2018082121001004290570960523\",\"bizSubType\":\"1\",\"createTime\":\"15:13\",\"consumeTitle\":\"百度网盘会员(1个月)\",\"createDesc\":\"08-21\",\"canDelete\":true,\"consumeFee\":\"-10.00\",\"actionParam\":{\"autoJumpUrl\":\"alipays://platformapi/startapp?appId=66666676&appClearTop=false&url=%2Fwww%2Findex.html%3FtradeNo%3D2018082121001004290570960523%26bizType%3DTRADE%26appClearTop%3Dfalse%26startMultApp%3DYES\",\"type\":\"APP\"},\"isAggregatedRec\":false},{\"bizType\":\"TRADE\",\"tagStatus\":0,\"recordType\":\"CONSUME\",\"gmtCreate\":1534824098000,\"consumeStatus\":\"2\",\"oppositeLogo\":\"https://gw.alipayobjects.com/zos/mwalletmng/mukPPhtdXrnqECpCXXDq.png\",\"bizInNo\":\"2018082121001004290571186513\",\"bizSubType\":\"102\",\"createTime\":\"12:01\",\"consumeTitle\":\"护国寺小吃草房店\",\"createDesc\":\"08-21\",\"canDelete\":true,\"consumeFee\":\"-18.00\",\"actionParam\":{\"autoJumpUrl\":\"alipays://platformapi/startapp?appId=66666676&appClearTop=false&url=%2Fwww%2Findex.html%3FtradeNo%3D2018082121001004290571186513%26bizType%3DTRADE%26appClearTop%3Dfalse%26startMultApp%3DYES\",\"type\":\"APP\"},\"isAggregatedRec\":false},{\"bizType\":\"TRADE\",\"tagStatus\":0,\"recordType\":\"CONSUME\",\"gmtCreate\":1534671283000,\"consumeStatus\":\"2\",\"oppositeLogo\":\"wHt7YamERaqKv_-BiXroSQAAACMAAQED\",\"bizInNo\":\"2018081921001004290564140661\",\"bizSubType\":\"66\",\"createTime\":\"17:34\",\"consumeTitle\":\"欧尚\",\"createDesc\":\"08-19\",\"canDelete\":true,\"consumeFee\":\"-81.01\",\"actionParam\":{\"autoJumpUrl\":\"alipays://platformapi/startapp?appId=66666676&appClearTop=false&url=%2Fwww%2Findex.html%3FtradeNo%3D2018081921001004290564140661%26bizType%3DTRADE%26appClearTop%3Dfalse%26startMultApp%3DYES\",\"type\":\"APP\"},\"isAggregatedRec\":false},{\"bizType\":\"TRADE\",\"tagStatus\":0,\"recordType\":\"CONSUME\",\"gmtCreate\":1534659668000,\"consumeStatus\":\"2\",\"oppositeLogo\":\"wHt7YamERaqKv_-BiXroSQAAACMAAQED\",\"bizInNo\":\"2018081921001004290563773330\",\"bizSubType\":\"66\",\"createTime\":\"14:21\",\"consumeTitle\":\"欧尚\",\"createDesc\":\"08-19\",\"canDelete\":true,\"consumeFee\":\"-34.80\",\"actionParam\":{\"autoJumpUrl\":\"alipays://platformapi/startapp?appId=66666676&appClearTop=false&url=%2Fwww%2Findex.html%3FtradeNo%3D2018081921001004290563773330%26bizType%3DTRADE%26appClearTop%3Dfalse%26startMultApp%3DYES\",\"type\":\"APP\"},\"isAggregatedRec\":false},{\"bizType\":\"TRADE\",\"tagStatus\":0,\"recordType\":\"CONSUME\",\"gmtCreate\":1534659588000,\"consumeStatus\":\"2\",\"oppositeLogo\":\"https://gw.alipayobjects.com/zos/mwalletmng/avkNRQetKoyaSEjnbqvh.png\",\"bizInNo\":\"2018081021001004290525195045_20180819000000022000000072607287\",\"bizSubType\":\"1\",\"createTime\":\"14:19\",\"consumeTitle\":\"退款-一度用车押金\",\"createDesc\":\"08-19\",\"canDelete\":true,\"consumeFee\":\"+399.00\",\"actionParam\":{\"autoJumpUrl\":\"alipays://platformapi/startapp?appId=66666676&appClearTop=false&url=%2Fwww%2Findex.html%3FtradeNo%3D2018081021001004290525195045_20180819000000022000000072607287%26bizType%3DTRADE%26appClearTop%3Dfalse%26startMultApp%3DYES\",\"type\":\"APP\"},\"isAggregatedRec\":false},{\"bizType\":\"TRADE\",\"tagStatus\":0,\"recordType\":\"CONSUME\",\"bizStateDesc\":\"交易关闭\",\"gmtCreate\":1533901606000,\"consumeStatus\":\"3\",\"oppositeLogo\":\"https://t.alipayobjects.com/images/logoimg/TB1rPfhXkRDDuNkUvNmxKNSypXa.jpeg_[pixelWidth]x.jpeg\",\"bizInNo\":\"2018081021001004290525195045\",\"bizSubType\":\"1\",\"createTime\":\"19:46\",\"consumeTitle\":\"一度用车押金\",\"createDesc\":\"08-10\",\"canDelete\":true,\"consumeFee\":\"-399.00\",\"actionParam\":{\"autoJumpUrl\":\"alipays://platformapi/startapp?appId=66666676&appClearTop=false&url=%2Fwww%2Findex.html%3FtradeNo%3D2018081021001004290525195045%26bizType%3DTRADE%26appClearTop%3Dfalse%26startMultApp%3DYES\",\"type\":\"APP\"},\"isAggregatedRec\":false}]},\"balance_data\":{\"yuebaoBaleance\":\"\",\"huangjin\":\"\",\"jijin\":\"\",\"allBalance\":\"0.00\",\"dingqi\":\"\",\"risk_test\":\"保守型\",\"ye\":\"\"}},\"code\":0,\"business_no\":\"84906611-56fc-442d-906b-7df9d7b709f1\",\"remote_id\":\"sxb_xdc3gqb9rla8_3036bf16-0261-45f2-b4b0-a12f4c1d0971\"}";
        SampleResult prev = getSr(str);
        testTimeTool(vars);
//        new QueryDataByBusinessno(vars).postProcessor(prev,"0");
//        new Poll(vars,30).postProcessor(prev,false);
//        new RecordData(vars).writeResult2File();
//        outVars(vars);
    }

    public static void testTimeTool(JMeterVariables vars){
        TimeTool tt = new TimeTool(vars);
        Map<String, Map<String, Long>> ss = tt.calulateTime("C:\\All\\wTools\\apache-jmeter-4.0\\scripts\\real_saas\\result\\2018-10\\2018-10-23");
        System.out.println(ss);
    }

    public static SampleResult getSr(String str){
        SampleResult prev = new SampleResult();
        prev.setResponseData(str.getBytes());
        return prev;
    }

    public static JMeterVariables getVars(){
        JMeterVariables vars = new JMeterVariables();
        vars.put("remote_id","sxb_xdc3gqb9rla8_3036bf16-0261-45f2-b4b0-a12f4c1d0971");
        vars.put("tenant_id","sxb_hqse75ubrnac1111");
        vars.put("record_data_path","C:\\All\\wTools\\apache-jmeter-4.0\\scripts\\real_saas\\result");
        vars.put("line_case","[{\"publish\":{\"event\":\"LOGIN_PWD\",\"content\":\"{\\\"account\\\":\\\"18612707802\\\",\\\"password\\\":\\\"1~2s3d4f5g\\\"}\"},\"poll\":{\"content\":\"COLLECT\",\"event\":\"DEVICE_STATUS\"}}]");
        vars.put("record_data_path","C:\\All\\wTools\\apache-jmeter-4.0\\scripts\\real_saas\\result");
        vars.put("workspace_record_data_path","C:\\All\\wTools\\apache-jmeter-4.0\\scripts\\real_saas\\result1");
        vars.put("is_sample_correct","true");
        vars.put("business_no","84906611-56fc-442d-906b-7df9d7b709f1");
        vars.put("slave_id","863360025904800");
        vars.put("protocol","HTTPS");
        vars.put("query_server_address","query_server_address");
        vars.put("query_server_port","1234");
        vars.put("server","server");
        vars.put("server_port","1234");
        vars.put("master_path","/master_tengxun");
        vars.put("record_data","[]");
        vars.put("simple_record","[]");
        vars.put("poll_loop_count","1");
        vars.put("request_data","request_data");
        vars.put("line_result","{\"address_list\": [],\"person_info\": {\"name\": \"李思维\",\"credentials_no\": \"1****************1\"},\"bad_credit_recoder\": [],\"balance_data\": {\"yuebaoBaleance\": \"\",\"huangjin\": \"\",\"jijin\": \"\",\"allBalance\": \"0.00\",\"dingqi\": \"\",\"risk_test\": \"保守型\",\"ye\": \"\"}}");
        return vars;
    }

    public static void outVars(JMeterVariables vars){
        Iterator<Map.Entry<String, Object>> iterator = vars.getIterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }


    public static void compare(JSONObject json, Object o) {
        Object jo = json.get("is_finish");
        System.out.println(jo.getClass());
//        System.out.println(o.getClass());


        System.out.println(jo.equals(o));
    }

}
