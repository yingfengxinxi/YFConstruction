package com.kakarote.build.wifi.controller;


import com.kakarote.build.wifi.entity.PO.WifiVideoDetail;
import com.kakarote.build.wifi.service.IWifiVideoDetailService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.upload.UploadEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 教育视频配置--明细表 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-06-01
 */
@RestController
@RequestMapping("/wifiVideoDetail")
public class WifiVideoDetailController {
    @Autowired
    private IWifiVideoDetailService iWifiVideoDetailService;
    @Autowired
    private AdminFileService adminFileService;

    @PostMapping("/list/{vid}")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<WifiVideoDetail>> list(@PathVariable String vid) {
        List<WifiVideoDetail> list = iWifiVideoDetailService
                .query()
                .ne("project_id", 0)
                .eq("vid", vid)
                .list();
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody WifiVideoDetail wifiVideoDetail) {
        iWifiVideoDetailService.save(wifiVideoDetail);
        return Result.ok(wifiVideoDetail.getId());
    }



    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody WifiVideoDetail wifiVideoDetail) {
        boolean b = iWifiVideoDetailService.updateById(wifiVideoDetail);
        return Result.ok(wifiVideoDetail.getId());
    }


    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable String id){

        iWifiVideoDetailService.removeById(id);

        return Result.ok();
    }


    @PostMapping("/getOneVideo")
    @ApiOperation("根据工种随机获取一条视频")
    @Transactional
    @OperateLog(detail = "根据工种随机获取一条视频",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result getOneVideo(@Validated @RequestBody Map map){

        Map oneVideoRandom = iWifiVideoDetailService.getOneVideoRandom(map);

        return Result.ok(oneVideoRandom);
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据工种随机获取一条视频")
    @Transactional
    @OperateLog(detail = "根据工种随机获取一条视频",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable String id){

        WifiVideoDetail wifiVideoDetail = iWifiVideoDetailService.getBaseMapper().selectById(id);

        return Result.ok(wifiVideoDetail);
    }

    @PostMapping("/selectByBithIdToFileId/{id}")
    @ApiOperation("根据详情对应id获取对应附件id")
    @Transactional
    @OperateLog(detail = "根据详情对应id获取对应附件id",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectByBithIdToFileId(@PathVariable String id){

        WifiVideoDetail wifiVideoDetail = iWifiVideoDetailService.selectByBithIdToFileId(id);

        return Result.ok(wifiVideoDetail);
    }
    
}

