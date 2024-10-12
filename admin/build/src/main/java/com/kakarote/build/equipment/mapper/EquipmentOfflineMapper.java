package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentOfflineBO;
import com.kakarote.build.equipment.entity.PO.EquipmentOffline;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 设备上下线记录表 Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2022-08-02
 */
public interface EquipmentOfflineMapper extends BaseMapper<EquipmentOffline> {

    List<Map> selectOfflineTower();

    List<Map> selectOfflineElevator();

    BasePage<Map<String,Object>> selectPage(BasePage<Object> parse, @Param("data")EquipmentOfflineBO offlineBO);

}
