package com.kakarote.build.safety.controller;



import com.kakarote.build.safety.entity.BO.BSafetySafetyTrainingBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTraining;
import com.kakarote.build.safety.service.IBSafetySafetyEmergencyMembersService;
import com.kakarote.build.safety.service.IBSafetySafetyTrainingService;
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
 * 安全教育培训管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-04-08
 */
@RestController
@RequestMapping("/bSafetySafetyTraining")
public class BSafetySafetyTrainingController {

    @Autowired
    private IBSafetySafetyTrainingService iBSafetySafetyTrainingService;


    @RequestMapping("/list")
    @ApiOperation("查询安全教育培训管理列表")
    @OperateLog(detail = "查询安全教育培训管理列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<BSafetySafetyTraining>> list(@RequestBody BSafetySafetyTrainingBO bSafetySafetyTrainingBO)
    {
        BasePage<BSafetySafetyTraining> list = iBSafetySafetyTrainingService.selectList(bSafetySafetyTrainingBO);
        return Result.ok(list);
    }

    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BSafetySafetyTraining bSafetySafetyTraining)
    {

        bSafetySafetyTraining.setCreateBy(UserUtil.getUser().getRealname());
        bSafetySafetyTraining.setCreateTime(new Date());
        iBSafetySafetyTrainingService.save(bSafetySafetyTraining);
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
        return Result.ok(iBSafetySafetyTrainingService.getById(id));
    }




    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BSafetySafetyTraining bSafetySafetyTraining)
    {
        bSafetySafetyTraining.setUpdateBy(UserUtil.getUser().getRealname());
        bSafetySafetyTraining.setUpdateTime(new Date());
        iBSafetySafetyTrainingService.updateById(bSafetySafetyTraining);
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
        return Result.ok( iBSafetySafetyTrainingService.removeById(id));
    }

    /**
     * 多条删除
     */
    @PostMapping("/delTrueByIds")
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            iBSafetySafetyTrainingService.removeById(id);
        }
        return Result.ok();
    }

}

