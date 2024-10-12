package com.qdoner.datarecept.resolver.equipment;

import com.alibaba.fastjson.JSONObject;
import com.qdoner.datarecept.resolver.equipment.agreements.HJ212.format.hbt212.core.T212Mapper;

/**
 * 测试解析：环境监测设备
 */
public class TestResolverEq {

    private T212Mapper t212Mapper = new T212Mapper().enableDefaultParserFeatures().enableDefaultVerifyFeatures();

    /**
     * 获取设备编码
     * @param data
     * @return
     */
    public String codeResolver(String data) {
        return null;
    }

    /**
     * 组装数据
     * @param data
     * @param asset
     * @return
     */
    public JSONObject dataResolver(String data, JSONObject asset) {
        return null;
    }

    /**
     *  组装响应信息
     * @param data
     * @return
     */
    public String responseMsg(String data){
        return null;
    }
}
