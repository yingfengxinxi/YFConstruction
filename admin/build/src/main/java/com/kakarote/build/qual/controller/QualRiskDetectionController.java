package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.QualRiskDetectionBO;
import com.kakarote.build.qual.entity.PO.QualQuestions;
import com.kakarote.build.qual.entity.PO.QualRiskDetection;
import com.kakarote.build.qual.service.IQualQuestionsService;
import com.kakarote.build.qual.service.IQualRiskDetectionService;
import com.kakarote.build.safety.entity.BO.SafetyRiskDetectionBO;
import com.kakarote.build.safety.entity.PO.SafetyRiskDetection;
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
 *  前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-07-11
 */
@RestController
@RequestMapping("/qualRiskDetection")
public class QualRiskDetectionController {

    @Autowired
    private IQualRiskDetectionService qualRiskDetectionService;

    @Autowired
    private ISequenceService sequenceService;

    @Autowired
    private IQualQuestionsService questionsService;

    @PostMapping("/list")
    @ApiOperation("查询安全隐患排查列表")
    @OperateLog(detail = "查询安全隐患排查列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<QualRiskDetection>> list(@RequestBody QualRiskDetectionBO riskDetectionBO) {
        return Result.ok(qualRiskDetectionService.selectByPage(riskDetectionBO));
    }

    @PostMapping("/add")
    @ApiOperation("新增安全隐患排查信息")
    @OperateLog(detail = "新增安全隐患排查信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result add(@RequestBody QualRiskDetection riskDetection) {
        return qualRiskDetectionService.saveInfo(riskDetection);
    }

    /**
     * 修改、整改、复检使用该接口
     * @param riskDetection
     * @return
     */
    @PostMapping("/updateInfo")
    @ApiOperation("更新安全隐患信息")
    @OperateLog(detail = "更新安全隐患信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result updateInfo(@RequestBody QualRiskDetection riskDetection) {
        return Result.ok(qualRiskDetectionService.updateInfo(riskDetection));
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("根据id查询安全隐患排查信息")
    @OperateLog(detail = "根据Id查询巡检信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable @NotNull Integer id) {

        QualRiskDetection data = qualRiskDetectionService.getById(id);
        QualQuestions questions = questionsService.getById(data.getQuestionId());
        if (questions != null) {
            data.setQuestionName(questions.getQuestionName());
        }

        return Result.ok(data);
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除安全隐患排查信息")
    @OperateLog(detail = "删除安全隐患排查信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result delete(@PathVariable  Integer id) {
        QualRiskDetection riskDetection = new QualRiskDetection();
        riskDetection.setId(id);
        riskDetection.setIsDelete("1");
        return Result.ok(qualRiskDetectionService.updateById(riskDetection));
    }

    @PostMapping("/generateDocumentNumber")
    @ApiOperation("生成隐患编号")
    @OperateLog(detail = "生成隐患编号", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<String> generateDocumentNumber() {
        UserInfo userInfo = UserUtil.getUser();
        Integer projectId = userInfo.getProjectId().intValue();
        return Result.ok(sequenceService.getDateSeq("8", projectId));
    }

    /**
     * 质量问题Top10数据
     * @return
     */
    @PostMapping("/selectPieData")
    @ApiOperation("质量问题总览--饼状图")
    @OperateLog(detail = "质量问题总览--饼状图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectPieData(){
        List<Map<String, Object>> list = qualRiskDetectionService.selectPieData();
        return R.ok(list);
    }

    /**
     * 检查问题数量统计
     * @param params type 1：查询最近7天；2：查询最近30天；3：查询最近12个月
     * @return
     */
    @PostMapping("/selectBarData")
    @ApiOperation("质量问题总览--柱状图")
    @OperateLog(detail = "质量问题总览--饼状图", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectBarData(@RequestBody Map<String, Object> params){
        List<Map<String, Object>> list = qualRiskDetectionService.selectBarData(params);
        return R.ok(list);
    }

    /**
     * 整改时间预警
     * @return
     */
    @PostMapping("/selectRectifyDateWarning")
    @ApiOperation("质量问题总览--整改时间预警")
    @OperateLog(detail = "质量问题总览--整改时间预警", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectRectifyDateWarning(){
        List<Map<String, Object>> list = qualRiskDetectionService.selectRectifyDateWarning();
        return R.ok(list);
    }


    /**
     * 质量隐患按负责人分析
     * @return
     */
    @PostMapping("/selectQualDataByPrinipal")
    @ApiOperation("质量隐患按负责人分析")
    @OperateLog(detail = "质量隐患按负责人分析", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectQualDataByPrinipal(){
        List<Map<String, Object>> list = qualRiskDetectionService.selectQualDataByPrinipal();
        return R.ok(list);
    }

    /**
     * 质量问题数量
     * @return
     */
    @PostMapping("/selectQualQuestionsQuantity")
    @ApiOperation("质量问题数量")
    @OperateLog(detail = "质量问题数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectQualQuestionsQuantity(){
        Map<String, Object> list = qualRiskDetectionService.selectQualQuestionsQuantity();
        return R.ok(list);
    }
}

