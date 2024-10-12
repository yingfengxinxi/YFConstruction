package com.kakarote.build.complex.controller;


import com.kakarote.build.complex.entity.BO.ComplexMaterialsInBO;
import com.kakarote.build.complex.entity.PO.ComplexMaterialsIn;
import com.kakarote.build.complex.entity.PO.ComplexMaterialsOut;
import com.kakarote.build.complex.service.IComplexMaterialsOutService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 施工综合--物料出场 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-06-16
 */
@RestController
@RequestMapping("/complexMaterialsOut")
public class ComplexMaterialsOutController {

    @Autowired
    private IComplexMaterialsOutService iComplexMaterialsOutService;

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> list(@RequestBody ComplexMaterialsInBO complexMaterialsInBO) {
        BasePage<Map<String,Object>> list = iComplexMaterialsOutService.selectList(complexMaterialsInBO);
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody ComplexMaterialsOut complexMaterialsOut) {

        iComplexMaterialsOutService.save(complexMaterialsOut);
        return Result.ok(complexMaterialsOut.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody ComplexMaterialsOut complexMaterialsOut) {
        boolean b = iComplexMaterialsOutService.updateById(complexMaterialsOut);
        return Result.ok(complexMaterialsOut.getId());
    }


    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable Integer id){
        iComplexMaterialsOutService.removeById(id);
        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable Integer id){
        ComplexMaterialsOut byId = iComplexMaterialsOutService.getById(id);
        return Result.ok(byId);
    }
    
}

