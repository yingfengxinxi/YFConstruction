package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.EquipmentMonitorPointBO;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorPoint;
import com.kakarote.build.equipment.service.IEquipmentMonitorPointService;
import com.kakarote.build.qual.entity.BO.QualCheckBO;
import com.kakarote.build.qual.entity.PO.QualCheck;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测点设置 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-05-18
 */
@RestController
@RequestMapping("/equipmentMonitorPoint")
public class EquipmentMonitorPointController {

    @Autowired
    private IEquipmentMonitorPointService monitorPointService;


    @PostMapping("/list")
    @ApiOperation("查询监测点列表")
    @OperateLog(detail = "查询监测点列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentMonitorPoint>> list(@RequestBody EquipmentMonitorPointBO monitorPointBO) {
        return Result.ok( monitorPointService.selectByPage(monitorPointBO));
    }

    @PostMapping("/listAllProject")
    @ApiOperation("查询监测点列表")
    @OperateLog(detail = "查询监测点列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EquipmentMonitorPoint>> listAllProject(@RequestBody EquipmentMonitorPointBO monitorPointBO) {
        return Result.ok( monitorPointService.listAllProject(monitorPointBO));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新监测点信息")
    @OperateLog(detail = "保存或更新监测点信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody EquipmentMonitorPoint monitorPoint) {
        return Result.ok(monitorPointService.saveOrUpdate(monitorPoint));
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("根据Id查询监测点信息")
    @OperateLog(detail = "根据Id查询监测点信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable @NotNull Integer id) {
        EquipmentMonitorPoint monitorPoint = monitorPointService.getById(id);
        return Result.ok(monitorPoint);
    }

    /**
     * 删除质量检查信息
     */
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除监测点信息")
    @OperateLog(detail = "删除监测点信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            // 根据checkId删除隐患信息
            monitorPointService.removeById(id);
        }
        return Result.ok();
    }

    @PostMapping("/selectPointsInfo")
    @ApiOperation("基坑监测历史数据曲线--监测点查询")
    @OperateLog(detail = "基坑监测历史数据曲线", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map>> selectPointsInfo(@RequestBody Map<String,Object>map) {
        List<Map> maps = monitorPointService.selectPointsInfo(map);
        return R.ok(maps);
    }

    /**
     * app 自动喷淋控制器使用
     * @param
     * @return
     */
    @PostMapping("/selectRaiseDustData")
    @ApiOperation("查询扬尘监测数据")
    @OperateLog(detail = "查询扬尘监测数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectRaiseDustData() {
        List<Map<String, Object>> list = monitorPointService.selectRaiseDustData();
        return R.ok(list);
    }

    /**
     * 根据数据type查询所有监测点信息
     *
     * @param type 数据主键
     * @return
     */
    @GetMapping(value = "/getMonitorList/{type}")
    @ApiOperation("查询单条数据详细")
    @OperateLog(detail = "查询单条数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getMonitorList(@PathVariable String type)
    {

        return Result.ok(monitorPointService.getMonitorList(type))
                ;
    }
    /**
     * 根据数据type查询所有监测点信息
     *
     * @param assetId 设备主键
     * @return
     */
    @GetMapping(value = "/getPositionListByAssetId/{assetId}")
    @ApiOperation("根据设备获取有效点位  关联实时数据")
    @OperateLog(detail = "根据设备获取有效点位  关联实时数据",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getPositionListByAssetId(@PathVariable String assetId)
    {

        return monitorPointService.getPointAndAtvalueByAssetId(assetId);
    }
}

