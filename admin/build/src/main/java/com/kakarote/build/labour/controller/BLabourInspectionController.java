package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.BO.BLabourInspectionBO;
import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.build.labour.entity.PO.BLabourInspection;
import com.kakarote.build.labour.service.IBLabour5gvideoService;
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

import java.util.Map;

/**
 * <p>
 * 企业报验 前端控制器
 * </p>
 *
 * @author zhang
 * @since 2022-11-30
 */
@RestController
@RequestMapping("/bLabourInspection")
public class BLabourInspectionController {


    @Autowired
    private IBLabourInspectionService iBLabourInspectionService;


    @RequestMapping("/list")
    @ApiOperation("查询数据列表")
    @OperateLog(detail = "查询数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BLabourInspectionBO>> list(@RequestBody BLabourInspectionBO bLabourInspectionBO)
    {
        BasePage<BLabourInspectionBO> list = iBLabourInspectionService.selectList(bLabourInspectionBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BLabourInspection bLabourInspection)
    {
        String falg=iBLabourInspectionService.add(bLabourInspection);

        return Result.ok();
    }

    /**
     * 根据数据主键ID查询信息
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/selectById/{id}")
    @ApiOperation("查询单条数据详细")
    @OperateLog(detail = "查询单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getInfo(@PathVariable String id)
    {
        return Result.ok(iBLabourInspectionService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BLabourInspection bLabourInspection)
    {

        String falg= iBLabourInspectionService.edit(bLabourInspection);
        return Result.ok();
    }


    /**
     * 删除
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {

        return Result.ok( iBLabourInspectionService.removeById(id));
    }

    /**
     * 修改字典类型
     */
    @PostMapping("/inspectionStatistical")
    @ApiOperation("企业提报统计")
    @OperateLog(detail = "企业提报统计",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result inspectionStatistical()
    {
        return Result.ok(iBLabourInspectionService.inspectionStatistical());
    }


    @RequestMapping("/selectApplyList")
    @ApiOperation("查询申请数据列表")
    @OperateLog(detail = "查询申请数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<Map>> selectApplyList(@RequestBody BLabourInspectionBO bLabourInspectionBO) {
        BasePage<Map> list = iBLabourInspectionService.selectApplyList(bLabourInspectionBO);
        return Result.ok(list);
    }

    /**
     * 监管端使用
     * @param bLabourInspectionBO
     * @return
     */
    @RequestMapping("/selectAllDataList")
    @ApiOperation("监管-查询数据列表")
    @OperateLog(detail = "监管-查询数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<Map>> selectAllDataList(@RequestBody BLabourInspectionBO bLabourInspectionBO) {
        BasePage<Map> list = iBLabourInspectionService.selectAllDataList(bLabourInspectionBO);
        return Result.ok(list);
    }
}

