package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataBO;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataEchartsBO;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorPointBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorData;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项历史数据表 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-05-20
 */
public interface EquipmentMonitorDataMapper extends BaseMapper<EquipmentMonitorData> {

    BasePage<Map<String, Object>> selectByPage(BasePage<Map> parse, @Param("data") EquipmentMonitorDataBO monitorDataBO);

    int batchSaveData(@Param("list")List<EquipmentMonitorData> monitorDataList);

    List<Map>selectInfo(@Param("data")Map<String,Object>map);

    @SqlParser(filter=true)
    BasePage<Map<String, Object>> selectAppListByPage(BasePage<Map> parse, @Param("data") EquipmentMonitorDataBO monitorDataBO);


    public EquipmentMonitorData   selectEquipmentMonitorDataOne(String pointId);

    BasePage<Map<String, Object>> selectListByProject(BasePage<Object> parse, @Param("data") EquipmentMonitorDataBO monitorDataBO);

    BasePage<Map<String, Object>> selectMonitorDataList(BasePage<Map> parse, @Param("data") EquipmentMonitorDataBO monitorDataBO);

    List<Map<String, Object>> selectMonitorDataByPointId(@Param("pointId") Integer id, @Param("data") EquipmentMonitorDataEchartsBO monitorDataBO);
	
	List<Map<String, Object>> getTowerCraneData(@Param("equipmentId") Integer id);


    BasePage<LinkedHashMap<String, Object>> getMDataByEq(BasePage<Map> parse, @Param("data") EquipmentMonitorDataBO monitorDataBO);


    BasePage<Map<String, Object>> selectListNew(BasePage<Object> parse, @Param("data") EquipmentMonitorDataBO monitorDataBO);

}
