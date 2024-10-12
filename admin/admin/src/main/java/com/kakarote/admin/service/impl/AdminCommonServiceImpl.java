package com.kakarote.admin.service.impl;

import com.kakarote.admin.mapper.AdminDictTypeMapper;
import com.kakarote.admin.service.IAdminCommonService;
import com.kakarote.core.common.BusinessTypeEntity;
import com.kakarote.core.common.BusinessTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminCommonServiceImpl implements IAdminCommonService {
    @Override
    public List<BusinessTypeEntity> getAllBusinessType() {
        List<BusinessTypeEntity> list = new ArrayList<>();
        for (BusinessTypeEnum Enum : BusinessTypeEnum.values()) {
            if(Enum.getType() == -1){
                continue;
            }
            BusinessTypeEntity typeEntity = new BusinessTypeEntity();
            typeEntity.setType(Enum.getType());
            typeEntity.setValue(Enum.getType());
            typeEntity.setName(Enum.getName());
            list.add(typeEntity);
        }
        return list;
    }

    @Autowired
    private AdminDictTypeMapper adminDictTypeMapper;

    @Override
    public List<Map<String,Object>> selectCustomList(Map<String, String> params) {

        List<Map<String,Object>> adminDictTypeList = adminDictTypeMapper.selectDiyList(params);
        return adminDictTypeList;
    }
}
