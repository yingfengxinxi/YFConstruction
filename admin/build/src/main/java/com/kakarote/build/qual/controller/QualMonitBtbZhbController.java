package com.kakarote.build.qual.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.qual.entity.BO.QualMonitSTbZhbBO;
import com.kakarote.build.qual.entity.BO.QualMonitsetBO;
import com.kakarote.build.qual.entity.PO.QualMonitBtbZhb;
import com.kakarote.build.qual.service.IQualMonitBtbZbService;
import com.kakarote.build.qual.service.IQualMonitBtbZhbService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 标养室监测--监测数据填报--主表 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-03-25
 */
@RestController
@RequestMapping("/buildQualMonitBtbZhb")
public class QualMonitBtbZhbController {
    @Autowired
    private IQualMonitBtbZbService iQualMonitBtbZbService;
    @Autowired
    private IQualMonitBtbZhbService iQualMonitBtbZhbService;

    @PostMapping("/list")
    @ApiOperation("查询填报主表信息列表")
    @OperateLog(detail = "查询填报主表信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<QualMonitBtbZhb>> list(@RequestBody QualMonitSTbZhbBO qualMonitSTbZhbBO) {
        BasePage<QualMonitBtbZhb> list = iQualMonitBtbZhbService.selectList(qualMonitSTbZhbBO);
        return Result.ok(list);
    }

    @PostMapping("/queryById/{valveId}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主表主键查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryById(@PathVariable @NotNull Integer valveId){
        return R.ok(iQualMonitBtbZhbService.getById(valveId));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或者修改监测项填报主表信息")
    @OperateLog(detail = "新增或者修改监测项填报主表信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result edit(@Validated
                       @RequestParam(value = "ZHUB",required = false) String ZHUB,
                       @RequestParam(value = "ZIB",required = false) String ZIB,
                       @RequestParam(value = "type",required = false) String type
    ) {

        QualMonitBtbZhb qualMonitSTbZhb = JSONObject.parseObject(ZHUB, QualMonitBtbZhb.class);
        List<QualMonitsetBO> qualMonitset = JSONObject.parseArray(ZIB,QualMonitsetBO.class);
        if(qualMonitSTbZhb.getCreateUserName() == null || qualMonitSTbZhb.getCreateUserName().equals("")){
            String username = UserUtil.getUser().getRealname();
            qualMonitSTbZhb.setCreateUserName(username);
        }
        return Result.ok(iQualMonitBtbZhbService.saveOrUpdateTB(qualMonitSTbZhb,qualMonitset));
    }


    @Transactional
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除监测项填报主表信息")
    @OperateLog(detail = "删除监测项填报主表信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            iQualMonitBtbZhbService.removeById(id);
            iQualMonitBtbZbService.deletByZHid(id);
        }

        return Result.ok();
    }

}

