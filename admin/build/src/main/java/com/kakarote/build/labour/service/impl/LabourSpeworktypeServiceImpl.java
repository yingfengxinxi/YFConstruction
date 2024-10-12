package com.kakarote.build.labour.service.impl;

import com.kakarote.build.equipment.entity.PO.BEquipmentCertificate;
import com.kakarote.build.labour.entity.BO.LabourSpeworktypeBO;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.build.labour.mapper.LabourSpeworktypeMapper;
import com.kakarote.build.labour.service.ILabourSpeworktypeService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 特殊工种--证件管理（分包单位--特殊工种--证件台账） 服务实现类
 * </p>
 *
 * @author zy
 * @since 2021-03-19
 */
@Service
public class LabourSpeworktypeServiceImpl extends BaseServiceImpl<LabourSpeworktypeMapper, LabourSpeworktype> implements ILabourSpeworktypeService {

    @Override
    public BasePage<LabourSpeworktype> selectList(LabourSpeworktypeBO labourSpeworktypeBO) {
        BasePage<LabourSpeworktype> list = getBaseMapper().selectList(labourSpeworktypeBO.parse(), labourSpeworktypeBO);
        return list;
    }

    @Override
    public LabourSpeworktype selectByInfoId(Integer infoId) {
        LabourSpeworktype labourSpeworktype = getBaseMapper().selectByInfoId(infoId);
        return labourSpeworktype;
    }

    @Override
    public Integer deleteByInfoId(Integer infoId) {
        Integer integer = getBaseMapper().deleteByInfoId(infoId);
        return integer;
    }

    @Override
    public BasePage<Map> selectTabsByInfo(LabourSpeworktypeBO labourSpeworktypeBO) {
        return getBaseMapper().selectTabsByInfo(labourSpeworktypeBO.parse(),labourSpeworktypeBO);
    }
    @Override
    public List<LabourSpeworktype> selectByInfocolId(String InfocolId){
        List<LabourSpeworktype> labourSpeworktype=getBaseMapper().selectByInfocolId(InfocolId);
        return labourSpeworktype;
    }

    @Override
    public BasePage<Map> selectTabsByInfoNew(LabourSpeworktypeBO labourSpeworktypeBO) {
        return getBaseMapper().selectTabsByInfoNew(labourSpeworktypeBO.parse(),labourSpeworktypeBO);
    }
}
