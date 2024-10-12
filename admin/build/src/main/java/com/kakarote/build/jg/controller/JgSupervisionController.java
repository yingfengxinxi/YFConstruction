package com.kakarote.build.jg.controller;


import com.kakarote.build.jg.entity.BO.JgEnforceRecordBO;
import com.kakarote.build.jg.entity.BO.JgSupervisionBO;
import com.kakarote.build.jg.entity.PO.JgEnforceRecord;
import com.kakarote.build.jg.entity.PO.JgSupervision;
import com.kakarote.build.jg.service.IJgEnforceRecordService;
import com.kakarote.build.jg.service.IJgSupervisionService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 现场安全监督 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/jgSupervision")
public class JgSupervisionController {

    @Autowired
    private IJgSupervisionService iJgSupervisionService;

    @PostMapping("/list")
    @ApiOperation("查询视频监控执法记录列表")
    @OperateLog(detail = "查询视频监控执法记录列表", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<JgSupervision>> list(@RequestBody JgSupervisionBO jgSupervisionBO) {
        return Result.ok(iJgSupervisionService.selectByPage(jgSupervisionBO));
    }

    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation("保存或更新视频监控执法记录")
    @OperateLog(detail = "保存或更新视频监控执法记录", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody JgSupervision jgSupervision) {
        if (jgSupervision.getId() == null) {
            jgSupervision.setCreateBy(String.valueOf(UserUtil.getUserId()));
            jgSupervision.setCreateTime(new Date());
            iJgSupervisionService.save(jgSupervision);
        } else {
            jgSupervision.setUpdateBy(String.valueOf(UserUtil.getUserId()));
            jgSupervision.setUpdateTime(new Date());
            iJgSupervisionService.updateById(jgSupervision);
        }
        return Result.ok();
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("查询视频监控执法记录详情")
    @OperateLog(detail = "查询视频监控执法记录详情", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result getById(@PathVariable Integer id) {
        JgSupervision jgSupervision = iJgSupervisionService.getById(id);
        return Result.ok(jgSupervision);
    }

    @PostMapping("/delete")
    @ApiOperation("删除视频监控执法记录")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(detail = "删除视频监控执法记录", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        return R.ok(iJgSupervisionService.removeByIds(ids));
    }

    /**
     * 删除
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {
        return Result.ok( iJgSupervisionService.removeById(id));
    }

}

