package com.kakarote.build.wifi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiVideo;
import com.kakarote.build.wifi.entity.PO.WifiVideoWatchRecord;
import com.kakarote.build.wifi.service.IWifiVideoDetailService;
import com.kakarote.build.wifi.service.IWifiVideoService;
import com.kakarote.build.wifi.service.IWifiVideoWatchRecordService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 教育视频观看记录 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
@RestController
@RequestMapping("/wifiVideoWatchRecord")
public class WifiVideoWatchRecordController {
    @Autowired
    private IWifiVideoWatchRecordService iWifiVideoWatchRecordService;
    @Autowired
    private IWifiVideoService iWifiVideoService;

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> list(@RequestBody WifiVideoBO wifiVideoBO) {
        BasePage<Map<String,Object>> list = iWifiVideoWatchRecordService.selectList(wifiVideoBO);
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody WifiVideoWatchRecord wifiVideoWatchRecord) {

        wifiVideoWatchRecord.setVId(wifiVideoWatchRecord.getVideoId());
        wifiVideoWatchRecord.setVName(wifiVideoWatchRecord.getVideoName());

        iWifiVideoWatchRecordService.save(wifiVideoWatchRecord);

        return Result.ok(wifiVideoWatchRecord.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody WifiVideoWatchRecord wifiVideoWatchRecord) {
        boolean b = iWifiVideoWatchRecordService.updateById(wifiVideoWatchRecord);
        return Result.ok(wifiVideoWatchRecord.getId());
    }


    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable String id){

        iWifiVideoWatchRecordService.removeById(id);

        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result<WifiVideoWatchRecord> selectById(@PathVariable String id){

        WifiVideoWatchRecord one = iWifiVideoWatchRecordService.query()
                .eq("id", id)
                .ne("project_id", 0)
                .one();

        return Result.ok(one);
    }
}

