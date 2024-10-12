package com.kakarote.admin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.admin.entity.PO.AdminInstrumentRole;
import com.kakarote.admin.entity.VO.AdminInstrumentRoleVO;
import com.kakarote.admin.mapper.AdminInstrumentRoleMapper;
import com.kakarote.admin.service.IAdminInstrumentRoleService;
import com.kakarote.core.common.Const;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 仪表盘权限表 服务实现类
 * </p>
 *
 * @author author
 * @since 2020-12-08
 */
@Service
public class AdminInstrumentRoleServiceImpl extends BaseServiceImpl<AdminInstrumentRoleMapper, AdminInstrumentRole> implements IAdminInstrumentRoleService {

    @Override
    public JSONObject queryModelSort(Integer roleId) {
        List<AdminInstrumentRoleVO> list = getBaseMapper().queryModelSort(roleId);
        Map<Integer, List<AdminInstrumentRoleVO>> collect = list.stream().
                collect(Collectors.groupingBy(AdminInstrumentRoleVO::getList));
        return new JSONObject().fluentPut("left", collect.get(1)).fluentPut("right", collect.get(2));
    }

    @Override
    public JSONObject queryModelByRoleIds(List<Integer> roleIds) {
        //根据用户角色获取仪表盘权限，如有多个角色则取合集，排序使用最后一次添加的数据
        List<AdminInstrumentRoleVO> list = getBaseMapper().queryModelByRoleIds(roleIds);
        return new JSONObject().fluentPut("list",list);
    }

    @Override
    public Integer getMaxDataType(List<Integer> roleIds) {
        //根据用户角色获取仪表盘权限，如有多个角色则使用显示数量最多的角色设置
        List<AdminInstrumentRole> list =  lambdaQuery().select(AdminInstrumentRole::getDataType).in(AdminInstrumentRole::getRoleId,roleIds)
                .list();
        if(list.size()==0){
            return 1;
        }
        return list.stream().mapToInt(AdminInstrumentRole::getDataType).max().getAsInt();
    }

    /**
     * 设置模块排序
     *
     * @param object obj
     */
    @Override
    public void setModelSort(JSONObject object) {
        List<AdminInstrumentRole> leftList = object.getJSONArray("left").toJavaList(AdminInstrumentRole.class);
        LambdaQueryWrapper<AdminInstrumentRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminInstrumentRole::getRoleId, object.getInteger("roleId"));
        remove(wrapper);
        List<AdminInstrumentRole> sortList = new ArrayList<>();
        for (int i = 0; i < leftList.size(); i++) {
            AdminInstrumentRole instrumentSort = leftList.get(i);
            instrumentSort.setList(1);
            instrumentSort.setSort(i);
            instrumentSort.setRoleId(object.getInteger("roleId"));
            instrumentSort.setDataType(object.getInteger("dataType"));
            sortList.add(instrumentSort);
        }
        List<AdminInstrumentRole> rightList = object.getJSONArray("right").toJavaList(AdminInstrumentRole.class);
        for (int i = 0; i < rightList.size(); i++) {
            AdminInstrumentRole instrumentSort = rightList.get(i);
            instrumentSort.setList(2);
            instrumentSort.setSort(i);
            instrumentSort.setRoleId(object.getInteger("roleId"));
            instrumentSort.setDataType(object.getInteger("dataType"));
            sortList.add(instrumentSort);
        }
        saveBatch(sortList, Const.BATCH_SAVE_SIZE);
    }
}
