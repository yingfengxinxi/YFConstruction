package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentHelmetBO;
import com.kakarote.build.equipment.entity.PO.EquipmentHelmet;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 智能安全帽 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
public interface EquipmentHelmetMapper extends BaseMapper<EquipmentHelmet> {

    BasePage<EquipmentHelmet> selectByPage(BasePage<Object> parse, @Param("data") EquipmentHelmetBO equipmentHelmetBO);

    BasePage<EquipmentHelmet> selectProList(BasePage<Object> parse, @Param("data") EquipmentHelmetBO equipmentHelmetBO);

    List<Map<String, Object>> selectLatestLocationList();

    List<Map<String, Object>> selectPeopleTrackList(@Param("data") Map<String, Object> params);
}
