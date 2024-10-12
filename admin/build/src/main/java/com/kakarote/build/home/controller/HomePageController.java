package com.kakarote.build.home.controller;/**
 * @ClassName HomePageController
 * @Author wnj58
 * @Date 2021/9/16 16:46
 * @purpose
 * @Version 1.0
 **/

import com.kakarote.build.equipment.entity.BO.EquipmentAssetsBO;
import com.kakarote.build.home.service.HomePageService;
import com.kakarote.core.common.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 *@ClassName HomePageController
 *@Author wnj58
 *@Date 2021/9/16 16:46
 *@purpose
 *@Version 1.0
 **/
@RestController
@RequestMapping("/homePage")
public class HomePageController {
    @Autowired
    private HomePageService homePageService;

    @RequestMapping("/envMonitPont")
    @ApiOperation("环境监测点统计")
    @OperateLog(detail = "环境监测点统计",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result envMonitPont(){
       return  R.ok(homePageService.EnvMonitPont());
    }

    @RequestMapping("/equipmentNum")
    @ApiOperation("设备数统计")
    @OperateLog(detail = "设备数统计",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result equipmentNum(){
        return  R.ok(homePageService.EquipmentNum());
    }

    @RequestMapping("/alarmsNum/{type}")
    @ApiOperation("总报警数,报警项目")
    @OperateLog(detail = ",报警项目",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result alarmsNum(@PathVariable Integer type){
        return  R.ok(homePageService.AlarmsNum(type));
    }

    @RequestMapping("/projectNum")
    @ApiOperation("接入项目")
    @OperateLog(detail = "接入项目",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result projectNum(){
        return  R.ok(homePageService.ProjectNum());
    }

    @RequestMapping("/earlyWarning")
    @ApiOperation("预警分类")
    @OperateLog(detail = "预警分类",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result earlyWarning(){
        return  R.ok(homePageService.EarlyWarning());
    }

    /**
     * 统计报警数和安全隐患数
     * @return
     */
    @RequestMapping("/projectAlarmNum")
    @ApiOperation("按项目分类统计报警数")
    @OperateLog(detail = "按项目分类统计报警数",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result projectAlarmNum(){
        return  R.ok(homePageService.ProjectAlarmNum());
    }

    @RequestMapping("/monitSituationNoise")
    @ApiOperation("噪音监控情况")
    @OperateLog(detail = "噪音监控情况",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result monitSituationNoise(@RequestBody Map<String, Object> params){
        return  R.ok(homePageService.MonitSituationNoise(params));
    }

    @RequestMapping("/monitSituationPM")
    @ApiOperation("监控情况")
    @OperateLog(detail = "监控情况",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result monitSituationPM(String type){
        return  R.ok(homePageService.MonitSituationPM(type));
    }

    @RequestMapping("/projectInfoList")
    @ApiOperation("项目详情")
    @OperateLog(detail = "项目详情",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result projectInfoList(){
        return  R.ok(homePageService.projectInfoList());
    }

    /**
    * @Description:监管首页地图数据查询
    * @author shz
    * @date 2022-1-7 14:57
    * @param 
    * @return 
    */
    @RequestMapping("/projectInfoListByProjectState")
    @ApiOperation("项目详情")
    @OperateLog(detail = "项目详情",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result projectInfoListByProjectState(@RequestBody Map<String,Object> params){
        return  R.ok(homePageService.projectInfoListByProjectState(params));
    }

    /**
     * nj 首页地图查询列表
     * @return
     */
    @RequestMapping("/projectInfoListNJ")
    @ApiOperation("项目详情")
    @OperateLog(detail = "项目详情",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result projectInfoListNJ(){
        return  R.ok(homePageService.projectInfoListNJ());
    }

    @RequestMapping("/selectProjectStateCount")
    @ApiOperation("项目进度分类占比")
    @OperateLog(detail = "项目进度分类占比",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectProjectStateCount(){
        return  R.ok(homePageService.selectProjectStateCount());
    }

    /**
    * @Description:接入设备数状态
    * @author shz
    * @date 2022-2-23 10:10
    * @param 
    * @return 
    */
    @RequestMapping("/getLine")
    @ApiOperation("接入设备数状态")
    @OperateLog(detail = "接入设备数状态",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result getLine(@RequestBody Map<String, Object> params){
        return  R.ok(homePageService.getLine(params));
    }

    /**
     * @Description:接入设备数状态
     * @author lzy
     * @date 2022-3-21 10:10
     * @param
     * @return
     */
    @RequestMapping("/getLineNew")
    @ApiOperation("接入设备数状态")
    @OperateLog(detail = "接入设备数状态",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result getLineNew(@RequestBody Map<String, Object> params){
        return  R.ok(homePageService.getLineNew(params));
    }

    /**
     * @Description:接入设备数状态 该接口只适合 需要管理三维模型的项目使用，未管理三维模型查询数量不可用！
     * @author lzy
     * @date 2022-3-21 10:10
     * @param
     * @return
     */
    @RequestMapping("/getLineByThreeDimensional")
    @ApiOperation("接入设备数状态")
    @OperateLog(detail = "接入设备数状态",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result getLineByThreeDimensional(@RequestBody Map<String, Object> params){
        return  R.ok(homePageService.getLineByThreeDimensional(params));
    }

    @RequestMapping("/dayAndNightAlarmNumber")
    @ApiOperation("三维大屏-日间夜间超标数")
    @OperateLog(detail = "三维大屏-日间夜间超标数",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result dayAndNightAlarmNumber(@RequestBody Map<String, Object> params){
        return  R.ok(homePageService.dayAndNightAlarmNumber(params));
    }

    /**
     * 查询设备的数据列表
     * @return
     * 该接口三维大屏使用
     * 2022-04-19  查询设备的数据列表
     *  @return
     */
    @PostMapping("/selectEquipmentList")
    @ApiOperation("查询设备的数据列表")
    @OperateLog(detail = "查询设备的数据列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Map<String, Object>> selectMonitoringList(@RequestBody Map<String, Object> params) {
        return Result.ok(homePageService.selectEquipmentList(params));
    }

    /**
     * 宁津项目不可用 查询今日预警数、今日报警数
     * @return
     */
    @RequestMapping("/alarmNumberByDay")
    @ApiOperation("查询今日预警数、今日报警数")
    @OperateLog(detail = "监管端-查询今日预警数、今日报警数",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result alarmNumberByDay(){
        return  R.ok(homePageService.alarmNumberByDay());
    }

    /**
     * 宁津项目不可用 统计报警数和安全隐患数
     * @return
     */
    @RequestMapping("/projectAlarmNumNew")
    @ApiOperation("按项目分类统计报警数")
    @OperateLog(detail = "按项目分类统计报警数",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result projectAlarmNumNew(){
        return  R.ok(homePageService.projectAlarmNumNew());
    }

    /**
     * @Description:监管首页地图数据查询 宁津项目不可用
     * @author lzy
     * @date 2022-5-6 14:57
     * @param
     * @return
     */
    @RequestMapping("/projectInfoListByAlertType")
    @ApiOperation("项目详情")
    @OperateLog(detail = "项目详情",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result projectInfoListByAlertType(@RequestBody Map<String,Object> params){
        return  R.ok(homePageService.projectInfoListByAlertType(params));
    }

    /**
     * 机械设备数接口 目前仅统计 塔机、升降机、基坑、高支模 如需改动，请同步更新注释！
     * @return
     */
    @RequestMapping("/machineEquNumber")
    @ApiOperation("机械设备数接口")
    @OperateLog(detail = "机械设备数接口",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result machineEquNumber(){
        return  R.ok(homePageService.machineEquNumber());
    }

    /**
     * 该接口监管大屏首页使用
     * 查询设备列表 selectType 1 环境设备 2机械设备 3视频设备 online 1 在线 0 离线
     * @return
     */
    @RequestMapping("/selectEquiListByType")
    @ApiOperation("查询对用类型的设备列表")
    @OperateLog(detail = "查询对用类型的设备列表",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectEquiListByType(@RequestBody EquipmentAssetsBO assetsBO){
        return  R.ok(homePageService.selectEquiListByType(assetsBO));
    }
}
