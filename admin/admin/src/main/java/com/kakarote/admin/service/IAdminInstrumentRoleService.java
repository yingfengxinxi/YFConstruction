package com.kakarote.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.entity.PO.AdminInstrumentRole;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 仪表盘权限表 服务类
 * </p>
 *
 * @author author
 * @since 2020-12-08
 */
public interface IAdminInstrumentRoleService extends BaseService<AdminInstrumentRole> {

    public JSONObject queryModelSort(Integer roleId);
    public void setModelSort(JSONObject object);
    public JSONObject queryModelByRoleIds(List<Integer> roleIds);
    public Integer getMaxDataType(List<Integer> roleIds);
}
