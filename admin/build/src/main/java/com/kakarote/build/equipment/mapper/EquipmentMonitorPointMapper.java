package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorPointBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.build.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测点设置 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
public interface EquipmentMonitorPointMapper extends BaseMapper<EquipmentMonitorPoint> {

    BasePage<EquipmentMonitorPoint> selectByPage(BasePage<EquipmentMonitorPoint> parse, @Param("data") EquipmentMonitorPointBO monitorPointBO);

    BasePage<EquipmentMonitorPoint> listAllProject(BasePage<EquipmentMonitorPoint> parse, @Param("data") EquipmentMonitorPointBO monitorPointBO);

    List<EquipmentMonitorPoint> selectAll();

    Map<String, Object> selectRaiseDustDataByPointCode(@Param("pointCode") String pointCode);

    List<EquipmentMonitorPoint> getMonitorList (String type);

    Map<String,Object> EnvMonitPont(@Param("data") Map<String,Object> map);

    List<Map<String,Object>> EarlyWarningType(Map<String,Object> map);

    @SqlParser(filter = true)
    List<Map<String,Object>> selectPointAndMonitorStatus(@Param("hour") Integer hour);

    List<EquipmentMonitorPoint> getPointListByIds(@Param("list") List<Integer> ids);

    List<EquipmentMonitorPoint> getPointAndAtvalueByAssetId(@Param("assetId") String assetId);

    @SqlParser(filter = true)
    EquipmentMonitorPoint getByIdInApi(Integer id);


    Map<String, Object> selectEnvironmentalData(@Param("data") Map<String, Object> params);
}
