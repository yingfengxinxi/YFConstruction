package com.kakarote.build.safety.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.safety.entity.PO.SafetyMaterialChangelog;
import com.kakarote.build.safety.service.ISafetyMaterialChangelogService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 安全资料变更记录 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-04-07
 */
@RestController
@RequestMapping("/safetyMaterialChangelog")
public class SafetyMaterialChangelogController {

    @Autowired
    private ISafetyMaterialChangelogService iSafetyMaterialChangelogService;

    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.BUILD ,behavior = LogBehavior.INSERT)
    public Result add(@RequestBody SafetyMaterialChangelog safetyMaterialCatalog)
    {
        iSafetyMaterialChangelogService.save(safetyMaterialCatalog);
        return Result.ok();
    }

    @PostMapping("/selectByPid/{pid}")
    @ApiOperation("查询数据")
    @OperateLog(detail = "根据Pid查询数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectByPid(@PathVariable Integer pid){
        SafetyMaterialChangelog materialChangelog = new SafetyMaterialChangelog();
        QueryWrapper<SafetyMaterialChangelog> wrappers = new QueryWrapper<>();
        wrappers.eq("parent_id",pid);
        return Result.ok(iSafetyMaterialChangelogService.list(wrappers));
    }


}

