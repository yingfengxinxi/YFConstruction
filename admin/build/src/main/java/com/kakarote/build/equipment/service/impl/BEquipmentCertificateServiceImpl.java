package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.BO.BEquipmentCertificateBO;
import com.kakarote.build.equipment.entity.PO.BEquipmentCertificate;
import com.kakarote.build.equipment.mapper.BEquipmentCertificateMapper;
import com.kakarote.build.equipment.service.IBEquipmentCertificateService;
import com.kakarote.build.qual.entity.PO.QualHouseAcceptFillSon;
import com.kakarote.build.wx.entity.BO.BWxFeedbackBO;
import com.kakarote.build.wx.entity.PO.BWxFeedback;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 机械设备证书管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-08-04
 */
@Service
public class BEquipmentCertificateServiceImpl extends BaseServiceImpl<BEquipmentCertificateMapper, BEquipmentCertificate> implements IBEquipmentCertificateService {

    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BEquipmentCertificateBO> selectList(BEquipmentCertificateBO bEquipmentCertificateBO)
    {
        bEquipmentCertificateBO.setOrdersBd("create_time",false);
        BasePage<BEquipmentCertificateBO> adminMessageBasePage = getBaseMapper().selectDataList(bEquipmentCertificateBO.parse(),bEquipmentCertificateBO);

        return adminMessageBasePage;
    }



    public String add(BEquipmentCertificate bEquipmentCertificate){
        bEquipmentCertificate.setCreateTime(new Date());
        bEquipmentCertificate.setCreateBy(UserUtil.getUser().getRealname());
        save(bEquipmentCertificate);
        return"";
    }

    public String edit(BEquipmentCertificate bEquipmentCertificate){
        bEquipmentCertificate.setUpdateTime(new Date());
        bEquipmentCertificate.setUpdateBy(UserUtil.getUser().getRealname());
        this.updateById(bEquipmentCertificate);
        return"";
    }

    @Override
    public Map<String, Object> selectById(String id) {
        BEquipmentCertificate bEquipmentCertificate =this.getById(id);
        Map<String, Object> map = new HashMap<>();
        String acceptBy =bEquipmentCertificate.getCertificateType();
        if(acceptBy != null && !acceptBy.equals("null") && !acceptBy.equals("") ){
            List<String> list = new ArrayList<>();
            String[] split = acceptBy.split(",");
            for(String s :split){
                list.add(s);
            }
            map.put("certificateType",list);
        }


        map.put("id", bEquipmentCertificate.getId());
        map.put("certificateSpecies", bEquipmentCertificate.getCertificateSpecies());
        map.put("equipmentType", bEquipmentCertificate.getEquipmentType());
        return map;
    }
    public List<BEquipmentCertificate> selectByEquipmentType(String equipmentType){
        List<BEquipmentCertificate> bEquipmentCertificate=getBaseMapper().selectByEquipmentType(equipmentType);
        return bEquipmentCertificate;
    }

}
