package com.kakarote.build.safety.controller;


import com.kakarote.build.safety.entity.PO.BSafetyDangerSource;
import com.kakarote.build.safety.entity.PO.SafetyProdRiskManacont;
import com.kakarote.build.safety.service.ISafetyProdRiskManacontService;
import com.kakarote.core.common.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 安全生产风险管控 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-04-02
 */
@RestController
@RequestMapping("/safetyProdRiskManacont")
public class SafetyProdRiskManacontController {

    @Autowired
    private ISafetyProdRiskManacontService iSafetyProdRiskManacontService;

    @GetMapping("/queryByParentId/{parentId}")
    @ApiOperation("根据上级查询安全生产风险管控")
    @OperateLog(detail = "根据上级查询安全生产风险管控", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<SafetyProdRiskManacont>> queryByParentId(@PathVariable("parentId") Integer parentId) {
        List<SafetyProdRiskManacont> list = iSafetyProdRiskManacontService.queryByParentId(parentId);
        return Result.ok(list);
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("获取安全生产风险管控详情")
    @OperateLog(detail = "获取安全生产风险管控详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Object> getMenuById(@PathVariable Integer id) {
        return Result.ok(iSafetyProdRiskManacontService.getById(id));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存安全生产风险管控信息")
    @OperateLog(detail = "保存安全生产风险管控信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result<Long> saveOrUpdateMessage(@RequestBody SafetyProdRiskManacont safetyProdRiskManacont) {
        iSafetyProdRiskManacontService.saveOrUpdate(safetyProdRiskManacont);
        return Result.ok();
    }

    @GetMapping("/del/{id}")
    @ApiOperation("删除安全生产风险管控")
    @OperateLog(detail = "删除安全生产风险管控", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result del(@PathVariable Integer id) {
        return R.ok(iSafetyProdRiskManacontService.removeListById(id));
    }

}

