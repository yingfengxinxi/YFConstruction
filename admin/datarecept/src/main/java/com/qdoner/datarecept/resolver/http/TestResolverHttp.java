package com.qdoner.datarecept.resolver.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;


/**
 * 测试解析：和知云-基坑数据
 */
public class TestResolverHttp{
    public String codeResolver(JSONObject object) {
        //获取设备code
        String eqCode = object.getString("dev_id");
        return eqCode;
    }

    /**
     * {
     * "dev_id": "20190705",
     * "volt": 5.785,
     * "sample_time": "2019-12-03 16:30:47",
     * "datas": [
     * {
     * "upload_code":"20190705#0#1#0" ,
     * "point_name":"C-01",
     * "mon_type": 3,
     * "dev_type": 11,
     * "depth":0.5,
     * "data1":193.36,
     * "data2":-123.36,
     * "data3":3.36,
     * "data4":-8.36,
     * "data1_this":1.265,
     * "data2_this":-4.265,
     * "data3_this":-4.265,
     * "data4_this":-4.265,
     * "data1_total":3.265,
     * "data2_total":-5.321,
     * "data3_total":-5.321,
     * "data4_total":-5.321,
     * "data1_rate":0.265,
     * "data2_rate":-1.265,
     * "data3_rate":-1.265,
     * "data4_rate":-1.265,
     * "alarm_state":1,
     * "warning": [
     * "data1_this",
     * "data1_rate",
     * ],
     * "error": [],
     * "control": []
     * },{..},..
     * ]
     * }
     * <p>
     * 返回
     * {
     * "code": 1,
     * "msg": "成功"
     * }
     *
     * @param object
     * @return
     */
    public JSONObject dataResolver(JSONObject object, JSONObject asset) {
        JSONObject jsonObject = JSONObject.parseObject(object.toString());
        //组合测点数据
        JSONObject resultJson = new JSONObject();
        resultJson.put("asset", asset);
        JSONArray dataArray = new JSONArray();
        String monitorTime = jsonObject.getString("sample_time");
        JSONArray jsonArray = jsonObject.getJSONArray("datas");
        for (Object o : jsonArray) {
            JSONObject one = JSONObject.parseObject(o.toString());
            //传感器编码
            String code = one.getString("upload_code");
            for (Map.Entry entry : one.entrySet()) {
                String key = String.valueOf(entry.getKey());
                if (key.startsWith("data")) {
                    JSONObject data = new JSONObject();
                    data.put("pointCode",code + "-" + key);
                    data.put("value", one.getString(key));
                    data.put("monitorTime", monitorTime);
                    dataArray.add(data);
                }
            }
        }
        resultJson.put("dataList", dataArray);
        return resultJson;
    }
}
