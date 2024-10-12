package com.kakarote.build.event.controller;


import com.kakarote.build.event.entity.BO.EventInfoBO;
import com.kakarote.build.event.entity.PO.EventInfo;
import com.kakarote.build.event.service.IEventInfoService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.service.ISequenceService;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 环保事件督办——事件表 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-03-07
 */
@RestController
@RequestMapping("/eventInfo")
public class EventInfoController {

    @Autowired
    private IEventInfoService eventInfoService;

    @Autowired
    private ISequenceService sequenceService;

    @PostMapping("/save")
    @ApiOperation("保存事件填报信息")
    @OperateLog(detail = "保存事件填报信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result save(@RequestBody EventInfo eventInfo) {


        Long userId = UserUtil.getUser().getUserId();
        String uuid= UUID.randomUUID().toString().replaceAll("-", "");
        eventInfo.setId(uuid);
        eventInfo.setInformant(userId);
        eventInfo.setFillInTime(new Date());
        eventInfo.setCreateBy(userId);
        eventInfo.setEventStatus("1");

        eventInfoService.save(eventInfo);
        return Result.ok();
    }

    @PostMapping("/selectStatusNumber")
    @ApiOperation("查询事件状态统计数量")
    @OperateLog(detail = "查询事件状态统计数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectStatusNumber() {
        return Result.ok(eventInfoService.selectStatusNumber());
    }

    @PostMapping("/selectByPage")
    @ApiOperation("查询事件列表")
    @OperateLog(detail = "查询事件列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EventInfo>> selectByPage(@RequestBody EventInfoBO eventInfoBO) {
        return Result.ok(eventInfoService.selectByPage(eventInfoBO));
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("查询环保事件详情")
    @OperateLog(detail = "查询环保事件详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable @NotNull String id) {
        Map<String, Object> data = eventInfoService.selectDataById(id);
        return Result.ok(data);
    }

    @PostMapping("/selectEventNumber")
    @ApiOperation("查询近一年的环保事件数量")
    @OperateLog(detail = "查询近一年的环保事件数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectEventNumber() {
        return Result.ok(eventInfoService.selectEventNumber());
    }

    @PostMapping("/selectProEventRanking")
    @ApiOperation("查询环保事件数量排名前八的项目")
    @OperateLog(detail = "查询环保事件数量排名前八的项目", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectProEventRanking() {
        return Result.ok(eventInfoService.selectProEventRanking());
    }

    @PostMapping("/generateEventCode")
    @ApiOperation("生成事件编号")
    @OperateLog(detail = "生成事件编号", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<String> generateEventCode() {
        UserInfo userInfo = UserUtil.getUser();
        return Result.ok(sequenceService.getDateSeqNew("4"));
    }

}

