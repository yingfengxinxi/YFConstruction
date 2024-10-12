package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.QualMaterialBO;
import com.kakarote.build.qual.entity.PO.QualMaterial;
import com.kakarote.build.qual.service.IQualMaterialService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 质量资料 前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-04-13
 */
@RestController
@RequestMapping("/qualMaterial")
public class QualMaterialController {

    @Autowired
    private IQualMaterialService qualMaterialService;

    @PostMapping("/add")
    @ApiOperation("新增数据")
    @OperateLog(detail = "新增数据",types= LogTypes.BUILD ,behavior = LogBehavior.INSERT)
    public Result add(@RequestBody QualMaterial safetyMaterial)
    {
        qualMaterialService.saveOrUpdate(safetyMaterial);
        return Result.ok();
    }
    @PostMapping("/selectPageAllByPid")
    @ApiOperation("查询数据")
    @OperateLog(detail = "查询数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectPageAllByPid(@RequestBody QualMaterialBO safetyMaterialBO){
        return Result.ok(qualMaterialService.selectPageAllByPid(safetyMaterialBO));
    }

    @PostMapping("/selectByCollect/{id}")
    @ApiOperation("查询数据")
    @OperateLog(detail = "查询数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result  selectByCollect(@PathVariable Integer id){
        return Result.ok(qualMaterialService.selectByCollect(id));
    }

    @PostMapping("/collect/{id}")
    @ApiOperation("收藏数据")
    @OperateLog(detail = "收藏数据",types= LogTypes.BUILD ,behavior = LogBehavior.INSERT)
    public Result collect(@PathVariable Integer id){
        Long userId = UserUtil.getUserId();
        int i = qualMaterialService.collect(id,userId);
        if(i>0){
            return Result.ok();
        }else {
            return Result.error(500,"收藏失败");
        }
    }

    @PostMapping("/deleteCollect/{id}")
    @ApiOperation("删除收藏数据")
    @OperateLog(detail = "删除收藏数据",types= LogTypes.BUILD ,behavior = LogBehavior.DELETE)
    public Result deleteCollect(@PathVariable Integer id){
        int i = qualMaterialService.deleteCollect(id);
        if(i>0){
            return Result.ok();
        }else {
            return Result.error(500,"收藏失败");
        }
    }
    @PostMapping("/getById/{id}")
    @ApiOperation("查询数据")
    @OperateLog(detail = "查询数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable Integer id){
        return Result.ok(qualMaterialService.getById(id));
    }

    @PostMapping("/removeById/{id}")
    @ApiOperation("删除数据")
    @OperateLog(detail = "删除数据",types= LogTypes.BUILD ,behavior = LogBehavior.DELETE)
    public Result removeById(@PathVariable Integer id){
        return Result.ok(qualMaterialService.removeById(id));
    }

    @PostMapping("/selectCollect/{id}")
    @ApiOperation("查询收藏数据")
    @OperateLog(detail = "查询收藏数据",types= LogTypes.BUILD ,behavior = LogBehavior.SELECT)
    public Result selectCollect(@PathVariable Integer id){
        return Result.ok(qualMaterialService.removeById(id));
    }

}

