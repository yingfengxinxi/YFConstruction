package com.kakarote.build.project.controller;


import com.kakarote.build.project.entity.BO.ProjectPublicInfoBO;
import com.kakarote.build.project.entity.PO.ProjectPublicInfo;
import com.kakarote.build.project.service.IProjectPublicInfoService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 公示信息 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-06-15
 */
@RestController
@RequestMapping("/projectPublicInfo")
public class ProjectPublicInfoController {

    @Autowired
    private IProjectPublicInfoService publicInfoService;


    @PostMapping("/list")
    @ApiOperation("查询信息公示列表")
    @OperateLog(detail = "查询信息公示列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<ProjectPublicInfo>> list(@RequestBody ProjectPublicInfoBO publicInfoBO) {
        return Result.ok( publicInfoService.selectByPage(publicInfoBO));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新公示信息")
    @OperateLog(detail = "保存或更新公示信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody ProjectPublicInfo projectPublicInfo) {
        System.out.println("走了吗");
        System.out.println(projectPublicInfo);
        return Result.ok(publicInfoService.saveOrUpdate(projectPublicInfo));
    }

    @PostMapping("/getById/{id}")
    @ApiOperation("根据Id查询公示信息")
    @OperateLog(detail = "根据Id查询公示信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable @NotNull Integer id) {
        ProjectPublicInfo projectPublicInfo = publicInfoService.getById(id);
        return Result.ok(projectPublicInfo);
    }

    /**
     * 删除公示信息
     */
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除公示信息")
    @OperateLog(detail = "删除公示信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            // 根据checkId删除隐患信息
            publicInfoService.removeById(id);
        }
        return Result.ok();
    }
}

