package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.BLabourAttendanceBO;
import com.kakarote.build.labour.entity.BO.BLabourAttendanceDetailBO;
import com.kakarote.build.labour.entity.PO.BLabourAttendance;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetail;
import com.kakarote.build.labour.entity.PO.BLabourAttendanceDetailMonth;
import com.kakarote.build.labour.service.IBLabourAttendanceDetailService;
import com.kakarote.build.labour.service.IBLabourAttendanceService;
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
 * 考勤明细管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/bLabourAttendanceDetail")
public class BLabourAttendanceDetailController {



    @Autowired
    private IBLabourAttendanceDetailService iBLabourAttendanceDetailService;


    @Autowired
    private IBLabourAttendanceService iBLabourAttendanceService;



    @RequestMapping("/list")
    @ApiOperation("查询安全教育培训管理列表")
    @OperateLog(detail = "查询安全教育培训管理列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BLabourAttendanceDetail>> list(@RequestBody BLabourAttendanceDetailBO bLabourAttendanceDetailBO)
    {
        BasePage<BLabourAttendanceDetail> list = iBLabourAttendanceDetailService.selectList(bLabourAttendanceDetailBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BLabourAttendanceDetail bLabourAttendanceDetail)
    {

        bLabourAttendanceDetail.setCreateBy(UserUtil.getUser().getRealname());
        bLabourAttendanceDetail.setCreateTime(new Date());
        iBLabourAttendanceDetailService.save(bLabourAttendanceDetail);
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
        return Result.ok(iBLabourAttendanceDetailService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BLabourAttendanceDetail bLabourAttendanceDetail)
    {
        bLabourAttendanceDetail.setUpdateBy(UserUtil.getUser().getRealname());
        bLabourAttendanceDetail.setUpdateTime(new Date());
        iBLabourAttendanceDetailService.updateById(bLabourAttendanceDetail);
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
        return Result.ok( iBLabourAttendanceDetailService.removeById(id));
    }

    /**
     * 多条删除
     */
    @PostMapping("/delTrueByIds")
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            iBLabourAttendanceDetailService.removeById(id);
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
        BLabourAttendance bLabourAttendance=   iBLabourAttendanceService.getById(id);
        BLabourAttendanceDetailMonth bLabourAttendanceDetailMonth=new BLabourAttendanceDetailMonth();
        bLabourAttendanceDetailMonth.setCreateBy(bLabourAttendance.getAttendanceMonth());
        bLabourAttendanceDetailMonth.setAttendanceId(id);
        List<BLabourAttendanceDetailMonth> list=iBLabourAttendanceDetailService.selectMonth(bLabourAttendanceDetailMonth);
        return Result.ok(list);
    }

}

