package com.kakarote.build.event.controller;


import com.kakarote.build.event.entity.BO.EventItemBO;
import com.kakarote.build.event.entity.PO.EventItem;
import com.kakarote.build.event.service.IEventItemService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 环保事件督办——处理表 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-03-07
 */
@RestController
@RequestMapping("/eventItem")
public class EventItemController {

    @Autowired
    private IEventItemService eventItemService;

    @PostMapping("/save")
    @ApiOperation("保存事件处理信息")
    @OperateLog(detail = "保存事件处理信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result save(@RequestBody EventItem eventItem) {
        eventItemService.saveHandleInfo(eventItem);
        return Result.ok();
    }

    @PostMapping("/eventCheck")
    @ApiOperation("事件审核")
    @OperateLog(detail = "事件审核", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result eventCheck(@RequestBody EventItem eventItem) {
        eventItemService.eventCheck(eventItem);
        return Result.ok();
    }

    @PostMapping("/selectProEventReturn")
    @ApiOperation("查询项目事件退回次数")
    @OperateLog(detail = "查询项目事件退回次数", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> selectProEventReturn(EventItemBO eventItemBO) {
        return Result.ok(eventItemService.selectProEventReturn(eventItemBO));
    }
}

