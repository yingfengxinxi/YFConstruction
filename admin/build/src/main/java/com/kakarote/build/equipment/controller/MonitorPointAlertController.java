package com.kakarote.build.equipment.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.equipment.entity.BO.MonitorPointAlertBO;
import com.kakarote.build.equipment.entity.PO.MonitorPointAlert;
import com.kakarote.build.equipment.service.IMonitorPointAlertService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 监测点警报 前端控制器
 * </p>
 *
 * @author shao
 * @since 2021-05-20
 */
@RestController
@RequestMapping("/monitorPointAlert")
public class MonitorPointAlertController {

    @Autowired
    private IMonitorPointAlertService monitorPointAlertService;

    @PostMapping("/selectPage")
    @ApiOperation("查询监测点警报列表")
    @OperateLog(detail = "查询监测点警报列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<MonitorPointAlert>> selectPage(@RequestBody MonitorPointAlertBO monitorPointAlertBO) {
        return Result.ok(monitorPointAlertService.selectPage(monitorPointAlertBO));
    }


    @PostMapping("/selectById/{id}")
    @ApiOperation("查询监测点警报详情")
    @OperateLog(detail = "查询监测点警报详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<MonitorPointAlert> selectById(@PathVariable Integer id) {
        return Result.ok(monitorPointAlertService.selectById(id));
    }

    @PostMapping("/selectByIdInApp/{id}")
    @ApiOperation("查询监测点警报详情")
    @OperateLog(detail = "查询监测点警报详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<MonitorPointAlert> selectByIdInApp(@PathVariable Integer id) {
        return Result.ok(monitorPointAlertService.selectByIdInApp(id));
    }

