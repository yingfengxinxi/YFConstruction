package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.PO.EquipmentMonitorDataAt;
import com.kakarote.build.equipment.mapper.EquipmentMonitorDataAtMapper;
import com.kakarote.build.equipment.service.IEquipmentMonitorDataAtService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项实时数据表 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-11-23
 */
@Service
public class EquipmentMonitorDataAtServiceImpl extends BaseServiceImpl<EquipmentMonitorDataAtMapper, EquipmentMonitorDataAt> implements IEquipmentMonitorDataAtService {

    @Override
    public List<Map<String, Object>> listByPro(Integer proId) {
        return this.getBaseMapper().listByPro(proId);
    }

    @Override
    public Map<String, Object> selectEnvDataAt() {
        List<Map<String, Object>> list = getBaseMapper().selectEnvDataAtList();

        Map<String, Object> result = new HashMap<>();

        for (Map<String, Object> map:list) {
            Map<String, Object> obj = new HashMap<>();
            if ("4".equals(map.get("monitorSonType"))) { //温度
                obj.put("value", map.get("value"));
                obj.put("unit", map.get("unit"));

                result.put("temperature", obj);
            } else if ("8".equals(map.get("monitorSonType"))) { // 风力
                obj.put("value", map.get("value"));
                obj.put("unit", map.get("unit"));

                result.put("windSpeed", obj);
            } else if ("3".equals(map.get("monitorSonType"))) { // 噪音
                obj.put("value", map.get("value"));
                obj.put("unit", map.get("unit"));

                result.put("noise", obj);
            } else if ("1".equals(map.get("monitorSonType"))) { // pm25
                obj.put("value", map.get("value"));
                obj.put("unit", map.get("unit"));
                obj.put("alarmUpper", map.get("alarmUpper"));

                result.put("pm25", obj);
            } else if ("2".equals(map.get("monitorSonType"))) { // pm10
                obj.put("value", map.get("value"));
                obj.put("unit", map.get("unit"));
                obj.put("alarmUpper", map.get("alarmUpper"));

                result.put("pm10", obj);
            } else if ("5".equals(map.get("monitorSonType"))) { // 湿度
                obj.put("value", map.get("value"));
                obj.put("unit", map.get("unit"));

                result.put("humidity", obj);
            }
        }

        if (result.get("temperature") == null) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("value", 0);
            obj.put("unit", "°C");
            result.put("temperature", obj);
        }
        if (result.get("windSpeed") == null) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("value", 0);
            obj.put("unit", "m/s");
            result.put("windSpeed", obj);
        }
        if (result.get("noise") == null) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("value", 0);
            obj.put("unit", "dB");
            result.put("noise", obj);
        }
        if (result.get("pm25") == null) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("value", 0);
            obj.put("unit", "μg/m³");
            result.put("pm25", obj);
        }
        if (result.get("pm10") == null) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("value", 0);
            obj.put("unit", "μg/m³");
            result.put("pm10", obj);
        }
        if (result.get("humidity") == null) {
            Map<String, Object> obj = new HashMap<>();
            obj.put("value", 0);
            obj.put("unit", "%rh");
            result.put("humidity", obj);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> listByEquipment(Integer equipmentId) {
        return this.getBaseMapper().listByEquipment(equipmentId);
    }
}
