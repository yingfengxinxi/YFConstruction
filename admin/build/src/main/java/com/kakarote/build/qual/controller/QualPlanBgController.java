package com.kakarote.build.qual.controller;


import cn.hutool.core.util.IdUtil;
import com.kakarote.build.qual.entity.BO.QualPlanBgBO;
import com.kakarote.build.qual.entity.PO.QualPlanBg;
import com.kakarote.build.qual.service.IQualPlanBgService;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 质量方案变更 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-03-23
 */
@RestController
@RequestMapping("/buildQualPlanBg")
public class QualPlanBgController {

    @Autowired
    private IQualPlanBgService planBgService;

    /**
     * 分页列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody QualPlanBgBO planBgBO) {
        return Result.ok(planBgService.selectByPage(planBgBO));
    }

    /**
     * 获取详情
     */
    @PostMapping("/getById/{id}")
    public Result getById(@PathVariable String id) {
        return Result.ok(planBgService.getById(id));
    }

    /**
     * 新增/修改
     */
    @PostMapping("/saveOrUpdate")
    //@WfAnnotation
    public Result saveOrUpdate(@RequestBody QualPlanBg qualPlanBg) {
        if (qualPlanBg.getId() == null) {
            qualPlanBg.setId(IdUtil.randomUUID());
            qualPlanBg.setOwnerUserId(UserUtil.getUserId());
            planBgService.save(qualPlanBg);
        }else{
            planBgService.updateById(qualPlanBg);
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
            planBgService.removeById(id);
        }
        return Result.ok();
    }
}

