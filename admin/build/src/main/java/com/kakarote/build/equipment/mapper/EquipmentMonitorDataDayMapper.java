package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorDataDay;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项日数据表 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-10-28
 */
public interface EquipmentMonitorDataDayMapper extends BaseMapper<EquipmentMonitorDataDay> {

    BasePage<Map<String, Object>> selectListByProject(BasePage<Object> parse, @Param("data") EquipmentMonitorDataBO monitorDataBO);

    List<String> getDayDataByPoint(@Param("pointId") Integer pointId
            , @Param("hoursList") List<String> hoursList);
}
