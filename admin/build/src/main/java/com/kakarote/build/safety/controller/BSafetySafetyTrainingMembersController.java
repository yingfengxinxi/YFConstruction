package com.kakarote.build.safety.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.safety.entity.PO.BSafetySafetyEmergencyMembers;
import com.kakarote.build.safety.entity.PO.BSafetySafetyTrainingMembers;
import com.kakarote.build.safety.service.IBSafetySafetyEmergencyMembersService;
import com.kakarote.build.safety.service.IBSafetySafetyTrainingMembersService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 安全教育受教人员列表 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-04-08
 */
@RestController
@RequestMapping("/bSafetySafetyTrainingMembers")
public class BSafetySafetyTrainingMembersController {

    @Autowired
    private IBSafetySafetyTrainingMembersService iBSafetySafetyTrainingMembersService;

    @Autowired
    private ILabourInfocollectionService iLabourInfocollectionService;





    /**
     *  新增数据
     */
    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody BSafetySafetyTrainingMembers bSafetySafetyTrainingMembers)
    {

        bSafetySafetyTrainingMembers.setCreateBy(UserUtil.getUser().getRealname());
        bSafetySafetyTrainingMembers.setCreateTime(new Date());
        iBSafetySafetyTrainingMembersService.save(bSafetySafetyTrainingMembers);
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
        return Result.ok(iBSafetySafetyTrainingMembersService.getById(id));
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
        return Result.ok(iBSafetySafetyTrainingMembersService.getByPId(id));
    }


    /**
     * 修改字典类型
     */
    @PostMapping("/edit")
    @ApiOperation("修改数据")
    @OperateLog(detail = "修改数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody BSafetySafetyTrainingMembers bSafetySafetyTrainingMembers)
    {
        bSafetySafetyTrainingMembers.setCreateBy(UserUtil.getUser().getRealname());
        bSafetySafetyTrainingMembers.setCreateTime(new Date());
        iBSafetySafetyTrainingMembersService.updateById(bSafetySafetyTrainingMembers);
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
        return Result.ok( iBSafetySafetyTrainingMembersService.removeById(id));
    }
    /**
     * 主数据删除联动子数据删除
     */
    @GetMapping(value = "/deleteDetailsPid/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result deleteDetailsPid(@PathVariable String id)
    {
        return Result.ok( iBSafetySafetyTrainingMembersService.deleteByPId(id));
    }
    /**
     * 多条删除
     */
    @PostMapping("/delTrueByIds")
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            iBSafetySafetyTrainingMembersService.removeById(id);
        }
        return Result.ok();
    }

    /**
     * 多选新增
     */
    @PostMapping("/addByIds")
    public Result addByIds(@ApiParam(name = "ids", value = "id列表") @RequestBody Map data) {
        System.out.println("dayin============================================="+data);
        String pid=data.get("pid").toString();
        String ids =data.get("ids").toString();
        ids=ids.replace("[","");
        ids=ids.replace("]","");
        ids=ids.trim();
        String[] arr =ids.split(",");

    for (String id : arr) {
        LabourInfocollection labourInfocollection=iLabourInfocollectionService.getById(id);
            BSafetySafetyTrainingMembers bSafetySafetyTrainingMembers=new BSafetySafetyTrainingMembers ();
            bSafetySafetyTrainingMembers.setCreateBy(UserUtil.getUser().getRealname());
            bSafetySafetyTrainingMembers.setCreateTime(new Date());
            bSafetySafetyTrainingMembers.setInfocollectionId(id);
            bSafetySafetyTrainingMembers.setTrainingId(pid);
            bSafetySafetyTrainingMembers.setInfocollectionName(labourInfocollection.getName());
            bSafetySafetyTrainingMembers.setInfocollectionTeamName(labourInfocollection.getTeamName());
            iBSafetySafetyTrainingMembersService.save(bSafetySafetyTrainingMembers);
        }
        return Result.ok();
    }

}

