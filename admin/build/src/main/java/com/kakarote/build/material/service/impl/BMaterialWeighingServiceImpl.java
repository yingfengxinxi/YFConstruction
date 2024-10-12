package com.kakarote.build.material.service.impl;

import com.kakarote.build.equipment.entity.BO.BEquipmentCertificateBO;
import com.kakarote.build.equipment.entity.PO.BEquipmentCertificate;
import com.kakarote.build.material.entity.BO.BMaterialWeighingBO;
import com.kakarote.build.material.entity.PO.BMaterialWeighing;
import com.kakarote.build.material.mapper.BMaterialWeighingMapper;
import com.kakarote.build.material.service.IBMaterialWeighingService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 物料称重 服务实现类
 * </p>
 *
 * @author author
 * @since 2021-08-11
 */
@Service
public class BMaterialWeighingServiceImpl extends BaseServiceImpl<BMaterialWeighingMapper, BMaterialWeighing> implements IBMaterialWeighingService {


    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<BMaterialWeighingBO> selectList(BMaterialWeighingBO bMaterialWeighingBO)
    {
        bMaterialWeighingBO.setOrdersBd("create_time",false);
        BasePage<BMaterialWeighingBO> adminMessageBasePage = getBaseMapper().selectDataList(bMaterialWeighingBO.parse(),bMaterialWeighingBO);

        return adminMessageBasePage;
    }



    public String add(BMaterialWeighing bMaterialWeighing){
        bMaterialWeighing.setCreateTime(new Date());
        bMaterialWeighing.setCreateBy(UserUtil.getUser().getRealname());
        save(bMaterialWeighing);
        return"";
    }

    public String edit(BMaterialWeighing bMaterialWeighing){
        bMaterialWeighing.setUpdateTime(new Date());
        bMaterialWeighing.setUpdateBy(UserUtil.getUser().getRealname());
        this.updateById(bMaterialWeighing);
        return"";
    }
}
