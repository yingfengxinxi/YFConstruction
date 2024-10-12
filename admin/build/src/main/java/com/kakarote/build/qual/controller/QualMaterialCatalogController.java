package com.kakarote.build.qual.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.qual.entity.PO.QualMaterial;
import com.kakarote.build.qual.entity.PO.QualMaterialCatalog;
import com.kakarote.build.qual.service.IQualMaterialCatalogService;
import com.kakarote.build.qual.service.IQualMaterialService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 质量资料目录 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-04-13
 */
@RestController
@RequestMapping("/qualMaterialCatalog")
public class QualMaterialCatalogController {

    @Autowired
    private IQualMaterialCatalogService qualMaterialCatalogService;

    @Autowired
    private IQualMaterialService qualMaterialService;

    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.BUILD ,behavior = LogBehavior.INSERT)
    public Result add(@RequestBody QualMaterialCatalog safetyMaterialCatalog)
    {
        qualMaterialCatalogService.saveOrUpdate(safetyMaterialCatalog);
        return Result.ok();
    }

    @PostMapping("/selectTree/{id}")
    @ApiOperation("查询树结构数据")
    @OperateLog(detail = "新增数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectTree(@PathVariable Integer id){
        return Result.ok(qualMaterialCatalogService.selectTree(id));
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除目录")
    @OperateLog(detail = "删除目录",types= LogTypes.BUILD ,behavior = LogBehavior.DELETE)
    public Result delete(@PathVariable Integer id){
        qualMaterialCatalogService.removeById(id);
        QueryWrapper<QualMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catalog_id",id);
        qualMaterialService.remove(queryWrapper);
        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("查询数据")
    @OperateLog(detail = "查询数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable Integer id){
        return Result.ok(qualMaterialCatalogService.getById(id));
    }

    @PostMapping("/selectAll")
    @ApiOperation("查询所有目录数据")
    @OperateLog(detail = "查询所有目录数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectAll(){
        return Result.ok(qualMaterialCatalogService.list());
    }
}

