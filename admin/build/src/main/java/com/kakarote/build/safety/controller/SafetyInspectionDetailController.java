package com.kakarote.build.safety.controller;


import com.fasterxml.jackson.databind.ser.Serializers;
import com.kakarote.build.safety.entity.BO.SafetyInspectionDetailBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionDetail;
import com.kakarote.build.safety.service.ISafetyInspectionDetailService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工安全--安全巡检信息--子表巡检明细 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-04-13
 */
@RestController
@RequestMapping("/safetyInspectionDetail")
public class SafetyInspectionDetailController {

    @Autowired
    private ISafetyInspectionDetailService inspectionDetailService;

    /**
     * 危险源Top10数据
     * @return
     */
    @PostMapping("/selectPieData")
    @ApiOperation("安全检查统计--饼状图")
    @OperateLog(detail = "安全检查统计--饼状图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectPieData(){
        List<Map<String, Object>> list = inspectionDetailService.selectPieData();
        return R.ok(list);
    }

    /**
     * 检查问题数量统计
     * @return
     */
    @PostMapping("/selectBarData")
    @ApiOperation("安全检查统计--柱状图")
    @OperateLog(detail = "安全检查统计--饼状图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectBarData(){
        List<Map<String, Object>> list = inspectionDetailService.selectBarData();
        return R.ok(list);
    }

    /**
     * 安全隐患趋势
     * @return
     */
    @PostMapping("/selectCurveData")
    @ApiOperation("安全检查统计--曲线图")
    @OperateLog(detail = "安全检查统计--曲线图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectCurveData(){
        Map<String, Object> list = inspectionDetailService.selectCurveData();
        return R.ok(list);
    }

