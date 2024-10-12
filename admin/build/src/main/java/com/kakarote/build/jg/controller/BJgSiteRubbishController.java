package com.kakarote.build.jg.controller;


import com.kakarote.build.jg.entity.BO.BJgSiteRubbishBO;
import com.kakarote.build.jg.entity.PO.BJgSiteRubbish;
import com.kakarote.build.jg.service.IBJgSiteRubbishService;
import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.service.IBSafetySafetyDataService;
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
 * 工地垃圾分类管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2022-02-09
 */
@RestController
@RequestMapping("/bJgSiteRubbish")
public class BJgSiteRubbishController {
    @Autowired
    private IBJgSiteRubbishService iBJgSiteRubbishService;

    @RequestMapping("/list")
    @ApiOperation("查询工地垃圾分类管理列表")
    @OperateLog(detail = "查询工地垃圾分类管理列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BJgSiteRubbish>> list(@RequestBody BJgSiteRubbishBO bJgSiteRubbishBO)
    {
        BasePage<BJgSiteRubbish> list = iBJgSiteRubbishService.selectList(bJgSiteRubbishBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增工地垃圾分类管理数据")
    @OperateLog(detail = "新增工地垃圾分类管理数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BJgSiteRubbish bJgSiteRubbish)
    {

        bJgSiteRubbish.setCreateBy(UserUtil.getUser().getRealname());
        bJgSiteRubbish.setCreateTime(new Date());
        return Result.ok(iBJgSiteRubbishService.save(bJgSiteRubbish));
    }

    /**
     * 根据数据主键ID查询信息
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/selectById/{id}")
    @ApiOperation("查询工地垃圾分类管理单条数据详细")
    @OperateLog(detail = "查询工地垃圾分类管理单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getInfo(@PathVariable String id)
    {
        return Result.ok(iBJgSiteRubbishService.getById(id));
    }


    /**
     * 修改数据
     */
    @PostMapping("/edit")
    @ApiOperation("修改工地垃圾分类管理数据")
    @OperateLog(detail = "修改工地垃圾分类管理数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BJgSiteRubbish bJgSiteRubbish)
    {
        bJgSiteRubbish.setUpdateBy(UserUtil.getUser().getRealname());
        bJgSiteRubbish.setUpdateTime(new Date());
        return Result.ok(iBJgSiteRubbishService.updateById(bJgSiteRubbish));
    }

    /**
     * 删除数据
     */
    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除工地垃圾分类管理数据详情")
    @OperateLog(detail = "删除工地垃圾分类管理数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {
        return Result.ok(iBJgSiteRubbishService.removeById(id));
    }

}

