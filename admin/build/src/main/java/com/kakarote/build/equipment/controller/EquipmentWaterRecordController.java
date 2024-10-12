package com.kakarote.build.equipment.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.BO.EquipmentElectricityRecordBO;
import com.kakarote.build.equipment.entity.BO.EquipmentWaterRecordBO;
import com.kakarote.build.equipment.service.IEquipmentWaterRecordService;
import com.kakarote.core.common.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-06-21
 */
@RestController
@RequestMapping("/equipmentWaterRecord")
public class EquipmentWaterRecordController {

    @Autowired
    private IEquipmentWaterRecordService waterRecordService;

    @PostMapping("/list")
    @ApiOperation("查询设备用电报表")
    @OperateLog(detail = "查询设备资产列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> list(@RequestBody EquipmentWaterRecordBO waterRecordBO){
        Map<String, Object> list = waterRecordService.selectTableList(waterRecordBO);
        return Result.ok(list);
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入设备用水数据")
    @OperateLog(detail = "excel导入设备用水数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = waterRecordService.excelImport(file);
        Integer errSize = object.getInteger("errSize");
        if (errSize > 0) {
            String msg = object.getString("message");
            return R.error(500, msg);
        } else {
            return R.ok(object);
        }
    }

    /**
     * 监管端使用
     * @param waterRecordBO
     * @return
     */
    @PostMapping("/selectProList")
    @ApiOperation("查询设备用电报表")
    @OperateLog(detail = "查询设备资产列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectProList(@RequestBody EquipmentWaterRecordBO waterRecordBO){
        Map<String, Object> list = waterRecordService.selectProTableList(waterRecordBO);
        return Result.ok(list);
    }

}

