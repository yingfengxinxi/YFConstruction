package com.kakarote.build.labour.service;

import com.kakarote.build.equipment.entity.PO.BEquipmentCertificate;
import com.kakarote.build.labour.entity.BO.LabourSpeworktypeBO;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 特殊工种--证件管理（分包单位--特殊工种--证件台账） 服务类
 * </p>
 *
 * @author zy
 * @since 2021-03-19
 */
public interface ILabourSpeworktypeService extends BaseService<LabourSpeworktype> {

    public BasePage<LabourSpeworktype> selectList(LabourSpeworktypeBO labourSpeworktypeBO);

    /**
     * 根据采集信息主键查询数据
     * @param infoId
     * @return
     */
    LabourSpeworktype selectByInfoId(Integer infoId);

    Integer deleteByInfoId(Integer infoId);

    public BasePage<Map> selectTabsByInfo(LabourSpeworktypeBO labourSpeworktypeBO);

    public List<LabourSpeworktype> selectByInfocolId(String InfocolId);

    BasePage<Map> selectTabsByInfoNew(LabourSpeworktypeBO labourSpeworktypeBO);
}
