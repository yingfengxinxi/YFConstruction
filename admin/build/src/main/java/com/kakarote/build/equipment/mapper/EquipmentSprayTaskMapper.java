package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentSprayTaskBO;
import com.kakarote.build.equipment.entity.PO.EquipmentSprayTask;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 喷淋定期任务表 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-08-05
 */
public interface EquipmentSprayTaskMapper extends BaseMapper<EquipmentSprayTask> {

    BasePage<EquipmentSprayTask> selectByPage(BasePage<Object> parse, @Param("data") EquipmentSprayTaskBO sprayTaskBO);
}
