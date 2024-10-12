package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentPlanBO;
import com.kakarote.build.equipment.entity.PO.EquipmentPlan;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备监测-预防性维护计划-维护计划设置 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface EquipmentPlanMapper extends BaseMapper<EquipmentPlan> {

    BasePage<EquipmentPlan> selectByPage(BasePage<EquipmentPlan> parse, @Param("data") EquipmentPlanBO equipmentPlanBO);

    int batchUpdatePlan(@Param("list") List<EquipmentPlan> plans);
}
