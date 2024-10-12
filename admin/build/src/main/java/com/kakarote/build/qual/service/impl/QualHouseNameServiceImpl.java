package com.kakarote.build.qual.service.impl;

import com.kakarote.build.qual.entity.PO.QualHouseName;
import com.kakarote.build.qual.mapper.QualHouseNameMapper;
import com.kakarote.build.qual.service.IQualHouseNameService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--户型管理--子表--房间名称 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-06-24
 */
@Service
public class QualHouseNameServiceImpl extends BaseServiceImpl<QualHouseNameMapper, QualHouseName> implements IQualHouseNameService {
    /**
     * 户型复制使用
     * @param id
     * @return
     */
    @Override
    public List<QualHouseName> getByHouseTypeId(Integer id) {
        List<QualHouseName> list=getBaseMapper().getByHouseTypeId(id);
        return list;
    }
    @Override
    public Map<String,Object> getByHouseTypeId2(Integer id) {
        List<QualHouseName> list=getBaseMapper().getByHouseTypeId(id);
        List<QualHouseName> house=new ArrayList<>();
        List<QualHouseName> accept=new ArrayList<>();
        for(QualHouseName QHN:list){
            //1房间，2工序与模板对应关系
            if("1".equals(QHN.getType())){
                house.add(QHN);
            }else{
                accept.add(QHN);
            }
        }
        Map<String,Object> map=new HashMap<>();
        map.put("house",house);
        map.put("accept",accept);
        return map;
    }

    @Override
    public boolean delByHouseTypeId(Integer id) {
        return getBaseMapper().delByHouseTypeId(id);
    }

    /**
     * 据户型id和验收工序查询模板信息--分户验收填报使用
     * @param map
     * @return
     */
    @Override
    public Map getInfoByMap(Map map) {
        return getBaseMapper().getInfoByMap(map);
    }

}
