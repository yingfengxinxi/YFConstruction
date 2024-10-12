package com.kakarote.build.equipment.service;

import com.kakarote.build.equipment.entity.BO.BEquipmentCertificateBO;
import com.kakarote.build.equipment.entity.PO.BEquipmentCertificate;
import com.kakarote.build.wx.entity.BO.BWxFeedbackBO;
import com.kakarote.build.wx.entity.PO.BWxFeedback;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机械设备证书管理 服务类
 * </p>
 *
 * @author author
 * @since 2021-08-04
 */
public interface IBEquipmentCertificateService extends BaseService<BEquipmentCertificate> {


    /**
     * 查询数据列表
     *
     */
    public BasePage<BEquipmentCertificateBO> selectList(BEquipmentCertificateBO bEquipmentCertificateBO);

    public String add(BEquipmentCertificate bEquipmentCertificate);

    public String edit(BEquipmentCertificate bEquipmentCertificate);

    Map<String, Object> selectById(String id);

    public List<BEquipmentCertificate> selectByEquipmentType(String equipmentType);
}
