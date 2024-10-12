package com.kakarote.build.equipment.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataBO;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataEchartsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorData;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项历史数据表 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-05-20
 */
public interface IEquipmentMonitorDataService extends BaseService<EquipmentMonitorData> {

    BasePage<Map<String,Object>> selectByPage(EquipmentMonitorDataBO monitorDataBO);

    JSONObject excelImport(MultipartFile file);


    Map<String, Object> selectInfo(Map<String,Object>map);

    BasePage<Map<String,Object>> selectAppListByPage(EquipmentMonitorDataBO monitorDataBO);

    EquipmentMonitorData   selectEquipmentMonitorDataOne(String pointId);

    BasePage<Map<String,Object>> selectListByProject(EquipmentMonitorDataBO monitorDataBO);

    BasePage<Map<String,Object>> selectMonitorDataList(EquipmentMonitorDataBO monitorDataBO);

    List<Map<String,Object>> selectMonitorDataListEcharts(EquipmentMonitorDataEchartsBO monitorDataEchartsBO);

    List<Map<String,Object>> realTimeDataZs(Map<String,Object> params);

    List<Map<String,Object>> realTimeDataTemperature(Map<String,Object> params);

    List<Map<String,Object>> realTimeDataAll(Map<String,Object> params);
	
	List<Map<String,Object>> getTowerCraneData(Integer id);


    Result getMDataByEq(EquipmentMonitorDataBO monitorDataBO);



    BasePage<Map<String,Object>> selectListNew(EquipmentMonitorDataBO monitorDataBO);

}
