package com.kakarote.build.equipment.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.BO.EquipmentHydropowerAlarmBO;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorPointBO;
import com.kakarote.build.equipment.entity.PO.EquipmentHydropowerAlarm;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.build.equipment.service.IEquipmentHydropowerAlarmService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 水电报警 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-06-21
 */
@RestController
@RequestMapping("/equipmentHydropowerAlarm")
public class EquipmentHydropowerAlarmController {

    @Autowired
    private IEquipmentHydropowerAlarmService hydropowerAlarmService;

    @PostMapping("/list")
    @ApiOperation("查询监测点列表")
    @OperateLog(detail = "查询监测点列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentHydropowerAlarm>> list(@RequestBody EquipmentHydropowerAlarmBO hydropowerAlarmBO) {
        return Result.ok( hydropowerAlarmService.selectByPage(hydropowerAlarmBO));
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入水电报警数据")
    @OperateLog(detail = "excel导入水电报警数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = hydropowerAlarmService.excelImport(file);
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
     * @param hydropowerAlarmBO
     * @return
     */
    @PostMapping("/selectProList")
    @ApiOperation("查询监测点列表")
    @OperateLog(detail = "查询监测点列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentHydropowerAlarm>> selectProList(@RequestBody EquipmentHydropowerAlarmBO hydropowerAlarmBO) {
        return Result.ok( hydropowerAlarmService.selectByPageAndPro(hydropowerAlarmBO));
    }

}

