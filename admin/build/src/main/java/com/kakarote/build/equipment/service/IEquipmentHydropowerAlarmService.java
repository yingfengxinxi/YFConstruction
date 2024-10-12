package com.kakarote.build.equipment.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.BO.EquipmentHydropowerAlarmBO;
import com.kakarote.build.equipment.entity.PO.EquipmentHydropowerAlarm;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 水电报警 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-06-21
 */
public interface IEquipmentHydropowerAlarmService extends BaseService<EquipmentHydropowerAlarm> {

    BasePage<EquipmentHydropowerAlarm> selectByPage(EquipmentHydropowerAlarmBO hydropowerAlarmBO);

    JSONObject excelImport(MultipartFile file);

    BasePage<EquipmentHydropowerAlarm> selectByPageAndPro(EquipmentHydropowerAlarmBO hydropowerAlarmBO);
}
