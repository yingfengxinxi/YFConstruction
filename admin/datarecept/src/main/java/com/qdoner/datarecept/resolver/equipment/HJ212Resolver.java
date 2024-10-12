package com.qdoner.datarecept.resolver.equipment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qdoner.datarecept.resolver.equipment.agreements.HJ212.format.hbt212.core.T212Mapper;
import com.qdoner.datarecept.resolver.equipment.agreements.HJ212.format.hbt212.model.CpData;
import com.qdoner.datarecept.resolver.equipment.agreements.HJ212.format.hbt212.model.Data;
import com.qdoner.datarecept.resolver.equipment.agreements.HJ212.format.hbt212.model.DataFlag;
import com.qdoner.datarecept.utils.Constant;

import java.util.Arrays;

public class HJ212Resolver {
    private T212Mapper t212Mapper = new T212Mapper().enableDefaultParserFeatures().enableDefaultVerifyFeatures();

    /**
     * 获取设备编码
     *
     * @param msg
     * @return
     */
    public String codeResolver(Object msg) {
        try{
            Data data = t212Mapper.readData((String)msg);
            return data.getMn();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 组装数据
     *
     * @param msg
     * @param asset
     * @return
     */
    public JSONObject dataResolver(Object msg, JSONObject asset) throws Exception {
        Data data = t212Mapper.readData((String)msg);
        //组合测点数据
        JSONObject resultJson = new JSONObject();
        resultJson.put("asset", asset);
        JSONArray cp = new JSONArray();
        data.getCp().getPollution().forEach((key, value) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("monitorTime", data.getCp().getDataTime());
            jsonObject.put("pointCode", key);
            jsonObject.put("value", value.getRtd().toString());
            jsonObject.put("flag", value.getFlag());
            cp.add(jsonObject);
        });
        resultJson.put("dataList", cp);
        return resultJson;
    }

    /**
     * 组装响应信息
     *
     * @param msg
     * @return
     */
    public String responseMsg(Object msg) throws Exception {
        Data data = t212Mapper.readData((String)msg);
        String response = "";
        if (DataFlag.A.isMarked(data.getDataFlag())) {
            Data resData = Data.builder().
                    qn(data.getQn())
                    .st(data.getSt())
                    .cn(Constant.DATA_RESPONSE)
                    .pw(data.getPw())
                    .mn(data.getMn())
                    .dataFlag(Arrays.asList(DataFlag.V0))
                    .cp(CpData.builder().build())
                    .build();
            response = t212Mapper.writeDataAsString(resData);
        }
        return response;
    }
}
