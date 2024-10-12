package com.kakarote.build.equipment.mapper;

import com.kakarote.build.equipment.entity.BO.BEquipmentCertificateBO;
import com.kakarote.build.equipment.entity.PO.BEquipmentCertificate;
import com.kakarote.build.wx.entity.BO.BWxFeedbackBO;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 机械设备证书管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2021-08-04
 */
public interface BEquipmentCertificateMapper extends BaseMapper<BEquipmentCertificate> {

    BasePage<BEquipmentCertificateBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") BEquipmentCertificateBO bEquipmentCertificateBO);
    public List<BEquipmentCertificate> selectByEquipmentType(String equipmentType);
}
