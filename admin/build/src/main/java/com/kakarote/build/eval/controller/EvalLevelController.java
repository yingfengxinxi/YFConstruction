package com.kakarote.build.eval.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.eval.entity.BO.EvalLevelBO;
import com.kakarote.build.eval.entity.PO.EvalLevel;
import com.kakarote.build.eval.service.IEvalLevelService;
import com.kakarote.build.project.entity.PO.ProjectInfo;
import com.kakarote.build.safety.entity.BO.SafetyInspectionPlanBO;
import com.kakarote.build.safety.entity.PO.SafetyInspectionPlan;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-02-22
 */
@RestController
@RequestMapping("/evalLevel")
public class EvalLevelController {

    @Autowired
    private IEvalLevelService evalLevelService;

    @PostMapping("/list")
    @ApiOperation("查询工地等级数据")
    @OperateLog(detail = "查询工地等级数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EvalLevel>> list(@RequestBody EvalLevelBO evalLevelBO) {
        return Result.ok(evalLevelService.selectByPage(evalLevelBO));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新工地等级数据")
    @OperateLog(detail = "保存或更新工地等级数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody EvalLevel evalLevel) {
        Long userId = UserUtil.getUser().getUserId();
        if (evalLevel.getId() != null) {
            evalLevel.setUpdateBy(userId);
            evalLevel.setUpdateTime(new Date());
            evalLevelService.updateById(evalLevel);
        } else {
            String uuid= UUID.randomUUID().toString().replaceAll("-", "");
            evalLevel.setId(uuid);
            evalLevel.setCreateBy(userId);
            evalLevel.setCreateTime(new Date());
            evalLevelService.save(evalLevel);
        }
        return Result.ok();
    }

    /**
     * 获取详情
     */
    @PostMapping("/getById/{id}")
    @ApiOperation("查询工地等级信息")
    @OperateLog(detail = "查询工地等级信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable String id) {
        return Result.ok(evalLevelService.getById(id));
    }

    /**
     * 更新工地等级状态
     * id 必填
     * status 0:停用 1：启用 -1逻辑删除
     * @param evalLevelParam
     * @return
     */
    @PostMapping("/updateStatus")
    @ApiOperation("更新工地等级状态")
    @OperateLog(detail = "更新工地等级状态", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result updateStatus(@RequestBody EvalLevel evalLevelParam) {

        if (evalLevelParam.getId() == null) {
            throw new CrmException(500, "参数id不能为null");
        }

        if (evalLevelParam.getStatus() == null || evalLevelParam.getStatus().equals("")) {
            throw new CrmException(500, "参数status不能为null");
        }

        EvalLevel evalLevel = new EvalLevel();
        evalLevel.setId(evalLevelParam.getId());
        evalLevel.setStatus(evalLevelParam.getStatus());

        evalLevelService.updateById(evalLevel);
        return Result.ok();
    }

    @PostMapping("/selectList")
    @ApiOperation("查询工地等级列表数据")
    @OperateLog(detail = "查询工地等级列表数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectList() {

        QueryWrapper<EvalLevel> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id, level_name AS levelName").eq("status", 1).ne("project_id", 0);
        List<Map<String, Object>> levelList = evalLevelService.listMaps(queryWrapper);

        return Result.ok(levelList);
    }
}

