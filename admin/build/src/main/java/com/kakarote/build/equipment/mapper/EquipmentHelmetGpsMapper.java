package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentHelmetGpsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentHelmetGps;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 智慧安全帽-定位数据 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
public interface EquipmentHelmetGpsMapper extends BaseMapper<EquipmentHelmetGps> {

    BasePage<EquipmentHelmetGps> selectByPage(BasePage<Object> parse, @Param("data") EquipmentHelmetGpsBO equipmentHelmetGpsBO);

    List<EquipmentHelmetGps> selectActionTrack(@Param("data") EquipmentHelmetGpsBO equipmentHelmetGpsBO);

    Integer deletePid(@Param("helmetId") Integer helmetId);
}
