package com.kakarote.admin.service;

import com.kakarote.core.common.BusinessTypeEntity;

import java.util.List;
import java.util.Map;

public interface IAdminCommonService {

    /**
     * 获取所有业务类型
     * @return
     */
    List<BusinessTypeEntity> getAllBusinessType();

    /**
     * 自定义字典项
     * @param params
     * @return
     */
    public List<Map<String,Object>> selectCustomList(Map<String,String> params);
}