    @PostMapping("/handleSave")
    @ApiOperation("报警处理详情保存")
    @OperateLog(detail = "报警处理详情保存", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result handleSave(@RequestBody MonitorPointAlert monitorPointAlert) {
        return Result.ok(monitorPointAlertService.handleSave(monitorPointAlert));
    }

    @PostMapping("/selectPageByEnv")
    @ApiOperation("查询环境报警数据列表")
    @OperateLog(detail = "查询环境报警数据列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<MonitorPointAlert>> selectPageByEnv(@RequestBody MonitorPointAlertBO monitorPointAlertBO) {
        return Result.ok(monitorPointAlertService.selectPageByEnv(monitorPointAlertBO));
    }
    /**
     * @description
     * @author wnj58
     * @Params [params]
     * @updateTime 2022/4/15 14:53 查全部报警数据修改为，可查单一监测类型的报警数据
     * @return com.kakarote.core.common.Result<java.util.Map<java.lang.String,java.lang.Object>>
     * @throws
     */
    @PostMapping("/selectAlertNumber")
    @ApiOperation("报警数据分析-查询报警数量")
    @OperateLog(detail = "报警数据分析-查询报警数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectAlertNumber(@RequestBody  Map<String,Object> params) {
        return Result.ok(monitorPointAlertService.selectAlertNumber(params));
    }

    /**
     * 该接口宁津相关项目使用
     *
     * @return
     */
    @PostMapping("/selectAlertList")
    @ApiOperation("报警数据分析-报警列表")
    @OperateLog(detail = "报警数据分析-报警列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAlertList() {
        return Result.ok(monitorPointAlertService.selectAlertList());
    }

    @PostMapping("/selectTypeProportion")
    @ApiOperation("报警数据分析-报警类型占比")
    @OperateLog(detail = "报警数据分析-报警类型占比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectTypeProportion() {
        return Result.ok(monitorPointAlertService.selectTypeProportion());
    }

    @PostMapping("/selectAlertTrend")
    @ApiOperation("报警数据分析-报警趋势")
    @OperateLog(detail = "报警数据分析-报警趋势", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAlertTrend(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectAlertTrend(params));
    }

    /**
     * 该接口整合 AI危险源识别 和 车辆智能分析 报警数据 宁津项目不可用
     * @param params
     * @return
     */
    @PostMapping("/selectAlertTrendNew")
    @ApiOperation("报警数据分析-报警趋势")
    @OperateLog(detail = "报警数据分析-报警趋势", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAlertTrendNew(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectAlertTrendNew(params));
    }

    @PostMapping("/selectRaiseDustAlert")
    @ApiOperation("报警数据分析-扬尘报警多发报警时段")
    @OperateLog(detail = "报警数据分析-扬尘报警多发报警时段", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectRaiseDustAlert(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectRaiseDustAlert(params));
    }

    /**
     * type 1：颗粒物最严重前5名 2：颗粒物最优秀前5名
     *
     * @param params
     * @return
     */
    @PostMapping("/selectPMRank")
    @ApiOperation("报警数据分析-PM10排名")
    @OperateLog(detail = "报警数据分析PM10排名", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectPMRank(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectPMRank(params));
    }

    @PostMapping("/selectProjectAlertNumber")
    @ApiOperation("报警数据分析-近一个月各项目报警数对比")
    @OperateLog(detail = "报警数据分析-近一个月各项目报警数对比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectProjectAlertNumber(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectProjectAlertNumber(params));
    }

    /**
     * 新版报警数据查询报警数据表和 AI识别报警表
     * 同时如果需要根据监测项查询，关联通过设备类型查询监测项
     * 宁津项目使用该接口，设备监测项可能未绑定，导致查不出数据
     * @param params
     * @return
     */
    @PostMapping("/selectProjectAlertNumberNew")
    @ApiOperation("报警数据分析-近一个月各项目报警数对比")
    @OperateLog(detail = "报警数据分析-近一个月各项目报警数对比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectProjectAlertNumberNew(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectProjectAlertNumberNew(params));
    }

    @PostMapping("/selectNoiseAlert")
    @ApiOperation("报警数据分析-噪音报警多发报警时段")
    @OperateLog(detail = "报警数据分析-噪音报警多发报警时段", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectNoiseAlert(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectNoiseAlert(params));
    }

    @PostMapping("/selectAlarmPage")
    @ApiOperation("按项目分时段查询报警列表")
    @OperateLog(detail = "按项目分时段查询报警列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<MonitorPointAlert>> selectAlarmPage(@RequestBody MonitorPointAlertBO monitorPointAlertBO) {
        return Result.ok(monitorPointAlertService.selectAlarmPage(monitorPointAlertBO));
    }

    /**
     * 该接口整合 AI危险源识别 和 车辆智能分析 报警数据 宁津项目不可用
     * @param monitorPointAlertBO
     * @return
     */
    @PostMapping("/selectAlarmPageNew")
    @ApiOperation("按项目分时段查询报警列表")
    @OperateLog(detail = "按项目分时段查询报警列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<MonitorPointAlert>> selectAlarmPageNew(@RequestBody MonitorPointAlertBO monitorPointAlertBO) {
        return Result.ok(monitorPointAlertService.selectAlarmPageNew(monitorPointAlertBO));
    }

    /**
     * 根据监测项类型主类型查询
     *
     * @return
     */
    @PostMapping("/selectTypeProportionByParent")
    @ApiOperation("报警数据分析-报警类型占比-查询监测类型主类型")
    @OperateLog(detail = "报警数据分析-报警类型占比-查询监测类型主类型", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectTypeProportionByParent(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectTypeProportionByParent(params));
    }

    /**
     * 该接口智慧工地使用
     *
     * @return
     */
    @PostMapping("/selectAlertListNew")
    @ApiOperation("报警数据分析-报警列表")
    @OperateLog(detail = "报警数据分析-报警列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAlertListNew() {
        return Result.ok(monitorPointAlertService.selectAlertListNew());
    }

    /**
     * 基坑/高支模 查询报警统计（近七日柱状图）
     */
    /**
     * 该接口智慧工地使用
     *
     * @return
     */
    @GetMapping("/getCountBarByPro/{proId}")
    @ApiOperation("坑/高支模 查询报警统计（近七日柱状图）")
    @OperateLog(detail = "坑/高支模 查询报警统计（近七日柱状图）", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<JSONObject > getCountBarByPro(@PathVariable String proId) {
        return Result.ok(monitorPointAlertService.getCountBarByPro(proId));
    }

    /**
     * 基坑/高支模 查询报警统计（累计扇形占比）
     */
    /**
     * 该接口智慧工地使用
     *
     * @return
     */
    @GetMapping("/getCountPieByPro/{proId}")
    @ApiOperation("坑/高支模 查询报警统计（累计扇形占比）")
    @OperateLog(detail = "坑/高支模 查询报警统计（累计扇形占比）", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>> > getCountPieByPro(@PathVariable String proId) {
        return Result.ok(monitorPointAlertService.getCountPieByPro(proId));
    }

    /**
     * 智能工地监测数据报告接口
     * @return
     */
    @PostMapping("/selectMonitorDataReport")
    @ApiOperation("查询智能工地监测数据报告")
    @OperateLog(detail = "查询智能工地监测数据报告", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectMonitorDataReport(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectMonitorDataReport(params));
    }
    /**
     * @description
     * @author wnj58
     * @Params [params]
     * @updateTime 2022/3/9 14:27
     * @return com.kakarote.core.common.Result<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     * @throws
     */
    @PostMapping("/selectMonitorDataStatistics")
    @ApiOperation("查询扬尘报警、预警数量，宁津小程序使用")
    @OperateLog(detail = "查询扬尘报警、预警数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectMonitorDataStatistics(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectMonitorDataStatistics(params));
    }
    /**
     * @description
     * @author wnj58
     * @Params [params]
     * @updateTime 2022/3/15 14:42
     * @return com.kakarote.core.common.Result<java.util.List<java.util.Map<java.lang.String,java.lang.Object>>>
     * @throws
     */
    @PostMapping("/selectDustAlarmNum")
    @ApiOperation("查询扬尘超标、恒值、离线次数，宁津小程序使用")
    @OperateLog(detail = "查询扬尘报警、预警数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectDustAlarmNum(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectDustAlarmNum(params));
    }

    /**
     * 查询塔机、升降机、烟感、AI识别的报警数和 总数 该接口关联三维模型、未使用三维模型的项目不可用
     * @return
     */
    @PostMapping("/selectNumber")
    @ApiOperation("查询塔机、升降机、烟感、AI识别的报警数和 总数")
    @OperateLog(detail = "查询塔机、升降机、烟感、AI识别的报警数和 总数", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectNumber(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectNumber(params));
    }

    /**
     * 查询PM2.5、PM10、噪音的状态
     * @return
     */
    @PostMapping("/selectState")
    @ApiOperation("查询PM2.5、PM10、噪音的状态")
    @OperateLog(detail = "查询PM2.5、PM10、噪音的状态", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectState() {
        return Result.ok(monitorPointAlertService.selectState());
    }

    /**
     * 查询未处理报警数据
     * @return
     */
    @PostMapping("/selectUntreatedDataList")
    @ApiOperation("查询未处理报警数据")
    @OperateLog(detail = "查询未处理报警数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectUntreatedDataList(@RequestBody MonitorPointAlertBO monitorPointAlertBO) {
        return Result.ok(monitorPointAlertService.selectUntreatedDataList(monitorPointAlertBO));
    }
    /**
     * @description
     * @author wnj58
     * @Params [monitorPointAlertBO]
     * @updateTime 2022/4/21 17:09
     * @return com.kakarote.core.common.Result
     * @throws
     */
    @PostMapping("/selectDataList")
    @ApiOperation("查询未处理报警数据")
    @OperateLog(detail = "查询未处理报警数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectDataList(@RequestBody MonitorPointAlertBO monitorPointAlertBO) {
        return Result.ok(monitorPointAlertService.selectDataList(monitorPointAlertBO));
    }
    /**
     * @description
     * @author wnj58
     * @Params [monitorPointAlertBO]
     * @updateTime 2022/4/21 17:10
     * @return com.kakarote.core.common.Result
     * @throws
     */
    @PostMapping("/selectMonitorDataList")
    @ApiOperation("查询监控点报警数据")
    @OperateLog(detail = "查询监控点报警数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectMonitorDataList(@RequestBody MonitorPointAlertBO monitorPointAlertBO) {
        return Result.ok(monitorPointAlertService.selectMonitorDataList(monitorPointAlertBO));
    }
    /**
     * 查询预警、报警数量
     * @return
     */
    @PostMapping("/selectEarlyAndAlarmNumber")
    @ApiOperation("查询预警、报警数量")
    @OperateLog(detail = "查询预警、报警数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectEarlyAndAlarmNumber(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectEarlyAndAlarmNumber(params));
    }

    /**
     * 基坑/高支模 查询报警统计（近七日柱状图）
     */
    /**
     * 该接口三维大屏使用
     *
     * @return
     */
    @GetMapping("/getCountBarByEquiId/{equipmentId}")
    @ApiOperation("坑/高支模 查询报警统计（近七日柱状图）")
    @OperateLog(detail = "坑/高支模 查询报警统计（近七日柱状图）", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<JSONObject > getCountBarByEquiId(@PathVariable Integer equipmentId) {
        return Result.ok(monitorPointAlertService.getCountBarByEquiId(equipmentId));
    }



    /**
     * 基坑/高支模 查询报警统计（累计扇形占比）
     */
    /**
     * 该接口三维大屏使用
     *
     * @return
     */
    @GetMapping("/getCountPieByEquiId/{equipmentId}/{type}")
    @ApiOperation("坑/高支模 查询报警统计（累计扇形占比）")
    @OperateLog(detail = "坑/高支模 查询报警统计（累计扇形占比）", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>> > getCountPieByEquiId(@PathVariable("equipmentId") Integer equipmentId, @PathVariable("type") String type) {
        return Result.ok(monitorPointAlertService.getCountPieByEquiId(equipmentId,type));
    }

    /**
     * 根据设备id查询 未处理报警数据
     * @return
     */
    @PostMapping("/selectAlertListByEquiId")
    @ApiOperation("根据设备id查询未处理报警数据")
    @OperateLog(detail = "根据设备id查询未处理报警数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAlertListByEquiId(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectAlertListByEquiId(params));
    }
	    /**
     * 累计报警占比 查询报警统计（累计扇形占比）
     */
    /**
     * 该接口三维大屏使用
     * 2022-04-15 新增大屏报警数据总量统计查询
     * @return
     */
    @GetMapping("/getCountPieByScreenPolice")
    @ApiOperation("累计报警占比 查询报警统计（累计扇形占比）")
    @OperateLog(detail = "累计报警占比 查询报警统计（累计扇形占比）", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>> > getCountPieByScreenPolice() {
        return Result.ok(monitorPointAlertService.getCountPieByScreenPolice());
    }


    /**
     * 报警处理效率统计查询
     */
    /**
     * 该接口三维大屏使用
     * 2022-04-15 新增大屏报警处理效率统计查询
     * @return
     */
    @GetMapping("/getCountByPoliceEfficiency")
    @ApiOperation("报警处理效率统计查询")
    @OperateLog(detail = "报警处理效率统计查询", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object> > getCountByPoliceEfficiency() {
        return Result.ok(monitorPointAlertService.getCountByPoliceEfficiency());
    }

    /**
     * 查询PM2.5、PM10、噪音的数据列表
     * @return
     * 该接口三维大屏使用
     * 2022-04-18 新增查询PM2.5、PM10、噪音的数据列表
     *  @return
     */
    @PostMapping("/selectStateList")
    @ApiOperation("查询PM2.5、PM10、噪音的数据列表")
    @OperateLog(detail = "查询PM2.5、PM10、噪音的数据列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectStateList(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectStateList(params));
    }

    /**
     * 查询当日报警或预警列表 宁静项目不可用
     * @param monitorPointAlertBO
     * @return
     */
    @PostMapping("/selectPageByDay")
    @ApiOperation("查询当日报警列表")
    @OperateLog(detail = "查询当日报警列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> selectPageByDay(@RequestBody MonitorPointAlertBO monitorPointAlertBO) {
        return Result.ok(monitorPointAlertService.selectPageByDay(monitorPointAlertBO));
    }

    @PostMapping("/selectAllAlert")
    @ApiOperation("报警数据分析-报警多发报警时段")
    @OperateLog(detail = "报警数据分析-报警多发报警时段", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAllAlert(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectAllAlert(params));
    }

    /**
     * 枣庄危大工程报警数据列表
     *
     * @return
     */
    @PostMapping("/selectAlertListZz")
    @ApiOperation("枣庄危大工程报警数据列表")
    @OperateLog(detail = "枣庄危大工程报警数据列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAlertListZz() {
        return Result.ok(monitorPointAlertService.selectAlertListZz());
    }

    @PostMapping("/selectProjectAlertNumberAll")
    @ApiOperation("枣庄危大工程报警数据分析-近一个月各项目报警数对比")
    @OperateLog(detail = "枣庄危大工程报警数据分析-近一个月各项目报警数对比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectProjectAlertNumberAll(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectProjectAlertNumberAll(params));
    }

    @PostMapping("/selectProjectAlertNumberTop10")
    @ApiOperation("危大工程报警数据分析-近一个月报警数前10")
    @OperateLog(detail = "危大工程报警数据分析-近一个月报警数前10", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectProjectAlertNumberTop10(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectProjectAlertNumberTop10(params));
    }

    /**
     * 宁津项目有使用
     * 根据监测类型查询设备当日、累计、预警、报警数量
     * @param params
     * @return
     */
    @PostMapping("/selectAlarmNumByMonitorType")
    @ApiOperation("根据监测类型查询设备当日、累计、预警、报警数量")
    @OperateLog(detail = "根据监测类型、查询设备当日、累计、预警、报警数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectAlarmNumByMonitorType(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectAlarmNumByMonitorType(params));
    }

    /**
     * 宁津项目有使用
     * 根据监测类型查询接入工地、设备数量
     * @param params
     * @return
     */
    @PostMapping("/selectProjectAndEquiNum")
    @ApiOperation("根据监测类型查询接入工地、设备数量")
    @OperateLog(detail = "根据监测类型查询接入工地、设备数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectProjectAndEquiNum(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectProjectAndEquiNum(params));
    }

    /**
     * 宁津项目有使用
     * 根据监测类型查询工地报警数排名
     * @param params
     * @return
     */
    @PostMapping("/selectProejctNumberRank")
    @ApiOperation("根据监测类型查询工地报警数排名")
    @OperateLog(detail = "根据监测类型查询工地报警数排名", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectProejctNumberRank(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectProejctNumberRank(params));
    }

    @PostMapping("/selectEquipmentNumberRank")
    @ApiOperation("根据监测类型查询设备报警数排名")
    @OperateLog(detail = "根据监测类型查询设备报警数排名", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectEquipmentNumberRank(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectEquipmentNumberRank(params));
    }
    /**
     * 监管端 报警数据
     * @param params
     * @return
     */
    @PostMapping("/selectAlarmListByPro")
    @ApiOperation("监管端-报警数据")
    @OperateLog(detail = "监管端-报警数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectAlarmListByPro(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectAlarmListByPro(params));
    }

    /**
     * 查询实时报警列表数据
     * @return
     */
    @PostMapping("/selectRealTimeDataList")
    @ApiOperation("查询未处理报警数据")
    @OperateLog(detail = "查询未处理报警数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String, Object>>> selectRealTimeDataList(@RequestBody MonitorPointAlertBO monitorPointAlertBO) {
        return Result.ok(monitorPointAlertService.selectRealTimeDataList(monitorPointAlertBO));
    }

    @PostMapping("/selectMonitorTypeAlertNumber")
    @ApiOperation("近30天各监测类型报警数量")
    @OperateLog(detail = "近30天各监测类型报警数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectMonitorTypeAlertNumber(@RequestBody Map<String, Object> params) {
        return Result.ok(monitorPointAlertService.selectMonitorTypeAlertNumber(params));
    }

    /**
     * 该接口智慧工地使用 统一查询所有报警表 查询b_alert_view视图
     *
     * @return
     */
    @PostMapping("/selectFullAlarmPage")
    @ApiOperation("按项目分时段查询报警列表")
    @OperateLog(detail = "按项目分时段查询报警列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<MonitorPointAlert>> selectFullAlarmPage(@RequestBody MonitorPointAlertBO monitorPointAlertBO) {
        return Result.ok(monitorPointAlertService.selectFullAlarmPage(monitorPointAlertBO));
    }
}

