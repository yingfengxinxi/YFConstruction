package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.EquipmentHydropowerAlarmBO;
import com.kakarote.build.equipment.entity.PO.EquipmentHydropowerAlarm;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 水电报警 Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-06-21
 */
public interface EquipmentHydropowerAlarmMapper extends BaseMapper<EquipmentHydropowerAlarm> {

    BasePage<EquipmentHydropowerAlarm> selectByPage(BasePage<Object> parse, @Param("data") EquipmentHydropowerAlarmBO hydropowerAlarmBO);

    int batchSaveData(@Param("list") List<EquipmentHydropowerAlarm> hydropowerAlarmList);

    BasePage<EquipmentHydropowerAlarm> selectByPageAndPro(BasePage<Object> parse, @Param("data")EquipmentHydropowerAlarmBO hydropowerAlarmBO);
}
