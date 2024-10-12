package com.kakarote.build.labour.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.labour.entity.BO.LabourWorktypeBO;
import com.kakarote.build.labour.entity.PO.LabourWorktype;
import com.kakarote.build.labour.service.ILabourWorktypeService;
import com.kakarote.build.labour.service.impl.LabourWorktypeServiceImpl;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.sql.Wrapper;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 劳务管理--工种 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-03-15
 */
@RestController
@RequestMapping("/labourWorktype")
public class LabourWorktypeController {
    @Autowired
    private ILabourWorktypeService labourWorktypeService;

    @ApiOperation(value = "查询所有工种信息")
    @RequestMapping("/query")
    @OperateLog(detail = "查询所有工种信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<LabourWorktype>> query(){
        return R.ok(labourWorktypeService.list());
    }

    @PostMapping("/list")
    @ApiOperation("查询工种信息列表")
    @OperateLog(detail = "查询工种信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<LabourWorktype>> list(@RequestBody LabourWorktypeBO labourWorktypeBO) {
        BasePage<LabourWorktype> list = labourWorktypeService.selectList(labourWorktypeBO);
        return Result.ok(list);
    }

    @PostMapping("/queryById/{valveId}")
    @ApiOperation("根据主键查询工种信息")
    @OperateLog(detail = "根据主键查询工种信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryById(@PathVariable @NotNull Integer valveId){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id",valveId);
        queryWrapper.ne("project_Id",0);
        return R.ok(labourWorktypeService.getOne(queryWrapper));
    }

    @PostMapping("/add")
    @ApiOperation("新增工种信息")
    @OperateLog(detail = "新增工种信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody LabourWorktype labourWorktype) {
        Long projectId = UserUtil.getUser().getProjectId();
        labourWorktype.setProjectId(Integer.parseInt(String.valueOf(projectId)));
        return Result.ok(labourWorktypeService.saveOrUpdate(labourWorktype));
    }


    @PostMapping("/update")
    @ApiOperation("修改工种信息")
    @OperateLog(detail = "修改工种信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody LabourWorktype labourWorktype) {
        return Result.ok(labourWorktypeService.saveOrUpdate(labourWorktype));
    }


    @PostMapping("/delete/{id}")
    @ApiOperation("删除工种信息")
    @OperateLog(detail = "删除工种信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable Integer id) {
        return Result.ok(labourWorktypeService.removeById(id));
    }


    @ApiOperation(value = "查询所有工种的id和worktype")
    @RequestMapping("/selcetIdsAndWorktype")
    @OperateLog(detail = "查询所有工种的id和worktype", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<LabourWorktype>> selcetIdsAndWorktype(){
        List<LabourWorktype> labourWorktypes = labourWorktypeService.selcetIdsAndWorktype();
        return Result.ok(labourWorktypes);
    }
}

