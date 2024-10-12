package com.kakarote.build.qual.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.qual.entity.BO.QualCheckBO;
import com.kakarote.build.qual.entity.BO.QualHouseTypeMangeBO;
import com.kakarote.build.qual.entity.PO.*;
import com.kakarote.build.qual.service.IQualHouseNameService;
import com.kakarote.build.qual.service.IQualHouseTypeMangeService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.upload.UploadEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 施工质量--分户验收--户型管理 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-06-24
 */
@RestController
@RequestMapping("/qualHouseTypeMange")
public class QualHouseTypeMangeController {
    @Autowired
    private IQualHouseTypeMangeService IQualHouseTypeMangeService;
    @Autowired
    private IQualHouseNameService IQualHouseNameService;
    @Autowired
    AdminFileService adminFileService;

    @PostMapping("/list")
    @ApiOperation("查询户型模板列表")
    @OperateLog(detail = "查询户型模板列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<QualHouseTypeMange>> list(@RequestBody QualHouseTypeMangeBO QualHouseTypeMangeBO) {
        return Result.ok( IQualHouseTypeMangeService.selectByPage(QualHouseTypeMangeBO));
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("获取户型模板详情")
    @OperateLog(detail = "获取户型模板详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Object> getMenuById(@PathVariable String id) {
        return Result.ok(IQualHouseTypeMangeService.getByIdDiy(Integer.valueOf(id)));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存户型模板")
    @OperateLog(detail = "保存户型模板", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdateMessage( @Validated
                                       @RequestParam(value = "ZHUB",required = false) String ZHUB,
                                       @RequestParam(value = "ZIB",required = false) String ZIB,
                                       @RequestParam(value = "ACCEPT",required = false) String ACCEPT,
                                       @RequestParam(value = "QYfile",required = false) MultipartFile QYfile) {
        QualHouseTypeMange qualHouseTypeMange =JSONObject.parseObject(ZHUB, QualHouseTypeMange.class);
        List<QualHouseName> qualHouseName =JSONObject.parseArray(ZIB, QualHouseName.class);
        List<QualHouseName> qualHouseName2 =JSONObject.parseArray(ACCEPT, QualHouseName.class);
        if(QYfile != null){
            Result<UploadEntity> img = adminFileService.upload(QYfile, qualHouseTypeMange.getBatchId(), "img");
            UploadEntity data = img.getData();
            qualHouseTypeMange.setHouseTypeImg(data.getUrl());
        }
        return Result.ok(IQualHouseTypeMangeService.saveOrUpdateDiy(qualHouseTypeMange,qualHouseName,qualHouseName2));
    }

    @GetMapping("/del/{id}")
    @ApiOperation("删除户型模板")
    @OperateLog(detail = "删除户型模板", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result del(@PathVariable String id) {
        String str[] = id.split(",");
        for (String idTemp : str) {

           boolean flag= IQualHouseTypeMangeService.removeById(idTemp);
           if(flag){
               IQualHouseNameService.delByHouseTypeId(Integer.valueOf(idTemp));
           }
        }
        return R.ok(str.length);
    }

    @PostMapping("/CopyHouseType")
    @ApiOperation("复制户型模板")
    @OperateLog(detail = "复制户型模板", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result CopyHouseType(@RequestBody QualHouseTypeMange QualHouseTypeMange) {
        return Result.ok(IQualHouseTypeMangeService.CopyHouseType(QualHouseTypeMange));
    }

    @PostMapping("/wxGetByIdDiy")
    @ApiOperation("获取户型模板详情")
    public Result<Object> wxGetByIdDiy(@RequestBody QualHouseTypeMange QualHouseTypeMange) {
        return Result.ok(IQualHouseTypeMangeService.wxGetByIdDiy(QualHouseTypeMange));
    }
}

