package com.kakarote.build.safety.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.safety.entity.PO.SafetyMaterial;
import com.kakarote.build.safety.entity.PO.SafetyMaterialCatalog;
import com.kakarote.build.safety.service.ISafetyMaterialCatalogService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 安全资料目录 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-04-07
 */
@RestController
@RequestMapping("/safetyMaterialCatalog")
public class SafetyMaterialCatalogController {

    @Autowired
    private ISafetyMaterialCatalogService iSafetyMaterialCatalogService;

    @Autowired
    private ISafetyMaterialService iSafetyMaterialService;

    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.BUILD ,behavior = LogBehavior.INSERT)
    public Result add(@RequestBody SafetyMaterialCatalog safetyMaterialCatalog)
    {
        iSafetyMaterialCatalogService.saveOrUpdate(safetyMaterialCatalog);
        return Result.ok();
    }

    @PostMapping("/selectTree/{id}")
    @ApiOperation("查询树结构数据")
    @OperateLog(detail = "新增数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectTree(@PathVariable Integer id){
        return Result.ok(iSafetyMaterialCatalogService.selectTree(id));
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除目录")
    @OperateLog(detail = "删除目录",types= LogTypes.BUILD ,behavior = LogBehavior.DELETE)
    public Result delete(@PathVariable Integer id){
        iSafetyMaterialCatalogService.removeById(id);
        QueryWrapper<SafetyMaterial> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("catalog_id",id);
        iSafetyMaterialService.remove(queryWrapper);
        return Result.ok();
    }

    @PostMapping("/deleteOneselfAndSublevel/{id}")
    @ApiOperation("删除目录及其子目录")
    @OperateLog(detail = "删除目录",types= LogTypes.BUILD ,behavior = LogBehavior.DELETE)
    public Result deleteOneselfAndSublevel(@PathVariable Integer id){
        iSafetyMaterialCatalogService.deleteOneselfAndSublevel(id);
        return Result.ok();
    }


    @PostMapping("/selectById/{id}")
    @ApiOperation("查询数据")
    @OperateLog(detail = "查询数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable Integer id){
        return Result.ok(iSafetyMaterialCatalogService.getById(id));
    }


    @PostMapping("/selectAll")
    @ApiOperation("查询所有目录数据")
    @OperateLog(detail = "查询所有目录数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectAll(){
        return Result.ok(iSafetyMaterialCatalogService.list());
    }
}

