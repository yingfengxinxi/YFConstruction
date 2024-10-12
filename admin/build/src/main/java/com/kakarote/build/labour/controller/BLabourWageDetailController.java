package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.BLabourWageDetailBO;
import com.kakarote.build.labour.entity.PO.*;
import com.kakarote.build.labour.service.IBLabourWageDetailService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工资管理详情 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-04-26
 */
@RestController
@RequestMapping("/bLabourWageDetail")
public class BLabourWageDetailController {


    @Autowired
    private IBLabourWageDetailService iBLabourWageDetailService;


    @RequestMapping("/list")
    @ApiOperation("查询工资详情列表")
    @OperateLog(detail = "查询工资详情列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BLabourWageDetail>> list(@RequestBody BLabourWageDetailBO bLabourWageDetailBO)
    {
        BasePage<BLabourWageDetail> list = iBLabourWageDetailService.selectList(bLabourWageDetailBO);
        return Result.ok(list);
    }


    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BLabourWageDetail bLabourWageDetail)
    {

        bLabourWageDetail.setCreateBy(UserUtil.getUser().getRealname());
        bLabourWageDetail.setCreateTime(new Date());
        iBLabourWageDetailService.save(bLabourWageDetail);
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
        return Result.ok(iBLabourWageDetailService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BLabourWageDetail bLabourWageDetail)
    {
        bLabourWageDetail.setUpdateBy(UserUtil.getUser().getRealname());
        bLabourWageDetail.setUpdateTime(new Date());
        iBLabourWageDetailService.updateById(bLabourWageDetail);
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
        return Result.ok( iBLabourWageDetailService.removeById(id));
    }

    /**
     * 多条删除
     */
    @PostMapping("/delTrueByIds")
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            iBLabourWageDetailService.removeById(id);
        }
        return Result.ok();
    }


    /**
     * 根据数据主表的主键ID查询信息
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/selectByPId/{id}")
    @ApiOperation("根据数据主表的主键ID查询信息")
    @OperateLog(detail = "根据数据主表的主键ID查询信息",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getPid(@PathVariable String id)
    {
        List<BLabourWageDetail> list=iBLabourWageDetailService.selectByPid(id);
        return Result.ok(list);
    }


}

