package com.kakarote.build.home.controller;/**
 * @ClassName HomePageController
 * @Author wnj58
 * @Date 2021/9/16 16:46
 * @purpose
 * @Version 1.0
 **/

import com.kakarote.build.home.service.ProjectAnalysisService;
import com.kakarote.build.project.entity.BO.ProjectInfoBO;
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
@RequestMapping("/projectAnalysis")
public class ProjectAnalysisController {
    @Autowired
    private ProjectAnalysisService projectAnalysisService;

    @RequestMapping("/projectList")
    @ApiOperation("项目列表")
    @OperateLog(detail = "项目列表",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result projectList(@RequestBody ProjectInfoBO projectInfoBO){
       return  R.ok(projectAnalysisService.projectList(projectInfoBO));
    }

    @RequestMapping("/projectInfoById/{id}")
    @ApiOperation("项目详情")
    @OperateLog(detail = "项目详情",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result projectInfoById(@PathVariable("id") Integer id){
        return  R.ok(projectAnalysisService.projectInfoById(id));
    }

    @PostMapping("/alarmsStatistics")
    @ApiOperation("按日、周、月、年统计当前项目报警数量")
    @OperateLog(detail = "按日、周、月、年统计当前项目报警数量",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result alarmsStatistics(@RequestBody Map<String,Object> params){
        return  R.ok(projectAnalysisService.alarmsStatistics(params));
    }

    /**
     * 该接口整合 AI危险源识别 和 车辆智能分析 报警数据 宁津项目不可用
     * @param params
     * @return
     */
    @PostMapping("/alarmsStatisticsNew")
    @ApiOperation("按日、周、月、年统计当前项目报警数量")
    @OperateLog(detail = "按日、周、月、年统计当前项目报警数量",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result alarmsStatisticsNew(@RequestBody Map<String,Object> params){
        return  R.ok(projectAnalysisService.alarmsStatisticsNew(params));
    }

    @PostMapping("/alarmsTrend")
    @ApiOperation("报警趋势")
    @OperateLog(detail = "报警趋势",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result alarmsTrend(@RequestBody Map<String,Object> params){
        return  R.ok(projectAnalysisService.alarmsTrend(params));
    }

    @PostMapping("/realTimeData")
    @ApiOperation("实时数据（噪音、空气质量、温湿度）")
    @OperateLog(detail = "实时数据（噪音、空气质量、温湿度）",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result realTimeData(@RequestBody Map<String,Object> params){
        return  R.ok(projectAnalysisService.realTimeData(params));
    }

    /**
     * 需传项目id
     * @param params
     * @return
     */
    @PostMapping("/hisAirQualityAnalysis")
    @ApiOperation("历史空气质量分析")
    @OperateLog(detail = "历史空气质量分析",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result hisAirQualityAnalysis(@RequestBody Map<String,Object> params){
        return  R.ok(projectAnalysisService.hisAirQualityAnalysis(params));
    }

    /**
     * 不传参
     * @return
     */
    @PostMapping("/hisAirQualityAnalysisNew")
    @ApiOperation("历史空气质量分析")
    @OperateLog(detail = "历史空气质量分析",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result hisAirQualityAnalysisNew(){
        return  R.ok(projectAnalysisService.hisAirQualityAnalysisNew());
    }

    /**
     * 需传项目id
     * @param params
     * @return
     */
    @PostMapping("/latelyAirQualityAnalysis")
    @ApiOperation("近7天空气质量分析")
    @OperateLog(detail = "近7天空气质量分析",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result latelyAirQualityAnalysis(@RequestBody Map<String,Object> params){
        return  R.ok(projectAnalysisService.latelyAirQualityAnalysis(params));
    }

    @PostMapping("/latelyAirQualityAnalysisNew")
    @ApiOperation("近7天空气质量分析")
    @OperateLog(detail = "近7天空气质量分析",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result latelyAirQualityAnalysisNew(){
        return  R.ok(projectAnalysisService.latelyAirQualityAnalysisNew());
    }

    /**
     * 需传项目id
     * @param params
     * @return
     */
    @PostMapping("/todayAirQualityAnalysis")
    @ApiOperation("今天空气质量分析")
    @OperateLog(detail = "今天空气质量分析",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result todayAirQualityAnalysis(@RequestBody Map<String,Object> params){
        return  R.ok(projectAnalysisService.todayAirQualityAnalysis(params));
    }

    /**
     * 不传参
     * @return
     */
    @PostMapping("/todayAirQualityAnalysisNew")
    @ApiOperation("今天空气质量分析")
    @OperateLog(detail = "今天空气质量分析",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result todayAirQualityAnalysisNew(){
        return  R.ok(projectAnalysisService.todayAirQualityAnalysisNew());
    }

    @PostMapping("/sprayRecord")
    @ApiOperation("喷淋启停记录")
    @OperateLog(detail = "喷淋启停记录",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result sprayRecord(@RequestBody Map<String,Object> params){
        return  R.ok(projectAnalysisService.sprayRecord(params));
    }

    @PostMapping("/overView")
    @ApiOperation("环境总览按项目统计")
    @OperateLog(detail = "环境总览按项目统计",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result overView(@RequestBody Map<String,Object> params){
        return  R.ok(projectAnalysisService.overView(params));
    }
}
