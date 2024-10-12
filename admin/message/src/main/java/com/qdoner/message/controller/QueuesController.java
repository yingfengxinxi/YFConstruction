package com.qdoner.message.controller;


import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.admin.entity.SimpleDept;
import com.kakarote.core.feign.admin.entity.SimpleUser;
import com.kakarote.core.feign.admin.service.AdminService;
import com.kakarote.core.utils.TagUtil;
import com.kakarote.core.utils.UserUtil;
import com.qdoner.message.common.MqManageService;
import com.qdoner.message.entity.BO.QueuesBO;
import com.qdoner.message.entity.PO.AssignUser;
import com.qdoner.message.entity.PO.Queues;
import com.qdoner.message.service.IAssignUserService;
import com.qdoner.message.service.IQueuesService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/msgQueues")
public class QueuesController {

    @Autowired
    private IQueuesService queuesService;

    @Autowired
    private MqManageService mqManageService;

    @Autowired
    private AdminService adminService;

    /**
     * 分页列表
     */
    @PostMapping("/list")
    @ApiOperation("查询消息列表")
    @OperateLog(detail = "查询消息列表", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public Result list(@RequestBody QueuesBO queuesBO) {
        return Result.ok(queuesService.queryPageList(queuesBO));
    }
    /**
     * 分页列表
     */
    @PostMapping("/listAll")
    @ApiOperation("查询消息列表")
    @OperateLog(detail = "查询消息列表", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public Result list() {
        List<Queues> list = queuesService.queryList();
        return Result.ok(list);
    }
    /**
     * 分页列表
     */
    @PostMapping("/newList")
    @ApiOperation("查询消息列表")
    @OperateLog(detail = "查询消息列表", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public Result newList(@RequestBody QueuesBO queuesBO) {
        return Result.ok(queuesService.newQueryPageList(queuesBO));
    }

    /**
     * 获取详情
     */
    @PostMapping("/getById/{id}")
    @ApiOperation("查询消息详情")
    @OperateLog(detail = "查询消息详情", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable String id) {
        Queues queues = queuesService.getById(id);
        /*List<SimpleDept> deptList = adminService.queryDeptByIds(TagUtil.toSet(queues.getDeptIds())).getData();
        queues.setDeptList(deptList);
        List<SimpleUser> userList = adminService.queryUserByIds(TagUtil.toLongSet(queues.getOwnerUserIds())).getData();
        queues.setOwnerUserList(userList);*/
        return Result.ok(queues);
    }

    /**
     * 新增/修改
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation("维护消息")
    @OperateLog(detail = "维护消息", types = LogTypes.OTHER, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody Queues queues) {
        //赋值 mq通道标识
        queues.setChannelName(queues.getCode());
        if (queues.getId() == null) {
            queues.setId(IdUtil.randomUUID());
            queues.setCreateUserId(Integer.parseInt(UserUtil.getUserId().toString()));
            queuesService.save(queues);
        } else {
            queuesService.updateById(queues);
        }
        return Result.ok();
    }

    /**
     * 物理删除
     */
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除消息")
    @OperateLog(detail = "删除消息", types = LogTypes.OTHER, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            Queues queues = queuesService.getById(id);
            mqManageService.deleteQueue(queues.getCode());
            queuesService.removeById(id);
        }
        return Result.ok();
    }

    /**
     * 部署消息
     */
    @PostMapping("/deployExchange/{id}")
    @ApiOperation("部署消息")
    @OperateLog(detail = "部署消息", types = LogTypes.OTHER, behavior = LogBehavior.OTHER)
    public Result deployExchange(@PathVariable String id) {
        Queues queues = queuesService.getById(id);
        if ("0".equals(queues.getIfSendMq()) && "0".equals(queues.getIfSendWeb())) {
            return Result.error(-1, "未启用任何通道，无需部署");
        }
        if ("1".equals(queues.getIfSendMq())) {
            mqManageService.deployQueue(queues);
        }
        queues.setDeployTime(new Date());
        queuesService.updateById(queues);
        return Result.ok();
    }

   /**
    * 根据message类型查询推送人员列表*
    */
    @PostMapping("/getQueuesUserList/{code}")
    @ApiOperation("查询消息列表")
    @OperateLog(detail = "查询消息列表", types = LogTypes.OTHER, behavior = LogBehavior.SELECT)
    public List<Long> getQueuesUserList(@PathVariable String code) {
        return queuesService.getQueuesUserList(code);
    }

    /**
     * 根据message类型查询推送人员列表,需传递租户id的调用
     */
    @PostMapping("/getQueuesUserList/{code}/{tenantId}")
    @ApiOperation("查询消息列表")
    public List<Long> getQueuesUserList(@PathVariable String code, @PathVariable Integer tenantId) {
        return queuesService.getQueuesUserList(code, tenantId);
    }
}

