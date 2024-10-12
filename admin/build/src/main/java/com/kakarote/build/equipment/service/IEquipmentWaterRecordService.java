package com.kakarote.build.equipment.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.BO.EquipmentWaterRecordBO;
import com.kakarote.build.equipment.entity.PO.EquipmentWaterRecord;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzy
 * @since 2021-06-21
 */
public interface IEquipmentWaterRecordService extends BaseService<EquipmentWaterRecord> {

    /**
     * 查询设备用水报表数据
     * @param waterRecordBO
     * @return
     */
    Map<String, Object> selectTableList(EquipmentWaterRecordBO waterRecordBO);

    JSONObject excelImport(MultipartFile file);

    Map<String, Object> selectProTableList(EquipmentWaterRecordBO waterRecordBO);
}
