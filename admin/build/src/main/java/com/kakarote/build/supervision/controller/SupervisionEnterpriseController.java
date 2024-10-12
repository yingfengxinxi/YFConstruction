package com.kakarote.build.supervision.controller;


import com.kakarote.build.eval.entity.PO.EvalApply;
import com.kakarote.build.supervision.entity.BO.SupervisionEnterpriseBO;
import com.kakarote.build.supervision.entity.PO.SupervisionEnterprise;
import com.kakarote.build.supervision.service.ISupervisionEnterpriseService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
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
 * @since 2022-11-29
 */
@RestController
@RequestMapping("/supervisionEnterprise")
public class SupervisionEnterpriseController {

    @Autowired
    private ISupervisionEnterpriseService supervisionEnterpriseService;

    @PostMapping("/list")
    @ApiOperation("查询监督任务列表")
    @OperateLog(detail = "查询监督任务列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> list(@RequestBody SupervisionEnterpriseBO supervisionEnterpriseBO) {
        return Result.ok(supervisionEnterpriseService.selectByPage(supervisionEnterpriseBO));
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("查询项目信息和监督任务信息")
    @OperateLog(detail = "查询项目信息和监督任务信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable @NotNull String id) {
        Map<String, Object> data = supervisionEnterpriseService.selectDataById(id);
        return Result.ok(data);
    }


    /**
     * 分配和调整方法
     * @return
     */
    @PostMapping("/insertOrupdateData")
    @ApiOperation("监督人员分配或调整")
    @OperateLog(detail = "通过评分", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result insertOrupdateData(@RequestBody SupervisionEnterprise supervisionEnterprise) {
        supervisionEnterpriseService.insertOrupdateData(supervisionEnterprise);
        return Result.ok();
    }

    @PostMapping("/selectSupeList")
    @ApiOperation("查询监督人员监督情况")
    @OperateLog(detail = "查询项目信息和监督任务信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectSupeList() {
        List<Map<String, Object>> list = supervisionEnterpriseService.selectSupeList();
        return R.ok(list);
    }
}

