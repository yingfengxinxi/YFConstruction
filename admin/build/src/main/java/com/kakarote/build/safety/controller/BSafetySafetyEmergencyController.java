package com.kakarote.build.safety.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.safety.entity.BO.BSafetySafetyEmergencyBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergency;
import com.kakarote.build.safety.service.IBSafetySafetyEmergencyService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 安全应急管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
@RestController
@RequestMapping("/bSafetySafetyEmergency")
public class BSafetySafetyEmergencyController {


    @Autowired
    private IBSafetySafetyEmergencyService iBSafetySafetyEmergencyService;

    /**
     * 分页列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody BSafetySafetyEmergencyBO bSafetySafetyEmergencyBO) {
        return Result.ok(iBSafetySafetyEmergencyService.selectList(bSafetySafetyEmergencyBO));
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BSafetySafetyEmergency bSafetySafetyEmergency)
    {

        bSafetySafetyEmergency.setCreateBy(UserUtil.getUser().getRealname());
        bSafetySafetyEmergency.setCreateTime(new Date());
        iBSafetySafetyEmergencyService.save(bSafetySafetyEmergency);
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
        return Result.ok(iBSafetySafetyEmergencyService.getById(id));
    }


    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BSafetySafetyEmergency bSafetySafetyEmergency)
    {
        bSafetySafetyEmergency.setUpdateBy(UserUtil.getUser().getRealname());
        bSafetySafetyEmergency.setUpdateTime(new Date());
        iBSafetySafetyEmergencyService.updateById(bSafetySafetyEmergency);
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
        return Result.ok( iBSafetySafetyEmergencyService.removeById(id));
    }

    /**
     * 多条删除
     */
    @PostMapping("/delTrueByIds")
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            iBSafetySafetyEmergencyService.removeById(id);
        }
        return Result.ok();
    }

    @PostMapping(value = "/selectIdAndName")
    @ApiOperation("查询id和name用于应急事故处置，关联应急预案")
    @OperateLog(detail = "查询id和name用于应急事故处置，关联应急预案",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result selectIdAndName()
    {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("id","safety_emergency_plan_name as name");
        List<Map> list = iBSafetySafetyEmergencyService.getBaseMapper().selectMaps(queryWrapper);

        return Result.ok(list);
    }

}

