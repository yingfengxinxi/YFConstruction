package com.kakarote.admin.demo.controller;


import cn.hutool.core.util.IdUtil;
import com.kakarote.admin.demo.entity.BO.WfDemoBO;
import com.kakarote.admin.demo.entity.PO.WfDemo;
import com.kakarote.admin.demo.service.IWfDemoService;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.core.workflow.WfAnnotation;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * DEMO表 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-03-15
 */
@RestController
@RequestMapping("/adminDemo")
public class WfDemoController {

    @Autowired
    private IWfDemoService demoService;

    /**
     * 分页列表
     */
    @PostMapping("/list")
    public Result list(@RequestBody WfDemoBO wfDemo) {
        return Result.ok(demoService.selectByPage(wfDemo));
    }

    /**
     * 获取详情
     */
    @PostMapping("/getById/{id}")
    public Result getById(@PathVariable String id) {
        return Result.ok(demoService.getById(id));
    }

    /**
     * 新增/修改
     */
    @PostMapping("/saveOrUpdate")
    @WfAnnotation
    public Result saveOrUpdate(@RequestBody WfDemo wfDemo) {
        if (wfDemo.getId() == null) {
            wfDemo.setId(IdUtil.randomUUID());
            wfDemo.setOwnerUserId(UserUtil.getUserId());
            demoService.save(wfDemo);
        }else{
            demoService.updateById(wfDemo);
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
            demoService.removeById(id);
        }
        return Result.ok();
    }

    /**
     * 逻辑删除
     */
    @PostMapping("/delLogicByIds")
    public Result del() {
        //更新check_status 为 7
        return Result.ok();
    }
}

