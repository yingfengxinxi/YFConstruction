package com.kakarote.build.safety.controller;


import com.kakarote.build.safety.entity.BO.BSafetySafetyEmergencyBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergency;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.build.safety.service.IBSafetySafetyEmergencyMembersService;
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

/**
 * <p>
 * 应急班组成员管理 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-03-22
 */
@RestController
@RequestMapping("/bSafetySafetyEmergencyMembers")
public class BSafetySafetyEmergencyMembersController {

    @Autowired
    private IBSafetySafetyEmergencyMembersService iBSafetySafetyEmergencyMembersService;



    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BSafetySafetyEmergencyMembers bSafetySafetyEmergencyMembers)
    {

        bSafetySafetyEmergencyMembers.setCreateBy(UserUtil.getUser().getRealname());
        bSafetySafetyEmergencyMembers.setCreateTime(new Date());
        iBSafetySafetyEmergencyMembersService.save(bSafetySafetyEmergencyMembers);
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
        return Result.ok(iBSafetySafetyEmergencyMembersService.getById(id));
    }

    /**
     * 根据数据主键ID查询信息
     *
     * @param id 数据主键
     * @return
     */
    @GetMapping(value = "/selectByPId/{id}")
    @ApiOperation("查询单条数据详细")
    @OperateLog(detail = "查询单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result selectByPId(@PathVariable String id)
    {
        return Result.ok(iBSafetySafetyEmergencyMembersService.getByPId(id));
    }


    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BSafetySafetyEmergencyMembers bSafetySafetyEmergencyMembers)
    {
        bSafetySafetyEmergencyMembers.setUpdateBy(UserUtil.getUser().getRealname());
        bSafetySafetyEmergencyMembers.setUpdateTime(new Date());
        iBSafetySafetyEmergencyMembersService.updateById(bSafetySafetyEmergencyMembers);
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
        return Result.ok( iBSafetySafetyEmergencyMembersService.removeById(id));
    }

    /**
     * 多条删除
     */
    @PostMapping("/delTrueByIds")
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            iBSafetySafetyEmergencyMembersService.removeById(id);
        }
        return Result.ok();
    }


}

