package com.qdoner.datarecept.mapper;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import com.qdoner.datarecept.entity.BO.EquipmentReceptPlatformBO;
import com.qdoner.datarecept.entity.PO.EquipmentReceptPlatform;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 设备数据对接平台 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-11-11
 */
public interface EquipmentReceptPlatformMapper extends BaseMapper<EquipmentReceptPlatform> {
    BasePage<EquipmentReceptPlatform> selectByPage(BasePage<EquipmentReceptPlatform> parse,
                                                   @Param("data") EquipmentReceptPlatformBO inspectionPlanBO);

}
