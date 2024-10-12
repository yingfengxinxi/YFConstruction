package com.kakarote.build.project.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.project.entity.BO.ProgressSelfEvaluateYearBO;
import com.kakarote.build.project.entity.PO.ProgressSelfEvaluateYear;
import com.kakarote.build.project.service.IProgressSelfEvaluateYearService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 年度自评 前端控制器
 * </p>
 *
 * @author shz
 * @since 2022-06-17
 */
@RestController
@RequestMapping("/progressSelfEvaluateYear")
public class ProgressSelfEvaluateYearController {

    @Autowired
    private IProgressSelfEvaluateYearService selfEvaluateService;

//    public Result save(ProgressSelfEvaluate selfEvaluate){
//        selfEvaluateService.saveOrUpdate(selfEvaluate);
//        return R.ok();
//    }

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<ProgressSelfEvaluateYearBO>> list(@RequestBody ProgressSelfEvaluateYearBO selfEvaluateBO) {
        BasePage<ProgressSelfEvaluateYearBO> list = selfEvaluateService.selectList(selfEvaluateBO);
        return Result.ok(list);
    }

    @GetMapping("/getEvaluateList/{id}")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<ProgressSelfEvaluateYearBO>> getEvaluateList(@PathVariable String id){
        List<ProgressSelfEvaluateYearBO> list = selfEvaluateService.getEvaluateList(id);
        return Result.ok(list);
    }

    @GetMapping("/getPrintData/{id}")
    @ApiOperation("获取打印数据")
    @OperateLog(detail = "获取打印数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map> getPrintData(@PathVariable String id){
        Map res = selfEvaluateService.getPrintData(id);
        return Result.ok(res);
    }

    @PostMapping("/add")
    @ApiOperation("新增信息")
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result add(@RequestBody List<ProgressSelfEvaluateYear> selfEvaluates){
        selfEvaluateService.saveBatch(selfEvaluates);
        return R.ok();
    }

    @GetMapping("/delBySecurityId/{id}")
    @ApiOperation("通过检查目录id删除")
    @OperateLog(detail = "通过检查目录id删除", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map> delBySecurityId(@PathVariable String id){
        ProgressSelfEvaluateYear selfEvaluate = new ProgressSelfEvaluateYear();
        selfEvaluate.setSecurityId(id);
        selfEvaluateService.remove(new QueryWrapper<>(selfEvaluate));
        return Result.ok();
    }

    @PostMapping("/update")
    @ApiOperation("修改信息")
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result update(@RequestBody ProgressSelfEvaluateYear selfEvaluate){
        selfEvaluateService.updateById(selfEvaluate);
        return R.ok();
    }
}

