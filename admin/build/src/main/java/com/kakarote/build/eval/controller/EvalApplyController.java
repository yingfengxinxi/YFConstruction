package com.kakarote.build.eval.controller;


import com.kakarote.build.eval.entity.BO.EvalApplyBO;
import com.kakarote.build.eval.entity.BO.EvalItemsBO;
import com.kakarote.build.eval.entity.PO.EvalApply;
import com.kakarote.build.eval.entity.PO.EvalItems;
import com.kakarote.build.eval.service.IEvalApplyService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 智慧工地评价-评估打分申请表 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-02-23
 */
@RestController
@RequestMapping("/evalApply")
public class EvalApplyController {

    @Autowired
    private IEvalApplyService evalApplyService;

    @PostMapping("/list")
    @ApiOperation("查询申请信息列表")
    @OperateLog(detail = "查询申请信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EvalApply>> list(@RequestBody EvalApplyBO evalApplyBO) {
        return Result.ok(evalApplyService.selectByPage(evalApplyBO));
    }

    @PostMapping("/save")
    @ApiOperation("保持申请信息和打分项数据")
    @OperateLog(detail = "保持申请信息和打分项数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result save(@RequestBody Map<String, Object> params) {

        evalApplyService.saveApplyAndApplyItemData(params);
        return Result.ok();
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("查询申请信息和打分项数据")
    @OperateLog(detail = "查询申请信息和打分项数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable @NotNull String id) {
        Map<String, Object> data = evalApplyService.selectDataById(id);
        return Result.ok(data);
    }

    /**
     * 打分项根据应用类别分类，打分项名称聚合为同一条数据。
     * @param id
     * @return
     */
    @PostMapping("/getInfoById/{id}")
    @ApiOperation("查询申请信息和打分项数据")
    @OperateLog(detail = "查询申请信息和打分项数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getInfoById(@PathVariable @NotNull String id) {
        Map<String, Object> data = evalApplyService.getInfoById(id);
        return Result.ok(data);
    }

    /**
     * 根据申请id、应用类别、分项名称查询打分项数据
     * @return
     */
    @PostMapping("/getItemList")
    @ApiOperation("查询打分项数据")
    @OperateLog(detail = "查询打分项数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getItemList(@RequestBody Map<String, Object> params) {
        List<Map<String, Object>> data = evalApplyService.getItemList(params);
        return Result.ok(data);
    }

    /**
     * APP端使用 只需计算得分，然后保存基本信息即可。
     * @return
     */
    @PostMapping("/updateInfoAndGrade")
    @ApiOperation("通过评分")
    @OperateLog(detail = "通过评分", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result updateInfoAndGrade(@RequestBody EvalApply evalApply) {

        evalApplyService.updateInfoAndGrade(evalApply);
        return Result.ok();
    }

    @PostMapping("/gradeOrHold")
    @ApiOperation("评分或暂存")
    @OperateLog(detail = "评分或暂存", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result gradeOrHold(@RequestBody Map<String, Object> params) {

        evalApplyService.gradeOrHold(params);
        return Result.ok();
    }

    @PostMapping("/applyRefuse/{id}")
    @ApiOperation("申请拒绝")
    @OperateLog(detail = "申请拒绝", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result applyRefuse(@PathVariable @NotNull String id) {


        Long userId = UserUtil.getUser().getUserId();
        EvalApply evalApply = new EvalApply();
        evalApply.setId(id);
        evalApply.setStatus("3");
        evalApply.setAppBy(userId);
        evalApply.setApplyTime(new Date());
        evalApply.setAppDes("该申请未通过！");

        evalApplyService.updateById(evalApply);

        return Result.ok();
    }

    @PostMapping("/selectRankingList")
    @ApiOperation("查询工地评分排名列表")
    @OperateLog(detail = "查询工地评分排名列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EvalApply>> selectRankingList(@RequestBody EvalApplyBO evalApplyBO) {
        return Result.ok(evalApplyService.selectRankingList(evalApplyBO));
    }



    @PostMapping("/selectExportDataById/{id}")
    @ApiOperation("查询评价表格生成数据")
    @OperateLog(detail = "查询评价表格生成数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectExportDataById(@PathVariable @NotNull String id) {
        Map<String, Object> data = evalApplyService.selectExportDataById(id);
        return Result.ok(data);
    }
}

