package com.kakarote.build.equipment.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataBO;
import com.kakarote.build.equipment.entity.BO.EquipmentMonitorDataEchartsBO;
import com.kakarote.build.equipment.entity.PO.EquipmentAssets;
import com.kakarote.build.equipment.entity.PO.EquipmentMonitorData;
import com.kakarote.build.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.build.equipment.service.IEquipmentMonitorDataService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备监测-监测项历史数据表 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/equipmentMonitorData")
public class EquipmentMonitorDataController {

    @Autowired
    private IEquipmentMonitorDataService monitorDataService;

    @PostMapping("/list")
    @ApiOperation("查询监测项历史数据")
    @OperateLog(detail = "查询监测项历史数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> list(@RequestBody EquipmentMonitorDataBO monitorDataBO) {
        return Result.ok(monitorDataService.selectByPage(monitorDataBO));
    }

    /**
     * 去除项目过滤时使用，返回项目名称
     * @param monitorDataBO
     * @return
     */
    @PostMapping("/selectListNew")
    @ApiOperation("查询监测项历史数据")
    @OperateLog(detail = "查询监测项历史数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> selectListNew(@RequestBody EquipmentMonitorDataBO monitorDataBO) {
        return Result.ok(monitorDataService.selectListNew(monitorDataBO));
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入监测项历史数据")
    @OperateLog(detail = "excel导入监测项历史数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = monitorDataService.excelImport(file);
        Integer errSize = object.getInteger("errSize");
        if (errSize > 0) {
            String msg = object.getString("message");
            return R.error(500, msg);
        } else {
            return R.ok(object);
        }
    }

    @PostMapping("/selectInfo")
    @ApiOperation("基坑监测历史数据曲线")
    @OperateLog(detail = "基坑监测历史数据曲线", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectInfo(@RequestBody Map<String,Object>map) {
        Map<String, Object> maps = monitorDataService.selectInfo(map);
        return R.ok(maps);
    }

    @PostMapping("/appList")
    @ApiOperation("查询监测点列表")
    @OperateLog(detail = "查询监测点列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> appList(@RequestBody EquipmentMonitorDataBO monitorDataBO) {
        return Result.ok(monitorDataService.selectAppListByPage(monitorDataBO));
    }

//    @PostMapping("/listByProject")
//    @ApiOperation("查询环境监测历史数据")
//    @OperateLog(detail = "查询环境监测历史数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
//    public Result<BasePage<Map<String,Object>>> listByProject(@RequestBody EquipmentMonitorDataBO monitorDataBO) {
//        return Result.ok(monitorDataService.selectListByProject(monitorDataBO));
//    }

    @PostMapping("/selectMonitorDataList")
    @ApiOperation("查询监测数据")
    @OperateLog(detail = "查询监测数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> selectMonitorDataList(@RequestBody EquipmentMonitorDataBO monitorDataBO) {
        return Result.ok(monitorDataService.selectMonitorDataList(monitorDataBO));
    }

    @PostMapping("/selectMonitorDataListEcharts")
    @ApiOperation("查询监测数据-曲线图")
    @OperateLog(detail = "查询监测数据-曲线图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String,Object>>> selectMonitorDataListEcharts(@RequestBody EquipmentMonitorDataEchartsBO monitorDataEchartsBO) {
        return Result.ok(monitorDataService.selectMonitorDataListEcharts(monitorDataEchartsBO));
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("查询监测数据详情")
    @OperateLog(detail = "查询监测点警报详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<EquipmentMonitorData> selectById(@PathVariable Integer id){
        return Result.ok(monitorDataService.getById(id));
    }

    @PostMapping("/save")
    @ApiOperation("保存或更新监测数据")
    @OperateLog(detail = "保存或更新监测数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody EquipmentMonitorData monitorData) {

        if (monitorData.getId() == null) {
            monitorDataService.save(monitorData);
        } else {
            monitorData.setUpdateUserId(UserUtil.getUserId());
            monitorDataService.updateById(monitorData);
        }
        return Result.ok();
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除监测数据")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(detail = "删除设备资产详情", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@PathVariable Integer id) {
        return R.ok(monitorDataService.removeById(id));
    }

    @PostMapping(value = "/export")
    @ApiOperation("数据导出")
    @OperateLog(detail = "数据导出",types= LogTypes.BUILD ,behavior = LogBehavior.EXPORT)
    public void export(@RequestBody Map<String,Object> map, HttpServletResponse response)
    {
       // monitorDataService.export(String.valueOf(map.get("ids")),response);
    }

    @PostMapping("/realTimeDataZs")
    @ApiOperation("24小时实时数据（噪音、空气质量、温湿度）")
    @OperateLog(detail = "24实时数据（噪音、空气质量、温湿度）",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result realTimeDataZs(@RequestBody Map<String,Object> params){
        return  R.ok(monitorDataService.realTimeDataZs(params));
    }

    @PostMapping("/realTimeDataTemperature")
    @ApiOperation("一周内气温获取最高最低")
    @OperateLog(detail = "一周内气温获取最高最低",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result realTimeDataTemperature(@RequestBody Map<String,Object> params){
        return  R.ok(monitorDataService.realTimeDataTemperature(params));
    }

    @PostMapping("/realTimeDataAll")
    @ApiOperation("实时数据全量")
    @OperateLog(detail = "实时数据全量",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result realTimeDataAll(@RequestBody Map<String,Object> params){
        return  R.ok(monitorDataService.realTimeDataAll(params));
    }

	/**
    * @Description:获取塔机监测数据
    * @author shz
    * @date 2021-11-11 16:37
    * @param
    * @return
    */
    @PostMapping(value = "/getTowerCraneData/{id}")
    @ApiOperation("获取塔机监测数据")
    @OperateLog(detail = "获取塔机监测数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result getTowerCraneData(@PathVariable Integer id){
        return R.ok(monitorDataService.getTowerCraneData(id));
    }

    /**
     * @Description:获取设备监测数据
     * @param
     * @return
     */
    @PostMapping(value = "/getMDataByEq")
    @ApiOperation("获取设备监测数据")
    @OperateLog(detail = "获取设备监测数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result getMDataByEq(@RequestBody EquipmentMonitorDataBO dataBO){
        return monitorDataService.getMDataByEq(dataBO);
    }

}

