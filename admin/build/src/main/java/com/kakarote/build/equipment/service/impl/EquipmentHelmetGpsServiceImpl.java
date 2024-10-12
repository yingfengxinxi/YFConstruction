package com.kakarote.build.equipment.service.impl;

import com.kakarote.build.equipment.entity.BO.EquipmentHelmetGpsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentHelmetGps;
import com.kakarote.build.equipment.mapper.EquipmentHelmetGpsMapper;
import com.kakarote.build.equipment.service.IEquipmentHelmetGpsService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 智慧安全帽-定位数据 服务实现类
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
@Service
public class EquipmentHelmetGpsServiceImpl extends BaseServiceImpl<EquipmentHelmetGpsMapper, EquipmentHelmetGps> implements IEquipmentHelmetGpsService {

    @Override
    public BasePage<EquipmentHelmetGps> selectByPage(EquipmentHelmetGpsBO equipmentHelmetGpsBO) {
        return getBaseMapper().selectByPage(equipmentHelmetGpsBO.parse(), equipmentHelmetGpsBO);
    }

    @Override
    public List selectActionTrack(EquipmentHelmetGpsBO equipmentHelmetGpsBO) {
        List<EquipmentHelmetGps>  list = getBaseMapper().selectActionTrack(equipmentHelmetGpsBO);
        List res  = new ArrayList();
        for(EquipmentHelmetGps helmetGps:list){
            if(helmetGps != null){
                List<Double> dian = new ArrayList<>();
                dian.add(helmetGps.getXAxis());
                dian.add(helmetGps.getYAxis());
                res.add(dian);
            }
        }
        return res;
    }

    @Override
    public String deletePid(Integer helmetId) {
        Integer k= getBaseMapper().deletePid(helmetId);
        return "";
    }
}
