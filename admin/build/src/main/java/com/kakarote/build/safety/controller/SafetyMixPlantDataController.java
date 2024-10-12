package com.kakarote.build.safety.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.safety.entity.BO.SafetyMixPlantDataBO;
import com.kakarote.build.safety.entity.PO.SafetyMixPlantData;
import com.kakarote.build.safety.service.ISafetyMixPlantDataService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 拌合站监控基本信息 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-08-30
 */
@RestController
@RequestMapping("/safetyMixPlantData")
public class SafetyMixPlantDataController {

    @Autowired
    private ISafetyMixPlantDataService mixPlantDataService;

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result list(@RequestBody SafetyMixPlantDataBO safetyMixPlantDataBO) {
        BasePage<SafetyMixPlantData> list =  mixPlantDataService.selectByPage(safetyMixPlantDataBO);
        return Result.ok(list);
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入拌合站监控数据")
    @OperateLog(detail = "excel导入拌合站监控数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) throws Exception{
        JSONObject object = mixPlantDataService.excelImport(file);
        return R.ok(object);
    }

}

