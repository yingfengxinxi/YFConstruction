package com.kakarote.build.safety.controller;


import com.kakarote.build.safety.entity.BO.BSafetySafetyEducationBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEducation;
import com.kakarote.build.safety.service.IBSafetySafetyEducationService;
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
 * 安全教育培训管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
@RestController
@RequestMapping("/bSafetySafetyEducation")
public class BSafetySafetyEducationController {


    @Autowired
    private IBSafetySafetyEducationService iBSafetySafetyEducationService;

    @RequestMapping("/list")
    @ApiOperation("查询安全教育培训管理列表")
    @OperateLog(detail = "查询安全教育培训管理列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BSafetySafetyEducation>> list(@RequestBody BSafetySafetyEducationBO bSafetySafetyEducationBO)
    {
        BasePage<BSafetySafetyEducation> list = iBSafetySafetyEducationService.selectList(bSafetySafetyEducationBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BSafetySafetyEducation bSafetySafetyEducation)
    {

        bSafetySafetyEducation.setCreateBy(UserUtil.getUser().getRealname());
        bSafetySafetyEducation.setCreateTime(new Date());
        return Result.ok(iBSafetySafetyEducationService.add(bSafetySafetyEducation));
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
        return Result.ok(iBSafetySafetyEducationService.selectById(id));
    }


    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BSafetySafetyEducation bSafetySafetyEducation)
    {
        bSafetySafetyEducation.setUpdateBy(UserUtil.getUser().getRealname());
        bSafetySafetyEducation.setUpdateTime(new Date());
        return Result.ok(iBSafetySafetyEducationService.updateDetails(bSafetySafetyEducation));
    }

    /**
     * 删除字典类型
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {
        return Result.ok(iBSafetySafetyEducationService.deleteDetails(id));
    }


}