    /**
     * 整改时间预警
     * @return
     */
    @PostMapping("/selectRectifyDateWarning")
    @ApiOperation("安全检查统计--整改时间预警")
    @OperateLog(detail = "安全检查统计--整改时间预警", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectRectifyDateWarning(){
        List<Map<String, Object>> list = inspectionDetailService.selectRectifyDateWarning();
        return R.ok(list);
    }

    /**
     * @Description:查询隐患列表
     * @author shz
     * @date 2021-4-23 10:18
     * @param safetyInspectionDetailBO：危险源ID
     * @return 隐患列表
     */
    @PostMapping("/selectInspectionDetail")
    @ApiOperation("查询隐患列表")
    @OperateLog(detail = "查询隐患列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectDanger(@RequestBody SafetyInspectionDetailBO safetyInspectionDetailBO){
        BasePage<SafetyInspectionDetail> selectByPage  = inspectionDetailService.selectDanger(safetyInspectionDetailBO);
        return Result.ok(selectByPage);
    }

    /**
     * 安全隐患按负责人分析
     * @return
     */
    @PostMapping("/selectSafetyDataByPrinipal")
    @ApiOperation("安全隐患按负责人分析")
    @OperateLog(detail = "安全隐患按负责人分析", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectSafetyDataByPrinipal(){
        List<Map<String, Object>> list = inspectionDetailService.selectSafetyDataByPrinipal();
        return R.ok(list);
    }

    /**
     * 安全问题数量
     * @return
     */
    @PostMapping("/selectSafetyQuestionsQuantity")
    @ApiOperation("安全问题数量")
    @OperateLog(detail = "安全问题数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectSafetyQuestionsQuantity(){
        Map<String, Object> list = inspectionDetailService.selectSafetyQuestionsQuantity();
        return R.ok(list);
    }

    /**
     * 安全问题日曲线图
     * @param params type 1：查询最近7天；2：查询最近30天；3：查询最近12个月
     * @return
     */
    @PostMapping("/selectNumByType")
    @ApiOperation("安全问题日曲线图")
    @OperateLog(detail = "安全问题日曲线图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectNumByType(@RequestBody Map<String, Object> params){
        List<Map<String, Object>> list = inspectionDetailService.selectNumByType(params);
        return R.ok(list);
    }

    /**
     * 监管端使用 查询项目信息
     * @param inspectionDetailBO
     * @return
     */
    @PostMapping("/selectListByPro")
    @ApiOperation("查询安全隐患信息列表")
    @OperateLog(detail = "查询安全隐患信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<SafetyInspectionDetail>> selectListByPro(@RequestBody SafetyInspectionDetailBO inspectionDetailBO) {
        return Result.ok(inspectionDetailService.selectListByPro(inspectionDetailBO));
    }

    /**
     * 监管端使用 根据id查询项目信息
     * @param id
     * @return
     */
    @PostMapping("/selectDetailByIdPro/{id}")
    @ApiOperation("监管端-根据id查询安全隐患信息")
    @OperateLog(detail = "监管端-根据id查询安全隐患信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<SafetyInspectionDetail> selectDetailByIdPro(@PathVariable Integer id) {
        return Result.ok(inspectionDetailService.selectDetailByIdPro(id));
    }

    /**
     * 监管端使用 项目隐患数量统计
     * @return
     */
    @PostMapping("/selectProDangerNumber")
    @ApiOperation("监管端-项目隐患数量统计")
    @OperateLog(detail = "监管端-项目隐患数量统计", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectProDangerNumber(){
        Map<String, Object> map = inspectionDetailService.selectProDangerNumber();
        return R.ok(map);
    }

    /**
     * 监管端使用 安全隐患级别占比
     * @return
     */
    @PostMapping("/selectProDangerLevelProportion")
    @ApiOperation("监管端-安全隐患级别占比")
    @OperateLog(detail = "监管端-安全隐患级别占比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectProDangerLevelProportion(){
        Map<String, Object> map = inspectionDetailService.selectProDangerLevelProportion();
        return R.ok(map);
    }

    /**
     * 监管端使用 安全隐患类型占比
     * @return
     */
    @PostMapping("/selectProDangerTypeProportion")
    @ApiOperation("监管端-安全隐患类型占比")
    @OperateLog(detail = "监管端-安全隐患类型占比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectProDangerTypeProportion(){
        Map<String, Object> map = inspectionDetailService.selectProDangerTypeProportion();
        return R.ok(map);
    }

    /**
     * 监管端使用 安全隐患近30天整改数据
     * @return
     */
    @PostMapping("/selectProDangerRectifyEfficiency")
    @ApiOperation("监管端-安全隐患近30天整改数据")
    @OperateLog(detail = "监管端-安全隐患近30天整改数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectProDangerRectifyEfficiency(){
        Map<String, Object> map = inspectionDetailService.selectProDangerRectifyEfficiency();
        return R.ok(map);
    }

    /**
     * 监管端使用 安全隐患趋势分析
     * @return
     */
    @PostMapping("/selectTrendAnalyses/{type}")
    @ApiOperation("监管端-安全隐患趋势分析")
    @OperateLog(detail = "监管端-安全隐患趋势分析", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectTrendAnalyses(@PathVariable String type){
        List<Map<String, Object>> list = inspectionDetailService.selectTrendAnalyses(type);
        return R.ok(list);
    }

    /**
     * 监管端使用 安全隐患排名
     * @return
     */
    @PostMapping("/selectProDangerRank")
    @ApiOperation("监管端-安全隐患排名")
    @OperateLog(detail = "监管端-安全隐患排名", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectProDangerRank(){
        List<Map<String, Object>> list = inspectionDetailService.selectProDangerRank();
        return R.ok(list);
    }

    /**
     * 监管端使用 安全隐患数量占比分析
     * @return
     */
    @PostMapping("/selectDangerNumberProportion")
    @ApiOperation("监管端-安全隐患数量占比分析")
    @OperateLog(detail = "监管端-安全隐患数量占比分析", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectDangerNumberProportion(@RequestBody Map<String, Object> params){
        List<Map<String, Object>> list = inspectionDetailService.selectDangerNumberProportion(params);
        return R.ok(list);
    }

    /**
     * 监管端使用 整改时效分析
     * @return
     */
    @PostMapping("/selectProAgingAnalysis")
    @ApiOperation("监管端-整改时效分析")
    @OperateLog(detail = "监管端-整改时效分析", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectProAgingAnalysis(){
        List<Map<String, Object>> list = inspectionDetailService.selectProAgingAnalysis();
        return R.ok(list);
    }

    /**
     * 安全隐患等级分析
     * @return
     */
    @PostMapping("/selectSafetyLevelAnalysis")
    @ApiOperation("安全隐患等级分析")
    @OperateLog(detail = "安全隐患等级分析", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectSafetyLevelAnalysis(){
        List<Map<String, Object>> list = inspectionDetailService.selectSafetyLevelAnalysis();
        return R.ok(list);
    }

    /**
     * 安全隐患问题数量统计
     * @return
     */
    @PostMapping("/selectQuestionNumber")
    @ApiOperation("安全隐患问题数量统计")
    @OperateLog(detail = "安全隐患问题数量统计", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectQuestionNumber(){
        Map<String, Object> map = inspectionDetailService.selectQuestionNumber();
        return R.ok(map);
    }

    /**
     * @description
     * @author wnj58
     * @Params [params]
     * @updateTime 2022/4/19 17:49
     * @return com.kakarote.core.common.Result
     * @throws
     */
    @PostMapping("/selectQuestionList")
    @ApiOperation("安全隐患问题分类列表")
    @OperateLog(detail = "安全隐患问题分类列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectQuestionList(@RequestBody Map<String,Object> params){
        BasePage<Map<String, Object>> map = inspectionDetailService.selectQuestionList(params);
        return R.ok(map);
    }
}

