package com.kakarote.build.safety.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.safety.entity.BO.SafetyInspectionPlanBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionPlan;
import com.kakarote.build.safety.service.ISafetyInspectionPlanService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
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
 *  前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-04-07
 */
@RestController
@RequestMapping("/safetyInspectionPlan")
public class SafetyInspectionPlanController {

    @Autowired
    private ISafetyInspectionPlanService inspectionPlanService;

    @PostMapping("/list")
    @ApiOperation("查询安全检查计划")
    @OperateLog(detail = "查询安全检查计划", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<SafetyInspectionPlan>> list(@RequestBody SafetyInspectionPlanBO inspectionPlanBO) {
        return Result.ok(inspectionPlanService.selectByPage(inspectionPlanBO));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新安全检查计划")
    @OperateLog(detail = "保存或更新安全检查计划", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody SafetyInspectionPlan inspectionPlan) {
        if (inspectionPlan.getId() != null) {
            Long userId = UserUtil.getUser().getUserId();
            inspectionPlan.setUpdateUserId(userId);
            inspectionPlanService.updateById(inspectionPlan);
        } else {
            inspectionPlanService.save(inspectionPlan);
        }
        return Result.ok();
    }

    /**
     * 获取详情
     */
    @PostMapping("/getById/{id}")
    @ApiOperation("查询安全检查技术信息")
    @OperateLog(detail = "查询安全检查技术信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable Integer id) {
        return Result.ok(inspectionPlanService.getById(id));
    }

    /**
     * 删除质量检查信息
     */
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除安全检查信息")
    @OperateLog(detail = "删除安全检查信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            inspectionPlanService.removeById(id);
        }
        return Result.ok();
    }



    /**
     * 下拉框使用
     * @return
     */
    @RequestMapping("/selectOptions")
    @ApiOperation("查询数据列表")
    @OperateLog(detail = "查询数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<List<SafetyInspectionPlan>> selectOptions() {
        List<SafetyInspectionPlan> list = inspectionPlanService.selectOptions();
        return Result.ok(list);
    }
}

