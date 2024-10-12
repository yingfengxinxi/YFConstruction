package com.kakarote.build.wifi.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.wifi.entity.BO.WifiTestPaperRulesBO;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiTestPaperRules;
import com.kakarote.build.wifi.entity.PO.WifiVideo;
import com.kakarote.build.wifi.entity.PO.WifiVideoDetail;
import com.kakarote.build.wifi.service.IWifiTestPaperRulesDelService;
import com.kakarote.build.wifi.service.IWifiVideoDetailService;
import com.kakarote.build.wifi.service.IWifiVideoService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.service.AdminFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p>
 * 教育视频配置--主表 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
@RestController
@RequestMapping("/wifiVideo")
public class WifiVideoController {
    @Autowired
    private IWifiVideoService iWifiVideoService;
    @Autowired
    private IWifiVideoDetailService iWifiVideoDetailService;


    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> list(@RequestBody WifiVideoBO wifiVideoBO) {
        BasePage<Map<String,Object>> list = iWifiVideoService.selectList(wifiVideoBO);
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody WifiVideo wifiVideo) {
        WifiVideo wifiVideo1 = iWifiVideoService.saveTable(wifiVideo);
        return Result.ok(wifiVideo1.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody WifiVideo wifiVideo) {
        WifiVideo wifiVideo1 = iWifiVideoService.updateTable(wifiVideo);
        return Result.ok(wifiVideo1.getId());
    }


    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable String id){

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("vid",id);
        iWifiVideoDetailService.remove(queryWrapper);

        iWifiVideoService.removeById(id);

        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result<Map> selectById(@PathVariable String id){
        Map map = iWifiVideoService.selectById(id);
        return Result.ok(map);
    }


    @PostMapping("/selectNameList")
    @ApiOperation("查询全部名称")
    @OperateLog(detail = "查询全部名称",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result<List<Map>> selectNameList(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("name","id").ne("project_id",0);
        List<Map> list = iWifiVideoService.getBaseMapper().selectMaps(queryWrapper);

        return Result.ok(list);
    }

}

