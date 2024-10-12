package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentPlanAssetBO;
import com.kakarote.build.equipment.entity.PO.EquipmentPlanAsset;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 计划维护设备信息 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface EquipmentPlanAssetMapper extends BaseMapper<EquipmentPlanAsset> {

    BasePage<EquipmentPlanAsset> selectByPage(BasePage<EquipmentPlanAsset> parse, @Param("data") EquipmentPlanAssetBO planAssetBO);
}
