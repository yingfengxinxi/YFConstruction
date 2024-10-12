package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataBO;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataEchartsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorDataMonth;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项月数据表 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-10-28
 */
public interface EquipmentMonitorDataMonthMapper extends BaseMapper<EquipmentMonitorDataMonth> {

    BasePage<Map<String, Object>> selectMonitorDataListByMonth(BasePage<Object> parse, @Param("data") EquipmentMonitorDataBO monitorDataBO);

    List<Map<String, Object>> selectMonitorDataMonthByPointId(@Param("pointId")Integer id, @Param("data") EquipmentMonitorDataEchartsBO monitorDataEchartsBO);

    BasePage<Map<String, Object>> selectListByProject(BasePage<Object> parse, @Param("data") EquipmentMonitorDataBO monitorDataBO);

    List<Map<String, Object>> selectRecentData(@Param("data") Map<String, Object> map);

    //2022-06-20 日 环境历史监测新增
    List<Map<String, Object>> airQualityMouth(Map<String, Object> map);

    //2022-06-20 日 环境历史监测新增
    List<String> pointIdList(@Param("data") Map<String, Object> map);

    //2022-06-23 日 环境历史监测新增
    List<Map<String, Object>> dataListLineTable(Map<String, Object> map);

    //2022-06-24 日 环境历史监测年度新增
    @SqlParser(filter = true)
    List<Map<String, Object>> yearDataStatistics(@Param("data")Map<String, Object> map);

    //2022-06-24 日 环境历史监测年度新增
    List<Map<String, Object>> standardList(@Param("data")Map<String, Object> map);
}
