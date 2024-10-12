package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentRebarSpotCheckBO;
import com.kakarote.build.equipment.entity.PO.EquipmentRebarSpotCheck;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 钢筋智能点检功能开发 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
public interface EquipmentRebarSpotCheckMapper extends BaseMapper<EquipmentRebarSpotCheck> {

    BasePage<EquipmentRebarSpotCheck> selectByPage(BasePage<Object> parse, @Param("data") EquipmentRebarSpotCheckBO equipmentRebarSpotCheckBO);

    BasePage<EquipmentRebarSpotCheck> selectProList(BasePage<Object> parse, @Param("data") EquipmentRebarSpotCheckBO equipmentRebarSpotCheckBO);
}
