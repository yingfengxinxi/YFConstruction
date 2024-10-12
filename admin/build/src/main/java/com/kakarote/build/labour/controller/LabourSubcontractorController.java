package com.kakarote.build.labour.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kakarote.build.labour.entity.BO.LabourSubContractorBO;
import com.kakarote.build.labour.entity.PO.LabourSubcontractor;
import com.kakarote.build.labour.entity.PO.LabourWorktype;
import com.kakarote.build.labour.service.ILabourSubcontractorService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 劳务管理--分包商 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-03-15
 */
@RestController
@RequestMapping("/labourSubcontractor")
public class LabourSubcontractorController {

    @Autowired
    private ILabourSubcontractorService subcontractorService;

    @ApiOperation("获取分包商数据列表")
    @PostMapping("/list")
    @OperateLog(detail = "获取分包商数据列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> list(@RequestBody LabourSubContractorBO subContractorBO) {
        BasePage<Map<String, Object>> list = subcontractorService.selectList(subContractorBO);
        return Result.ok(list);
    }

    @PostMapping("/queryById/{id}")
    @ApiOperation("根据主键查询分包商信息")
    @OperateLog(detail = "根据主键查询分包商信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryById(@PathVariable @NotNull Integer id){
        LambdaQueryWrapper<LabourSubcontractor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.ne(LabourSubcontractor::getProjectId, 0);
        queryWrapper.eq(LabourSubcontractor::getId, id);
        return R.ok(subcontractorService.getOne(queryWrapper));
    }

    @PostMapping("/add")
    @ApiOperation("新增分包商信息")
    @OperateLog(detail = "根据主键查询分包商信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result add(@Validated @RequestBody LabourSubcontractor subcontractor) {
        return Result.ok(subcontractorService.save(subcontractor));
    }

    @PostMapping("/edit")
    @ApiOperation("修改分包商信息")
    @OperateLog(detail = "修改分包商信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody LabourSubcontractor subcontractor) {
        return Result.ok(subcontractorService.updateById(subcontractor));
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除分包商信息")
    @OperateLog(detail = "删除分包商信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@PathVariable Integer id) {
        return Result.ok(subcontractorService.removeByIdNew(id));
    }

    @ApiOperation(value = "查询所有分包商的id和name")
    @RequestMapping("/selcetIdsAndName")
    @OperateLog(detail = "查询所有分包商的id和name", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<LabourSubcontractor>> selcetIdsAndName(){
        List<LabourSubcontractor> list = subcontractorService.selcetIdsAndName();
        return Result.ok(list);
    }
}

