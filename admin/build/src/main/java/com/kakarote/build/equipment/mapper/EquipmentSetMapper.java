package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentSetBO;
import com.kakarote.build.equipment.entity.PO.EquipmentSet;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-05-24
 */
public interface EquipmentSetMapper extends BaseMapper<EquipmentSet> {

    BasePage<EquipmentSet> selectByPage(BasePage<EquipmentSet> parse, @Param("data") EquipmentSetBO setBO);

    BasePage<EquipmentSet> selectListByAssetId(BasePage<EquipmentSet> parse, @Param("data") EquipmentSetBO setBO);
}
