package com.kakarote.build.complex.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.complex.entity.BO.ComplexMaterialsInBO;
import com.kakarote.build.complex.entity.PO.ComplexMaterialsIn;
import com.kakarote.build.complex.service.IComplexMaterialsInService;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
import com.kakarote.build.wifi.entity.PO.WifiQuestOption;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工综合--物料进场 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-06-16
 */
@RestController
@RequestMapping("/complexMaterialsIn")
public class ComplexMaterialsInController {
    @Autowired
    private IComplexMaterialsInService iComplexMaterialsInService;

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> list(@RequestBody ComplexMaterialsInBO complexMaterialsInBO) {
        BasePage<Map<String,Object>> list = iComplexMaterialsInService.selectList(complexMaterialsInBO);
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody ComplexMaterialsIn complexMaterialsIn) {

        iComplexMaterialsInService.save(complexMaterialsIn);
        return Result.ok(complexMaterialsIn.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody ComplexMaterialsIn complexMaterialsIn) {
        boolean b = iComplexMaterialsInService.updateById(complexMaterialsIn);
        return Result.ok(complexMaterialsIn.getId());
    }


    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable Integer id){
        iComplexMaterialsInService.removeById(id);
        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable Integer id){
        ComplexMaterialsIn byId = iComplexMaterialsInService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("/selectIdAndCodeLists")
    @ApiOperation("查询进场物料的id和code")
    @OperateLog(detail = "查询进场物料的id和code",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectIdAndCodeLists(){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("id","code");
        List list = iComplexMaterialsInService.getBaseMapper().selectMaps(queryWrapper);

        return Result.ok(list);
    }

}

