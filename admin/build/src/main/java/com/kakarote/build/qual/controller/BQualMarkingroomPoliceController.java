package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.BQualMarkingroomBO;
import com.kakarote.build.qual.entity.BO.BQualMarkingroomPoliceBO;
import com.kakarote.build.qual.entity.PO.BQualMarkingroom;
import com.kakarote.build.qual.entity.PO.BQualMarkingroomPolice;
import com.kakarote.build.qual.service.IBQualMarkingroomPoliceService;
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

import java.util.Date;

/**
 * <p>
 * 标养室报警信息 前端控制器
 * </p>
 *
 * @author zhang
 * @since 2022-07-14
 */
@RestController
@RequestMapping("/bQualMarkingroomPolice")
public class BQualMarkingroomPoliceController {

    @Autowired
    private IBQualMarkingroomPoliceService iBQualMarkingroomPoliceService;

    @RequestMapping("/list")
    @ApiOperation("查询数据列表")
    @OperateLog(detail = "查询数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BQualMarkingroomPolice>> list(@RequestBody BQualMarkingroomPoliceBO bQualMarkingroomPoliceBO)
    {
        BasePage<BQualMarkingroomPolice> list = iBQualMarkingroomPoliceService.selectDataList(bQualMarkingroomPoliceBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BQualMarkingroomPolice bQualMarkingroomPolice)
    {

        bQualMarkingroomPolice.setCreateBy(UserUtil.getUser().getRealname());
        bQualMarkingroomPolice.setCreateTime(new Date());
        return Result.ok(iBQualMarkingroomPoliceService.save(bQualMarkingroomPolice));
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
        return Result.ok(iBQualMarkingroomPoliceService.getById(id));
    }


    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BQualMarkingroomPolice bQualMarkingroomPolice)
    {
        bQualMarkingroomPolice.setUpdateBy(UserUtil.getUser().getRealname());
        bQualMarkingroomPolice.setUpdateTime(new Date());
        return Result.ok(iBQualMarkingroomPoliceService.updateById(bQualMarkingroomPolice));
    }

    /**
     * 删除字典类型
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {
        return Result.ok(iBQualMarkingroomPoliceService.removeById(id));
    }
}

