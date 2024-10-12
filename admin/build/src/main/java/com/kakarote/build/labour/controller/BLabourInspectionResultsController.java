package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.BLabourInspectionBO;
import com.kakarote.build.labour.entity.PO.BLabourInspection;
import com.kakarote.build.labour.entity.PO.BLabourInspectionResults;
import com.kakarote.build.labour.service.IBLabourInspectionResultsService;
import com.kakarote.build.labour.service.IBLabourInspectionService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 企业报验结果表 前端控制器
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
@RestController
@RequestMapping("/bLabourInspectionResults")
public class BLabourInspectionResultsController {

    @Autowired
    private IBLabourInspectionResultsService iBLabourInspectionResultsService;


    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BLabourInspectionResults bLabourInspectionResults)
    {
        String falg=iBLabourInspectionResultsService.add(bLabourInspectionResults);

        return Result.ok();
    }

    /**
     * 根据数据主键ID查询信息
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/getPid/{id}")
    @ApiOperation("查询单条数据详细")
    @OperateLog(detail = "查询单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getPid(@PathVariable String id)
    {
        return Result.ok(iBLabourInspectionResultsService.getPid(id));
    }

}

