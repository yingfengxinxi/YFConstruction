package com.qdoner.message.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.utils.TagUtil;
import com.qdoner.message.entity.BO.QueuesBO;
import com.qdoner.message.entity.PO.Queues;
import com.qdoner.message.mapper.QueuesMapper;
import com.qdoner.message.service.IQueuesService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-04-14
 */
@Service
public class QueuesServiceImpl extends BaseServiceImpl<QueuesMapper, Queues> implements IQueuesService {

    @Autowired
    private AdminService adminService;

    @Override
    public Queues queryByCode(String code) {
        List<Queues> list = this.getBaseMapper().queryByCode(code);
        if(list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Queues> queryList() {
        return this.list();
    }

    @Override
    public BasePage<Queues> queryPageList(QueuesBO queuesBO) {
        return this.getBaseMapper().selectByPage(queuesBO.parse(), queuesBO);
    }

    @Override
    public BasePage<Queues> newQueryPageList(QueuesBO queuesBO) {
        queuesBO.setTargetConfigType("1");
        BasePage<Queues> basePage = this.getBaseMapper().selectByPage(queuesBO.parse(), queuesBO);

        /*List<Queues> list = basePage.getList();
        for (Queues queues: list) {
            List<SimpleDept> deptList = adminService.queryDeptByIds(TagUtil.toSet(queues.getDeptIds())).getData();
            queues.setDeptList(deptList);
            List<SimpleUser> userList = adminService.queryUserByIds(TagUtil.toLongSet(queues.getOwnerUserIds())).getData();
            queues.setOwnerUserList(userList);
        }*/

        return basePage;
    }

    @Override
    public List<Long> getQueuesUserList(String code) {
        // 根据code查询消息类型
        LambdaQueryWrapper<Queues> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Queues::getCode, code);
        queryWrapper.last("limit 1");
        Queues queues =  getBaseMapper().selectOne(queryWrapper);

        // 查询推送用户
        List<Long> ids = new ArrayList<>();
        ids.addAll(StrUtil.splitTrim(queues.getOwnerUserIds(), Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList()));
        List<Integer> deptIds = StrUtil.splitTrim(queues.getDeptIds(), Const.SEPARATOR).stream().map(Integer::valueOf).collect(Collectors.toList());
        if (deptIds.size() > 0) {
            ids.addAll(adminService.queryUserByDeptIds(deptIds).getData());
        }
        return ids;
    }

    @Override
    public List<Long> getQueuesUserList(String code, Integer tenantId) {
        // 根据code查询消息类型
        LambdaQueryWrapper<Queues> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Queues::getCode, code);
        queryWrapper.last("limit 1");
        Queues queues =  getBaseMapper().selectOne(queryWrapper);

        // 查询推送用户
        List<Long> ids = new ArrayList<>();
        ids.addAll(StrUtil.splitTrim(queues.getOwnerUserIds(), Const.SEPARATOR).stream().map(Long::valueOf).collect(Collectors.toList()));
        List<Integer> deptIds = StrUtil.splitTrim(queues.getDeptIds(), Const.SEPARATOR).stream().map(Integer::valueOf).collect(Collectors.toList());
        if (deptIds.size() > 0) {
            ids.addAll(adminService.queryUserByDeptIds(deptIds).getData());
        }
        return ids;
    }
}
