package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.LabourAdvanceRetreatBO;
import com.kakarote.build.labour.entity.PO.LabourAdvanceRetreat;
import com.kakarote.build.labour.service.ILabourAdvanceRetreatService;
import com.kakarote.build.labour.service.impl.LabourAdvanceRetreatServiceImpl;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 人员进场退场管理 前端控制器
 * </p>
 *
 * @author shz
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/labourAdvanceRetreat")
public class LabourAdvanceRetreatController {

    @Autowired
    private ILabourAdvanceRetreatService advanceRetreatService;

    /**
    * @Description:获取人员进场退场数据列表
    * @author shz
    * @date 2022-4-20 11:27
    * @param 
    * @return 
    */
    @ApiOperation("获取人员进场退场数据列表")
    @PostMapping("/selectPageList")
    @OperateLog(detail = "获取人员进场退场数据列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> selectPageList(@RequestBody LabourAdvanceRetreatBO advanceRetreatBO) {
        BasePage<Map<String, Object>> list = advanceRetreatService.selectPageList(advanceRetreatBO);
        return Result.ok(list);
    }

    @ApiOperation("更新人员进场退场数据")
    @PostMapping("/save")
    @OperateLog(detail = "更新人员进场退场数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result save(@RequestBody LabourAdvanceRetreat advanceRetreat) {
        advanceRetreatService.saveOrUpdate(advanceRetreat);
        return Result.ok();
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除人员进场退场数据")
    @OperateLog(detail = "删除人员进场退场数据", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@PathVariable Integer id) {
        return Result.ok(advanceRetreatService.removeById(id));
    }


    @PostMapping("/getById/{id}")
    @ApiOperation("查询人员进场退场数据")
    @OperateLog(detail = "查询人员进场退场数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable Integer id) {
        return Result.ok(advanceRetreatService.getById(id));
    }

}

