package com.kakarote.build.jg.controller;


import com.kakarote.build.jg.entity.BO.BCreditBadBO;
import com.kakarote.build.jg.entity.BO.JgEducationTrainRecordBO;
import com.kakarote.build.jg.entity.PO.BCreditBad;
import com.kakarote.build.jg.entity.PO.JgEducationTrainRecord;
import com.kakarote.build.jg.service.IBCreditBadService;
import com.kakarote.build.jg.service.IJgEducationTrainRecordService;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 信用不良行为 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-11-25
 */
@RestController
@RequestMapping("/bCreditBad")
public class BCreditBadController {

    @Autowired
    private IBCreditBadService iBCreditBadService;

    @PostMapping("/list")
    @ApiOperation("查询信用不良行为列表")
    @OperateLog(detail = "查询信用不良行为列表", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<BCreditBad>> list(@RequestBody BCreditBadBO bCreditBadBO) {
        return Result.ok(iBCreditBadService.selectList(bCreditBadBO));
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BCreditBad bCreditBad)
    {
        String falg=iBCreditBadService.add(bCreditBad);

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
        return Result.ok(iBCreditBadService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BCreditBad bCreditBad)
    {

        String falg= iBCreditBadService.edit(bCreditBad);
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

        return Result.ok( iBCreditBadService.removeById(id));
    }


}

