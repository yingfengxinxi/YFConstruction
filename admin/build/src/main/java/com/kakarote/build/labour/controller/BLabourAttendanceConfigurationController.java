package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceConfigurationBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceConfiguration;
import com.kakarote.build.labour.service.IBLabourAttendanceConfigurationService;
import com.kakarote.build.labour.service.IBLabourAttendanceDetailService;
import com.kakarote.build.labour.service.IBLabourAttendanceService;
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
 * 考勤日期设定 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-12-17
 */
@RestController
@RequestMapping("/bLabourAttendanceConfiguration")
public class BLabourAttendanceConfigurationController {

    @Autowired
    private IBLabourAttendanceConfigurationService iBLabourAttendanceConfigurationService;





    @RequestMapping("/list")
    @ApiOperation("查询考勤日期设定列表")
    @OperateLog(detail = "查询考勤日期设定列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BLabourAttendanceConfiguration>> list(@RequestBody BLabourAttendanceConfigurationBO bLabourAttendanceConfigurationBO)
    {
        BasePage<BLabourAttendanceConfiguration> list = iBLabourAttendanceConfigurationService.selectList(bLabourAttendanceConfigurationBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增考勤日期设定数据")
    @OperateLog(detail = "新增考勤日期设定数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BLabourAttendanceConfiguration bLabourAttendanceConfiguration)
    {
        String falg=iBLabourAttendanceConfigurationService.add(bLabourAttendanceConfiguration);

        return Result.ok();
    }

    /**
     * 根据数据主键ID查询信息
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/selectById/{id}")
    @ApiOperation("查询考勤日期设定单条数据详细")
    @OperateLog(detail = "查询考勤日期设定单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getInfo(@PathVariable String id)
    {
        return Result.ok(iBLabourAttendanceConfigurationService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改考勤日期设定数据")
    @OperateLog(detail = "修改考勤日期设定数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BLabourAttendanceConfiguration bLabourAttendanceConfiguration)
    {

        String falg= iBLabourAttendanceConfigurationService.edit(bLabourAttendanceConfiguration);
        return Result.ok();
    }

    /**
     * 数据添加前验证
     */
    @PostMapping("/validation")
    @ApiOperation("考勤日期设定数据验证")
    @OperateLog(detail = "考勤日期设定数据验证",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result validation(@Validated @RequestBody BLabourAttendanceConfiguration bLabourAttendanceConfiguration)
    {

        String falg= iBLabourAttendanceConfigurationService.validation(bLabourAttendanceConfiguration);
        ;
        return Result.ok().setData(falg);
    }

    /**
     * 删除
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除考勤日期设定数据详情")
    @OperateLog(detail = "删除考勤日期设定数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {
        return Result.ok( iBLabourAttendanceConfigurationService.removeById(id));
    }


}

