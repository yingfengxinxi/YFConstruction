package com.qdoner.datarecept.resolver.equipment.tower.resolver;

import com.alibaba.fastjson.JSONObject;

/**
 * 济南拓兴塔机
 */
public class TowerResolver_JNTX {

    /**
     * 获取设备编码
     *
     * @param msg
     * @return
     */
    public String codeResolver(String msg) {
        return "";
    }

    /**
     * 组装数据
     *
     * @param msg
     * @param asset
     * @return
     */
    public JSONObject dataResolver(String msg, JSONObject asset) throws Exception {
        //组合测点数据
        JSONObject resultJson = new JSONObject();
        return resultJson;
    }

    /**
     * 组装响应信息
     *
     * @param msg
     * @return
     */
    public String responseMsg(String msg) throws Exception {
        String response = "";
        return response;
    }
}
