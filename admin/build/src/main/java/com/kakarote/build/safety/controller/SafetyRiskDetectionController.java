package com.kakarote.build.safety.controller;


import com.kakarote.build.safety.entity.BO.SafetyRiskDetectionBO;
import com.kakarote.build.safety.entity.PO.SafetyRiskDetection;
import com.kakarote.build.safety.service.ISafetyRiskDetectionService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.service.ISequenceService;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 安全隐患排查 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-06-27
 */
@RestController
@RequestMapping("/safetyRiskDetection")
public class SafetyRiskDetectionController {

    @Autowired
    private ISafetyRiskDetectionService riskDetectionService;

    @Autowired
    private ISequenceService sequenceService;

    @PostMapping("/list")
    @ApiOperation("查询安全隐患排查列表")
    @OperateLog(detail = "查询安全隐患排查列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<SafetyRiskDetection>> list(@RequestBody SafetyRiskDetectionBO riskDetectionBO) {
        return Result.ok(riskDetectionService.selectByPage(riskDetectionBO));
    }

    @PostMapping("/selectListByPro")
    @ApiOperation("查询安全隐患排查列表")
    @OperateLog(detail = "查询安全隐患排查列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<SafetyRiskDetection>> selectListByPro(@RequestBody SafetyRiskDetectionBO riskDetectionBO) {
        return Result.ok(riskDetectionService.selectListByPro(riskDetectionBO));
    }

    @PostMapping("/add")
    @ApiOperation("新增安全隐患排查信息")
    @OperateLog(detail = "新增安全隐患排查信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result add(@RequestBody SafetyRiskDetection riskDetection) {
        return riskDetectionService.saveInfo(riskDetection);
    }

