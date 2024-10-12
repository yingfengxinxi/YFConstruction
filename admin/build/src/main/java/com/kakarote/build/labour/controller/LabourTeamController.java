package com.kakarote.build.labour.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.labour.entity.BO.LabourTeamBO;
import com.kakarote.build.labour.entity.PO.LabourInfocollection;
import com.kakarote.build.labour.entity.PO.LabourSubcontractor;
import com.kakarote.build.labour.entity.PO.LabourTeam;
import com.kakarote.build.labour.service.ILabourInfocollectionService;
import com.kakarote.build.labour.service.ILabourTeamService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 劳务管理--班组 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/labourTeam")
public class LabourTeamController {

    @Autowired
    private ILabourTeamService teamService;

    @Autowired
    private ILabourInfocollectionService infocollectionService;

    @ApiOperation("分页查询-获取分班组数据列表")
    @PostMapping("/list")
    @OperateLog(detail = "分页查询-获取分班组数据列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> list(@RequestBody LabourTeamBO teamBO) {
        BasePage<Map<String,Object>> list = teamService.selectList(teamBO);
        return Result.ok(list);
    }

    @PostMapping("/queryById/{id}")
    @ApiOperation("根据主键查询班组信息")
    @OperateLog(detail = "根据主键查询班组信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryById(@PathVariable @NotNull Integer id){
        return R.ok(teamService.getById(id));
    }

    @PostMapping("/add")
    @ApiOperation("新增班组信息")
    @OperateLog(detail = "新增班组信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result add(@RequestBody LabourTeam labourTeam) {

        // 同一个项目下面，班组名称不能重复。
        HashMap<String, Object> params = new HashMap<>();
        params.put("teamName", labourTeam.getTeamName());
        List<LabourTeam> list = teamService.selectListByName(params);
        if (list != null && list.size() > 0) {
            throw new CrmException(500, "同一个项目下，班组名称不能重复！");
        }

        teamService.save(labourTeam);
        // 更新班组长的班组id
        LabourInfocollection infocollection = new LabourInfocollection();
        infocollection.setId(labourTeam.getTeamLeaderId());
        infocollection.setTeamName(labourTeam.getId());
        infocollectionService.updateById(infocollection);

        return Result.ok();
    }

    @PostMapping("/edit")
    @ApiOperation("修改班组信息")
    @OperateLog(detail = "修改班组信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody LabourTeam labourTeam) {

        // 同一个项目下面，班组名称不能重复。
        HashMap<String, Object> params = new HashMap<>();
        params.put("teamName", labourTeam.getTeamName());
        params.put("id", labourTeam.getId());
        List<LabourTeam> list = teamService.selectListByName(params);
        if (list != null && list.size() > 0) {
            throw new CrmException(500, "同一个项目下，班组名称不能重复！");
        }

        // 更新班组长的班组id
        LabourInfocollection infocollection = new LabourInfocollection();
        infocollection.setId(labourTeam.getTeamLeaderId());
        infocollection.setTeamName(labourTeam.getId());
        infocollectionService.updateById(infocollection);

        return Result.ok(teamService.updateById(labourTeam));
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除班组信息")
    @OperateLog(detail = "删除班组信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@PathVariable Integer id) {
        return Result.ok(teamService.removeById(id));
    }

    @ApiOperation(value = "查询所有班组的id和name")
    @RequestMapping("/selcetIdsAndName")
    @OperateLog(detail = "查询所有班组的id和name", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<LabourTeam>> selcetIdsAndName(){
        List<LabourTeam> list = teamService.selcetIdsAndName();
        return Result.ok(list);
    }
}

