package com.kakarote.build.qual.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.qual.entity.BO.QualMonitSTbZhbBO;
import com.kakarote.build.qual.entity.BO.QualMonitsetBO;
import com.kakarote.build.qual.entity.PO.QualMonitJtbZhb;
import com.kakarote.build.qual.service.IQualMonitJtbZbService;
import com.kakarote.build.qual.service.IQualMonitJtbZhbService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.service.AdminFileService;
import com.kakarote.core.servlet.upload.UploadEntity;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 检测检验--监测数据填报--主表 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
@RestController
@RequestMapping("/buildQualMonitJtbZhb")
public class QualMonitJtbZhbController {
    @Autowired
    private IQualMonitJtbZbService iQualMonitJtbZbService;
    @Autowired
    private IQualMonitJtbZhbService iQualMonitJtbZhbService;
    @Autowired
    AdminFileService adminFileService;


    @PostMapping("/list")
    @ApiOperation("查询填报主表信息列表")
    @OperateLog(detail = "查询填报主表信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<QualMonitJtbZhb>> list(@RequestBody QualMonitSTbZhbBO qualMonitSTbZhbBO) {
        BasePage<QualMonitJtbZhb> list = iQualMonitJtbZhbService.selectList(qualMonitSTbZhbBO);
        return Result.ok(list);

    }

    @PostMapping("/queryById/{valveId}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryById(@PathVariable @NotNull Integer valveId){
        return R.ok(iQualMonitJtbZhbService.getById(valveId));
    }


    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或者修改监测项填报主表信息")
    @OperateLog(detail = "新增或者修改监测项填报主表信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result edit(@Validated
                       @RequestParam(value = "ZHUB",required = false) String ZHUB,
                       @RequestParam(value = "ZIB",required = false) String ZIB,
                       @RequestParam(value = "QYfile",required = false) MultipartFile QYfile,
                       @RequestParam(value = "JZfile",required = false) MultipartFile JZfile
    ) {

        QualMonitJtbZhb qualMonitJtbZhb = JSONObject.parseObject(ZHUB, QualMonitJtbZhb.class);
        List<QualMonitsetBO> qualMonitset = JSONObject.parseArray(ZIB,QualMonitsetBO.class);

        if(QYfile != null){
            Result<UploadEntity> img = adminFileService.upload(QYfile, qualMonitJtbZhb.getBatchId(), "img");
            UploadEntity data = img.getData();
            qualMonitJtbZhb.setSamplerImgUrl(data.getUrl());
        }
        if(JZfile != null) {
            Result<UploadEntity> img2 = adminFileService.upload(JZfile, qualMonitJtbZhb.getBatchId(), "img");
            UploadEntity data1 = img2.getData();
            qualMonitJtbZhb.setWitnessImgUrl(data1.getUrl());
        }
        if(qualMonitJtbZhb.getCreateUserName() == null || qualMonitJtbZhb.getCreateUserName().equals("")){
            String username = UserUtil.getUser().getRealname();
            qualMonitJtbZhb.setCreateUserName(username);
        }
        return Result.ok(iQualMonitJtbZhbService.saveOrUpdateTB(qualMonitJtbZhb,qualMonitset));
    }


    @Transactional
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除监测项填报主表信息")
    @OperateLog(detail = "删除监测项填报主表信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            iQualMonitJtbZhbService.removeById(id);
            iQualMonitJtbZbService.deletByZHid(id);
        }

        return Result.ok();
    }

}