    /**
     * 修改、整改、复检使用该接口
     * @param riskDetection
     * @return
     */
    @PostMapping("/updateInfo")
    @ApiOperation("更新安全隐患信息")
    @OperateLog(detail = "更新安全隐患信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result updateInfo(@RequestBody SafetyRiskDetection riskDetection) {
        return Result.ok(riskDetectionService.updateInfo(riskDetection));
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("根据id查询安全隐患排查信息")
    @OperateLog(detail = "根据Id查询巡检信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable @NotNull Integer id) {
        SafetyRiskDetection data = riskDetectionService.getById(id);
        return Result.ok(data);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除安全隐患排查信息")
    @OperateLog(detail = "删除安全隐患排查信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result delete(@PathVariable  Integer id) {
        SafetyRiskDetection riskDetection = new SafetyRiskDetection();
        riskDetection.setId(id);
        riskDetection.setIsDelete("1");
        return Result.ok(riskDetectionService.updateById(riskDetection));
    }

    /**
     * 查询安全隐患排查超期数据
     * @return
     */
    @PostMapping("/selectTimeoutList")
    @ApiOperation("查询安全隐患排查超期数据")
    @OperateLog(detail = "查询安全隐患排查超期数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<SafetyRiskDetection>> selectTimeoutList(@RequestBody SafetyRiskDetectionBO riskDetectionBO) {
        return Result.ok(riskDetectionService.selectTimeoutList(riskDetectionBO));
    }

    @PostMapping("/dangeTimeoutPush/{id}")
    @ApiOperation("安全隐患超期推送")
    @OperateLog(detail = "安全隐患超期推送", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result dangeTimeoutPush(@PathVariable Integer id) {
        return riskDetectionService.dangeTimeoutPush(id);
    }

    @PostMapping("/generateDocumentNumber")
    @ApiOperation("生成隐患编号")
    @OperateLog(detail = "生成隐患编号", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<String> generateDocumentNumber() {
        UserInfo userInfo = UserUtil.getUser();
        Integer projectId = userInfo.getProjectId().intValue();
        return Result.ok(sequenceService.getDateSeq("7", projectId));
    }

    /**
     * 安全隐患问题数量统计
     * @return
     */
    @PostMapping("/selectQuestionNumber")
    @ApiOperation("安全隐患问题数量统计")
    @OperateLog(detail = "安全隐患问题数量统计", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectQuestionNumber(){
        Map<String, Object> map = riskDetectionService.selectQuestionNumber();
        return R.ok(map);
    }


    @PostMapping("/selectSafetyHiddenDanger")
    @ApiOperation("安全隐患-总数和未解决数")
    @OperateLog(detail = "安全隐患-总数和未解决数", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectSafetyHiddenDanger(@RequestBody Map<String,Object> params){
        List<Map<String, Object>> list = riskDetectionService.selectSafetyHiddenDanger(params);
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
        List<Map<String, Object>> list = riskDetectionService.selectSafetyLevelAnalysis();
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
        List<Map<String, Object>> list = riskDetectionService.selectNumByType(params);
        return R.ok(list);
    }

    /**
     * 安全隐患按负责人分析
     * @return
     */
    @PostMapping("/selectSafetyDataByPrinipal")
    @ApiOperation("安全隐患按负责人分析")
    @OperateLog(detail = "安全隐患按负责人分析", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectSafetyDataByPrinipal(){
        List<Map<String, Object>> list = riskDetectionService.selectSafetyDataByPrinipal();
        return R.ok(list);
    }

    /**
     * 危险源Top10数据
     * @return
     */
    @PostMapping("/selectPieData")
    @ApiOperation("安全检查统计--饼状图")
    @OperateLog(detail = "安全检查统计--饼状图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectPieData(){
        List<Map<String, Object>> list = riskDetectionService.selectPieData();
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
        Map<String, Object> list = riskDetectionService.selectSafetyQuestionsQuantity();
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
        List<Map<String, Object>> list = riskDetectionService.selectBarData();
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
        Map<String, Object> list = riskDetectionService.selectCurveData();
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
        List<Map<String, Object>> list = riskDetectionService.selectRectifyDateWarning();
        return R.ok(list);
    }

    @PostMapping("/selectQuestionList")
    @ApiOperation("安全隐患问题分类列表")
    @OperateLog(detail = "安全隐患问题分类列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectQuestionList(@RequestBody Map<String,Object> params){
        BasePage<Map<String, Object>> map = riskDetectionService.selectQuestionList(params);
        return R.ok(map);
    }

    @PostMapping("/selectPhotoList")
    @ApiOperation("查询隐患照片列表")
    @OperateLog(detail = "查询隐患照片列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<SafetyRiskDetection>> selectPhotoList(@RequestBody SafetyRiskDetectionBO riskDetectionBO) {
        return Result.ok(riskDetectionService.selectPhotoList(riskDetectionBO));
    }

    /**
     * 监管端使用 项目隐患数量统计
     * @return
     */
    @PostMapping("/selectProDangerNumber")
    @ApiOperation("监管端-项目隐患数量统计")
    @OperateLog(detail = "监管端-项目隐患数量统计", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectProDangerNumber(){
        Map<String, Object> map = riskDetectionService.selectProDangerNumber();
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
        Map<String, Object> map = riskDetectionService.selectProDangerRectifyEfficiency();
        return R.ok(map);
    }

    /**
     * 监管端使用 安全隐患数量占比分析
     * @return
     */
    @PostMapping("/selectDangerNumberProportion")
    @ApiOperation("监管端-安全隐患数量占比分析")
    @OperateLog(detail = "监管端-安全隐患数量占比分析", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectDangerNumberProportion(@RequestBody Map<String, Object> params){
        List<Map<String, Object>> list = riskDetectionService.selectDangerNumberProportion(params);
        return R.ok(list);
    }

    /**
     * 监管端使用 安全隐患趋势分析
     * @return
     */
    @PostMapping("/selectTrendAnalyses/{type}")
    @ApiOperation("监管端-安全隐患趋势分析")
    @OperateLog(detail = "监管端-安全隐患趋势分析", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectTrendAnalyses(@PathVariable String type){
        List<Map<String, Object>> list = riskDetectionService.selectTrendAnalyses(type);
        return R.ok(list);
    }

    /**
     * 监管端使用 安全隐患级别占比
     * @return
     */
    @PostMapping("/selectProDangerLevelProportion")
    @ApiOperation("监管端-安全隐患级别占比")
    @OperateLog(detail = "监管端-安全隐患级别占比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectProDangerLevelProportion(){
        Map<String, Object> map = riskDetectionService.selectProDangerLevelProportion();
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
        Map<String, Object> map = riskDetectionService.selectProDangerTypeProportion();
        return R.ok(map);
    }

    /**
     * 监管端使用 安全隐患排名
     * @return
     */
    @PostMapping("/selectProDangerRank")
    @ApiOperation("监管端-安全隐患排名")
    @OperateLog(detail = "监管端-安全隐患排名", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectProDangerRank(){
        List<Map<String, Object>> list = riskDetectionService.selectProDangerRank();
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
        List<Map<String, Object>> list = riskDetectionService.selectProAgingAnalysis();
        return R.ok(list);
    }
}

