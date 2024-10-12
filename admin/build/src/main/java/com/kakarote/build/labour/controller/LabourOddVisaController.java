package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.LabourOddVisaBO;
import com.kakarote.build.labour.entity.PO.LabourOddVisa;
import com.kakarote.build.labour.service.ILabourOddVisaService;
import com.kakarote.build.wifi.entity.BO.WifiVideoBO;
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

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 零工签证 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-06-07
 */
@RestController
@RequestMapping("/labourOddVisa")
public class LabourOddVisaController {
    @Autowired
    private ILabourOddVisaService iLabourOddVisaService;


    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> list(@RequestBody LabourOddVisaBO labourOddVisaBO) {
        BasePage<Map<String,Object>> list = iLabourOddVisaService.selectList(labourOddVisaBO);
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody LabourOddVisa labourOddVisa) {
        iLabourOddVisaService.save(labourOddVisa);
        return Result.ok(labourOddVisa.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody LabourOddVisa labourOddVisa) {
        iLabourOddVisaService.updateById(labourOddVisa);
        return Result.ok(labourOddVisa.getId());
    }


    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable Integer id){

        iLabourOddVisaService.removeById(id);

        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result<LabourOddVisa> selectById(@PathVariable Integer id){
        LabourOddVisa byId = iLabourOddVisaService.getById(id);
        return Result.ok(byId);
    }

    @PostMapping("/exportExcel")
    @ApiOperation("导出")
    @OperateLog(detail = "导出",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public void exportExcel(@RequestBody LabourOddVisaBO labourOddVisaBO, HttpServletResponse response){
        iLabourOddVisaService.exportExcel(labourOddVisaBO,response);
    }

}

