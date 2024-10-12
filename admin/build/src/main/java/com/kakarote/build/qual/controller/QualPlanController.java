package com.kakarote.build.qual.controller;


import cn.hutool.core.util.IdUtil;
import com.kakarote.build.qual.entity.BO.QualPlanBO;
import com.kakarote.build.qual.entity.PO.QualPlan;
import com.kakarote.build.qual.entity.PO.QualPlanBg;
import com.kakarote.build.qual.service.IQualPlanBgService;
import com.kakarote.build.qual.service.IQualPlanService;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 质量方案 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-03-22
 */
@RestController
@RequestMapping("/buildQualPlan")
public class QualPlanController {


    @Autowired
    private IQualPlanService qualPlanService;

    /**
     * 分页列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody QualPlanBO planBO) {
        return Result.ok(qualPlanService.selectByPage(planBO));
    }

    /**
     * 获取详情
     */
    @PostMapping("/getById/{id}")
    public Result getById(@PathVariable String id) {
        return Result.ok(qualPlanService.getById(id));
    }

    /**
     * 新增/修改
     */
    @PostMapping("/saveOrUpdate")
    //@WfAnnotation
    public Result saveOrUpdate(@RequestBody QualPlan qualPlan) {
        if (qualPlan.getId() == null) {
            qualPlan.setId(IdUtil.randomUUID());
            qualPlan.setOwnerUserId(UserUtil.getUserId());
            qualPlanService.save(qualPlan);
        }else{
            qualPlanService.updateById(qualPlan);
        }
        //throw new RuntimeException();
        return Result.ok();
    }

    /**
     * 物理删除
     */
    @PostMapping("/delTrueByIds")
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            qualPlanService.removeById(id);
        }
        return Result.ok();
    }

    @Autowired
    private IQualPlanBgService planBgService;

    /**
     * 组装打印数据
     */
    @PostMapping("/getPrintData/{id}")
    public Result getPrintData(@PathVariable String id) {
        QualPlan plan = qualPlanService.getById(id);
        List<QualPlanBg> list = planBgService.lambdaQuery().eq(QualPlanBg::getPlanId,plan.getId())
                .orderByAsc(QualPlanBg::getCreateTime).list();
        plan.setBgList(list);
        return Result.ok(plan);
    }
}

