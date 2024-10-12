package com.kakarote.build.equipment.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.BO.EquipmentElectricityRecordBO;
import com.kakarote.build.equipment.entity.PO.EquipmentElectricityRecord;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * <p>
 * 设备用电量 服务类
 * </p>
 *
 * @author lzy
 * @since 2021-06-18
 */
public interface IEquipmentElectricityRecordService extends BaseService<EquipmentElectricityRecord> {

    /**
     * 查询设备用电报表数据
     * @param electricityRecordBO
     * @return
     */
    Map<String, Object> selectTableList(EquipmentElectricityRecordBO electricityRecordBO);

    JSONObject excelImport(MultipartFile file);

    Map<String, Object> selectProTableList(EquipmentElectricityRecordBO electricityRecordBO);
}
