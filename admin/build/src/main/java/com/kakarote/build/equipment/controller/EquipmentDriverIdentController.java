package com.kakarote.build.equipment.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.complex.entity.BO.ComplexMaterialsInBO;
import com.kakarote.build.complex.service.IComplexMaterialsInService;
import com.kakarote.build.equipment.entity.BO.EquipmentDriverIdentBO;
import com.kakarote.build.equipment.service.IEquipmentDriverIdentService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 机械设备司机识别 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-07-26
 */
@RestController
@RequestMapping("/equipmentDriverIdent")
public class EquipmentDriverIdentController {

    @Autowired
    private IEquipmentDriverIdentService iEquipmentDriverIdentService;

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> list(@RequestBody EquipmentDriverIdentBO equipmentDriverIdentBO) {
        BasePage<Map<String,Object>> list = iEquipmentDriverIdentService.selectList(equipmentDriverIdentBO);
        return Result.ok(list);
    }

    @PostMapping("/getByIdInApp/{id}")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String,Object>> getByIdInApp(@PathVariable Integer id) {
        return Result.ok(iEquipmentDriverIdentService.getByIdInApp(id));
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入周界防护报警")
    @OperateLog(detail = "excel导入周界防护报警", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) throws Exception{
        JSONObject object = iEquipmentDriverIdentService.excelImport(file);
        return R.ok(object);
    }

    /**
     * 该接口添加项目id作为查询条件，并且关联查询项目名称、编号
     * @param equipmentDriverIdentBO
     * @return
     */
    @PostMapping("/selectListNew")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> selectListNew(@RequestBody EquipmentDriverIdentBO equipmentDriverIdentBO) {
        BasePage<Map<String,Object>> list = iEquipmentDriverIdentService.selectListNew(equipmentDriverIdentBO);
        return Result.ok(list);
    }

    @PostMapping("/saveData")
    @ApiOperation("塔机实时数据上传接口")
    @OperateLog(detail = "塔机实时数据上传接口", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveData(@RequestBody List list) {
        return iEquipmentDriverIdentService.saveData(list);
    }

}

