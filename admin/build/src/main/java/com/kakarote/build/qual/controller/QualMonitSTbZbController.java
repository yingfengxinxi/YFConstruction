package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.QualMonitSTbZbBO;
import com.kakarote.build.qual.entity.PO.QualMonitSTbZb;
import com.kakarote.build.qual.service.IQualMonitSTbZbService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 监测数据填报--子表 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-03-24
 */
@RestController
@RequestMapping("/buildQualMonitTbZb")
public class QualMonitSTbZbController {
    @Autowired
    private IQualMonitSTbZbService iQualMonitSTbZbService;

    @ApiOperation(value = "查询所有监测项填报子表信息")
    @RequestMapping("/query")
    @OperateLog(detail = "查询所有监测项填报子表信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<QualMonitSTbZb>> query(){
        return R.ok(iQualMonitSTbZbService.list());
    }

    @PostMapping("/list")
    @ApiOperation("查询填报子表信息列表")
    @OperateLog(detail = "查询填报子表信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<QualMonitSTbZb>> list(@RequestBody QualMonitSTbZbBO qualMonitSTbZbBO) {
        BasePage<QualMonitSTbZb> list = iQualMonitSTbZbService.selectList(qualMonitSTbZbBO);
        return Result.ok(list);
    }

    @PostMapping("/queryById/{valveId}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryById(@PathVariable @NotNull Integer valveId){
        return R.ok(iQualMonitSTbZbService.getById(valveId));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或者修改监测项填报子表信息")
    @OperateLog(detail = "新增或者修改监测项填报子表信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody QualMonitSTbZb qualMonitSTbZb) {
        return Result.ok(iQualMonitSTbZbService.saveOrUpdate(qualMonitSTbZb));
    }


    @PostMapping("/delTrueByIds")
    @ApiOperation("删除监测项填报子表信息")
    @OperateLog(detail = "删除监测项填报子表信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            iQualMonitSTbZbService.removeById(id);
        }
        return Result.ok();
    }

    @PostMapping("/listByNoPage")
    @ApiOperation("查询填报子表信息列表")
    @OperateLog(detail = "查询填报子表信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<QualMonitSTbZb>> listNoPage(@RequestBody QualMonitSTbZbBO qualMonitSTbZbBO) {
        List<QualMonitSTbZb> list = iQualMonitSTbZbService.selectListNoPage(qualMonitSTbZbBO);
        return Result.ok(list);
    }

    @PostMapping("/queryByZHUId/{valveId}")
    @ApiOperation("根据主表主键查询信息")
    @OperateLog(detail = "根据主表主键查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryByZHUId(@PathVariable @NotNull Integer valveId){
        return R.ok(iQualMonitSTbZbService.selectT(valveId));
    }

}

