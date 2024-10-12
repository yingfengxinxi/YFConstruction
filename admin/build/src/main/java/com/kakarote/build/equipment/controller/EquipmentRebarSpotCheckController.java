package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentRebarSpotCheckBO;
import com.kakarote.build.equipment.entity.PO.EquipmentRebarSpotCheck;
import com.kakarote.build.equipment.service.IEquipmentRebarSpotCheckService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 钢筋智能点检功能开发 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-02-14
 */
@RestController
@RequestMapping("/equipmentRebarSpotCheck")
public class EquipmentRebarSpotCheckController {

    @Autowired
    private IEquipmentRebarSpotCheckService iEquipmentRebarSpotCheckService;

    @PostMapping("/list")
    @ApiOperation("查询钢筋智能点检数据")
    @OperateLog(detail = "查询钢筋智能点检数据", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentRebarSpotCheck>> list(@RequestBody EquipmentRebarSpotCheckBO equipmentRebarSpotCheckBO) {
        return Result.ok(iEquipmentRebarSpotCheckService.selectByPage(equipmentRebarSpotCheckBO));
    }

    /**
     * 监管端查询接口
     * @param equipmentRebarSpotCheckBO
     * @return
     */
    @PostMapping("/selectProList")
    @ApiOperation("监管端——查询钢筋智能点检数据")
    @OperateLog(detail = "监管端——查询钢筋智能点检数据", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentRebarSpotCheck>> selectProList(@RequestBody EquipmentRebarSpotCheckBO equipmentRebarSpotCheckBO) {
        return Result.ok(iEquipmentRebarSpotCheckService.selectProList(equipmentRebarSpotCheckBO));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增智能安全帽数据")
    @OperateLog(detail = "新增智能安全帽数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveOrUpdate(@RequestBody EquipmentRebarSpotCheck equipmentRebarSpotCheck) {
        if (equipmentRebarSpotCheck.getId() == null) {
            equipmentRebarSpotCheck.setCreateTime(new Date());
            iEquipmentRebarSpotCheckService.save(equipmentRebarSpotCheck);
        } else {
            equipmentRebarSpotCheck.setUpdateTime(new Date());
            iEquipmentRebarSpotCheckService.updateById(equipmentRebarSpotCheck);
        }
        return Result.ok();
    }

    @GetMapping(value = "/selectById/{id}")
    @ApiOperation("查询单条数据详细")
    @OperateLog(detail = "查询单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable String id)
    {
        return Result.ok(iEquipmentRebarSpotCheckService.getById(id));
    }

    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除数据详情")
    @OperateLog(detail = "删除数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable Integer id) {
        return Result.ok(iEquipmentRebarSpotCheckService.removeById(id));
    }

}

