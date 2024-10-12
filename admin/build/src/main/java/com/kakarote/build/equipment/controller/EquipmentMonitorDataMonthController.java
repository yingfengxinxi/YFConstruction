package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataBO;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataEchartsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorData;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorDataMonth;
import com.kakarote.build.equipment.service.IEquipmentMonitorDataMonthService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项月数据表 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-10-28
 */
@RestController
@RequestMapping("/equipmentMonitorDataMonth")
public class EquipmentMonitorDataMonthController {

    @Autowired
    private IEquipmentMonitorDataMonthService monitorDataMonthService;



    @PostMapping("/selectMonitorDataListByMonth")
    @ApiOperation("查询监测数据")
    @OperateLog(detail = "查询监测数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> selectMonitorDataListByMonth(@RequestBody EquipmentMonitorDataBO monitorDataBO) {
        return Result.ok(monitorDataMonthService.selectMonitorDataListByMonth(monitorDataBO));
    }


    @PostMapping("/selectById/{id}")
    @ApiOperation("查询监测数据详情")
    @OperateLog(detail = "查询监测点警报详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<EquipmentMonitorDataMonth> selectById(@PathVariable Integer id){
        return Result.ok(monitorDataMonthService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation("保存或更新监测数据")
    @OperateLog(detail = "保存或更新监测数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody EquipmentMonitorDataMonth monitorDataMonth) {

        if (monitorDataMonth.getId() == null) {
            monitorDataMonthService.save(monitorDataMonth);
        } else {
            monitorDataMonthService.updateById(monitorDataMonth);
        }
        return Result.ok();
    }

    @PostMapping("/selectMonitorDataMonthEcharts")
    @ApiOperation("查询监测数据-曲线图")
    @OperateLog(detail = "查询监测数据-曲线图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String,Object>>> selectMonitorDataMonthEcharts(@RequestBody EquipmentMonitorDataEchartsBO monitorDataEchartsBO) {
        return Result.ok(monitorDataMonthService.selectMonitorDataMonthEcharts(monitorDataEchartsBO));
    }



    @PostMapping("/selectRecentData")
    @ApiOperation("根据类型查询最近7天PM2.5、PM10或噪音的数据")
    @OperateLog(detail = "根据类型查询最近7天PM2.5、PM10或噪音的数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String,Object>> selectRecentData(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorDataMonthService.selectRecentData(params));
    }

    //2022-06-20 日 环境历史监测新增
    @PostMapping("/airQualityMouth")
    @ApiOperation("根据日期获取当月的最好数据与最差数据")
    @OperateLog(detail = "根据日期获取当月的最好数据与最差数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String,Object>> airQualityMouth(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorDataMonthService.airQualityMouth(params));
    }

    //2022-06-24 日 环境历史监测年度新增
    @PostMapping("/yearDataStatistics")
    @ApiOperation("根据年度获取空气质量数据")
    @OperateLog(detail = "根据年度获取空气质量数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String,Object>> yearDataStatistics(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorDataMonthService.yearDataStatistics(params));
    }
}

