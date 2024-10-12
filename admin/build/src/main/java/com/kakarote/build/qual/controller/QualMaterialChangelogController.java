package com.kakarote.build.qual.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.qual.entity.PO.QualMaterialCatalog;
import com.kakarote.build.qual.entity.PO.QualMaterialChangelog;
import com.kakarote.build.qual.service.IQualMaterialCatalogService;
import com.kakarote.build.qual.service.IQualMaterialChangelogService;
import com.kakarote.build.safety.entity.PO.SafetyMaterialChangelog;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 质量资料变更记录 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-04-13
 */
@RestController
@RequestMapping("/qualMaterialChangelog")
public class QualMaterialChangelogController {

    @Autowired
    private IQualMaterialChangelogService qualMaterialChangelogService;

    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.BUILD ,behavior = LogBehavior.INSERT)
    public Result add(@RequestBody QualMaterialChangelog qualMaterialChangelog)
    {
        qualMaterialChangelogService.save(qualMaterialChangelog);
        return Result.ok();
    }

    @PostMapping("/selectByPid/{pid}")
    @ApiOperation("查询数据")
    @OperateLog(detail = "根据Pid查询数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectByPid(@PathVariable Integer pid){
        QueryWrapper<QualMaterialChangelog> wrappers = new QueryWrapper<>();
        wrappers.eq("parent_id",pid);
        return Result.ok(qualMaterialChangelogService.list(wrappers));
    }

}

