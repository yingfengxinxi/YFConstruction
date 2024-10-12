package com.qdoner.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.qdoner.message.entity.PO.AssignUser;
import com.qdoner.message.service.IAssignUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/msgAssignUser")
public class AssignUserController {

    @Autowired
    private IAssignUserService assignUserService;

    /**
     * 新增/修改
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/saveOrUpdate")
    @ApiOperation("维护推送配置")
    @OperateLog(detail = "维护推送配置", types = LogTypes.OTHER, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody AssignUser assignUser) {
        //处理人员及部门
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code", assignUser.getCode());
        assignUserService.remove(queryWrapper);
        //为全部事，直接保存
        List<AssignUser> assignUsers = new ArrayList<>();
        if (assignUser.getTargetType().equals("0")) {
            assignUser.setDataId("");
            assignUser.setName("");
            assignUser.setType("");
            assignUsers.add(assignUser);
        } else {
            List<SimpleDept> depts = assignUser.getDeptList();
            depts.forEach(dept -> {
                AssignUser user = new AssignUser();
                user.setDataId(dept.getId().toString());
                user.setName(dept.getName());
                user.setCode(assignUser.getCode());
                user.setType("10");
                user.setTargetType(assignUser.getTargetType());
                assignUsers.add(user);
            });
            List<SimpleUser> users = assignUser.getUserList();
            users.forEach(u -> {
                AssignUser user = new AssignUser();
                user.setDataId(u.getUserId().toString());
                user.setName(u.getRealname());
                user.setCode(assignUser.getCode());
                user.setType("20");
                user.setTargetType(assignUser.getTargetType());
                assignUsers.add(user);
            });
        }
        if (assignUsers.size() > 0) {
            assignUserService.saveOrUpdateBatch(assignUsers);
        }
        return Result.ok();
    }

    /**
     * 获取详情
     */
    @PostMapping("/getByCode/{code}")
    @ApiOperation("查询消息详情")
    @OperateLog(detail = "查询消息详情", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable String code) {
        List<AssignUser> assignUsers = assignUserService.lambdaQuery().eq(AssignUser::getCode, code).list();
        AssignUser assignUser = new AssignUser();
        //默认为（全部）
        if (assignUsers.size() > 0) {
            assignUser = assignUsers.get(0);
        }
        List<SimpleDept> depts = new ArrayList<>();
        List<SimpleUser> users = new ArrayList<>();
        assignUsers.forEach(u -> {
            if (u.getTargetType().equals("1")) {//只考虑指定
                if (u.getType().equals("10")) {//部门
                    SimpleDept dept = new SimpleDept();
                    dept.setId(Integer.valueOf(u.getDataId()));
                    dept.setName(u.getName());
                    depts.add(dept);
                }
                if (u.getType().equals("20")) {//人员
                    SimpleUser user = new SimpleUser();
                    user.setRealname(u.getName());
                    user.setUserId(Long.parseLong(u.getDataId()));
                    users.add(user);
                }
            }
        });
        assignUser.setDeptList(depts);
        assignUser.setUserList(users);
        return Result.ok(assignUser);
    }
}
