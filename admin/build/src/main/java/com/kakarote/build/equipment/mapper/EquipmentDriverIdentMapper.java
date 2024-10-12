package com.kakarote.build.equipment.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.build.equipment.entity.BO.EquipmentDriverIdentBO;
import com.kakarote.build.equipment.entity.PO.EquipmentDriverIdent;
import com.kakarote.build.safety.entity.BO.SafetyBlockAlarmBO;
import com.kakarote.build.safety.entity.PO.SafetyBlockAlarm;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机械设备司机识别 Mapper 接口
 * </p>
 *
 * @author zy
 * @since 2021-07-26
 */
public interface EquipmentDriverIdentMapper extends BaseMapper<EquipmentDriverIdent> {

    BasePage<Map<String,Object>> selectDriverIdentList(BasePage<EquipmentDriverIdent> page, @Param("data") EquipmentDriverIdentBO equipmentDriverIdentBO);

    BasePage<Map<String, Object>> selectListNew(BasePage<Object> parse, @Param("data") EquipmentDriverIdentBO equipmentDriverIdentBO);

    Map<String,Object> getByIdInApp(Integer id);

    @SqlParser(filter = true)
    int batchSaveData(@Param("list") List<EquipmentDriverIdent> bareSoilDataList);
}
