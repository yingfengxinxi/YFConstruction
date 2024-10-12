package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.BLabour5gvideoBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceEpidemicBO;
import com.kakarote.build.labour.entity.PO.BLabour5gvideo;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceEpidemic;
import com.kakarote.build.labour.service.IBLabourAttendanceEpidemicService;
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
 * 疫情进出场统计 前端控制器
 * </p>
 *
 * @author zhang
 * @since 2022-11-29
 */
@RestController
@RequestMapping("/bLabourAttendanceEpidemic")
public class BLabourAttendanceEpidemicController {

    @Autowired
    private IBLabourAttendanceEpidemicService iBLabourAttendanceEpidemicService;


    @RequestMapping("/list")
    @ApiOperation("查询数据列表")
    @OperateLog(detail = "查询数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BLabourAttendanceEpidemicBO>> list(@RequestBody BLabourAttendanceEpidemicBO bLabourAttendanceEpidemicBO)
    {
        BasePage<BLabourAttendanceEpidemicBO> list = iBLabourAttendanceEpidemicService.selectList(bLabourAttendanceEpidemicBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BLabourAttendanceEpidemic bLabourAttendanceEpidemic)
    {
        String falg=iBLabourAttendanceEpidemicService.add(bLabourAttendanceEpidemic);

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
        return Result.ok(iBLabourAttendanceEpidemicService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BLabourAttendanceEpidemic bLabourAttendanceEpidemic)
    {

        String falg= iBLabourAttendanceEpidemicService.edit(bLabourAttendanceEpidemic);
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

        return Result.ok( iBLabourAttendanceEpidemicService.removeById(id));
    }

    @PostMapping("/attendanceEpidemicStatistical")
    @ApiOperation("疫情进出场统计")
    @OperateLog(detail = "疫情进出场统计",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result attendanceEpidemicStatistical()
    {
        return Result.ok(iBLabourAttendanceEpidemicService.attendanceEpidemicStatistical());
    }
}

