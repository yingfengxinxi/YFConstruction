package com.kakarote.build.qual.controller;


import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.qual.entity.BO.QualCheckPersonnelBO;
import com.kakarote.build.qual.entity.BO.QualPlanBO;
import com.kakarote.build.qual.entity.PO.QualCheckPersonnel;
import com.kakarote.build.qual.entity.PO.QualHouseName;
import com.kakarote.build.qual.entity.PO.QualHouseTypeMange;
import com.kakarote.build.qual.entity.PO.QualPlan;
import com.kakarote.build.qual.service.IQualCheckPersonnelService;
import com.kakarote.build.qual.service.IQualPlanService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.upload.UploadEntity;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 验收人员管理 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-07-08
 */
@RestController
@RequestMapping("/qualCheckPersonnel")
public class QualCheckPersonnelController {

    @Autowired
    private IQualCheckPersonnelService QualCheckPersonnelService;
    @Autowired
    AdminFileService adminFileService;
    @PostMapping("/list")
    @ApiOperation("查询验收人员列表")
    @OperateLog(detail = "查询验收人员列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result list(@RequestBody QualCheckPersonnelBO qualCheckPersonnelBO) {
        return Result.ok(QualCheckPersonnelService.selectByPage(qualCheckPersonnelBO));
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("查询验收人员详情")
    @OperateLog(detail = "删除单位工程划分", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable String id) {
        return Result.ok(QualCheckPersonnelService.getById(id));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或修改验收人员")
    @OperateLog(detail = "新增或修改验收人员", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@Validated @RequestParam(value = "ZHUB",required = false) String ZHUB,
                               @RequestParam(value = "QYfile",required = false) MultipartFile QYfile) {
        QualCheckPersonnel qualCheckPersonnel =JSONObject.parseObject(ZHUB, QualCheckPersonnel.class);
        if(QYfile != null){
            Result<UploadEntity> img = adminFileService.upload(QYfile, qualCheckPersonnel.getBatchId(), "img");
            UploadEntity data = img.getData();
            qualCheckPersonnel.setPersonPhoto(data.getUrl());
        }
        if (qualCheckPersonnel.getId() == null) {
            qualCheckPersonnel.setCreateBy(UserUtil.getUserId());
            qualCheckPersonnel.setCreateTime(new Date());
            QualCheckPersonnelService.save(qualCheckPersonnel);
        }else{
            qualCheckPersonnel.setUpdateBy(UserUtil.getUserId());
            qualCheckPersonnel.setUpdateTime(new Date());
            QualCheckPersonnelService.updateById(qualCheckPersonnel);
        }
        return Result.ok();
    }

    @PostMapping("/delTrueByIds")
    @ApiOperation("删除验收人员")
    @OperateLog(detail = "删除验收人员", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            QualCheckPersonnelService.removeById(id);
        }
        return Result.ok();
    }

    @PostMapping("/getIdAndNameList")
    @ApiOperation("查询验收人员列表--验收人员下来选择使用")
    @OperateLog(detail = "查询验收人员列表--验收人员下来选择使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getIdAndNameList() {
        List<Map> list = QualCheckPersonnelService.selectMap();
        return Result.ok(list);
    }

    @PostMapping("/wxlist")
    @ApiOperation("微信查询所有验收人员")
    public Result wxlist(@RequestBody QualCheckPersonnelBO qualCheckPersonnelBO) {
        return Result.ok(QualCheckPersonnelService.selectWxList(qualCheckPersonnelBO));
    }
}

