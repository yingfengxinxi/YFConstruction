package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.BQualMarkingroomBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroom;
import com.kakarote.build.qual.service.IBQualMarkingroomService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 标养室基本信息 前端控制器
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
@RestController
@RequestMapping("/bQualMarkingroom")
public class BQualMarkingroomController {

    @Autowired
    private IBQualMarkingroomService iBQualMarkingroomService;

    @RequestMapping("/list")
    @ApiOperation("查询数据列表")
    @OperateLog(detail = "查询数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BQualMarkingroom>> list(@RequestBody BQualMarkingroomBO bQualMarkingroomBO)
    {
        BasePage<BQualMarkingroom> list = iBQualMarkingroomService.selectDataList(bQualMarkingroomBO);
        return Result.ok(list);
    }
    @PostMapping("/listAll")
    @ApiOperation("查询全部数据列表")
    @OperateLog(detail = "查询全部数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result listAll()
    {
        List<BQualMarkingroom> list = iBQualMarkingroomService.listAll();
        return Result.ok(list);
    }
    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BQualMarkingroom bQualMarkingroom)
    {

        bQualMarkingroom.setCreateBy(UserUtil.getUser().getRealname());
        bQualMarkingroom.setCreateTime(new Date());
        return Result.ok(iBQualMarkingroomService.save(bQualMarkingroom));
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
        return Result.ok(iBQualMarkingroomService.getById(id));
    }


    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BQualMarkingroom bQualMarkingroom)
    {
        bQualMarkingroom.setUpdateBy(UserUtil.getUser().getRealname());
        bQualMarkingroom.setUpdateTime(new Date());
        return Result.ok(iBQualMarkingroomService.updateById(bQualMarkingroom));
    }

    /**
     * 删除字典类型
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {
        return Result.ok(iBQualMarkingroomService.removeById(id));
    }
}

