package com.kakarote.build.qual.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.qual.entity.PO.QualBuildMange;
import com.kakarote.build.qual.entity.PO.QualHouseTypeMange;
import com.kakarote.build.qual.entity.PO.QualQuestions;
import com.kakarote.build.qual.service.IQualBuildMangeService;
import com.kakarote.build.qual.service.IQualHouseNameService;
import com.kakarote.core.common.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--楼栋管理 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-06-22
 */
@RestController
@RequestMapping("/qualBuildMange")
public class QualBuildMangeController {
    @Autowired
    private IQualBuildMangeService IQualBuildMangeService;

    @GetMapping("/queryByParentId/{parentId}")
    @ApiOperation("根据上级查询楼栋管理数据")
    @OperateLog(detail = "根据上级查询楼栋管理数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<QualBuildMange>> queryByParentId(@PathVariable("parentId") Integer parentId) {
        List<QualBuildMange> list = IQualBuildMangeService.queryByParentId(parentId);
        return Result.ok(list);
    }
    @PostMapping("/Save")
    @ApiOperation("保存楼栋信息")
    @OperateLog(detail = "保存楼栋信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result<Long> save(@RequestBody QualBuildMange qualBuildMange) {
        IQualBuildMangeService.Save(qualBuildMange);
        return Result.ok();
    }
    @PostMapping("/CopyMenu")
    @ApiOperation("复制树级菜单")
    @OperateLog(detail = "复制树级菜单", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result CopyMenu(@RequestBody QualBuildMange qualBuildMange) {
        IQualBuildMangeService.CopyMenu(qualBuildMange);
        return Result.ok();
    }
    @GetMapping("/getById/{buildingId}")
    @ApiOperation("获取详情")
    public Result<Object> getMenuById(@PathVariable Integer buildingId) {
        return Result.ok(IQualBuildMangeService.getById(buildingId));
    }
    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或修改")
    @OperateLog(detail = "保存或修改", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result<Long> saveOrUpdateMessage(@RequestBody QualBuildMange qualBuildMange) {
        IQualBuildMangeService.saveOrUpdate(qualBuildMange);
        return Result.ok();
    }
    @GetMapping("/del/{buildingId}")
    @ApiOperation("删除节点")
    @OperateLog(detail = "删除节点", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result del(@PathVariable Integer buildingId) {
        return R.ok(IQualBuildMangeService.removeListById(buildingId));
    }

    @RequestMapping("/queryTreeList/{pid}")
    @ApiOperation("查询楼栋管理，当前节点所有子级数据")
    @OperateLog(detail = "查询楼栋管理，当前节点所有子级数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> queryTreeList(@PathVariable Integer pid) {
          List<Map<String, Object>> list = IQualBuildMangeService.queryAllList(pid);
        return Result.ok(list);
    }
    @RequestMapping("/queryTreeAllList/{pid}")
    @ApiOperation("查询楼栋管理，当前节点所有子级数据")
    @OperateLog(detail = "查询楼栋管理，当前节点所有子级数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> queryTreeAllList(@PathVariable Integer pid) {
        List<Map<String, Object>> list = IQualBuildMangeService.queryTreeAllList(pid);
        return Result.ok(list);
    }
    @PostMapping("/SaveDistrib")
    @ApiOperation("保存户型分配")
    @OperateLog(detail = "保存户型分配", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result SaveDistrib(@Validated
                              @RequestParam(value = "houseTypeId",required = false) String houseTypeId,
                              @RequestParam(value = "ZI",required = false) String ZI) {
        List<QualBuildMange> qualBuildMange =JSONObject.parseArray(ZI, QualBuildMange.class);
        IQualBuildMangeService.SaveDistrib(Integer.valueOf(houseTypeId),qualBuildMange);
        return Result.ok();
    }

    @RequestMapping("/getAllHouseList")
    @ApiOperation("查询所有房号")
    @OperateLog(detail = "查询所有房号", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getAllHouseList() {
        List<Map<String, Object>> list = IQualBuildMangeService.getAllHouseList();
        return Result.ok(list);
    }

    @RequestMapping("/wXqueryByQualBuild")
    @ApiOperation("根据上级查询楼栋管理数据")
    public Result<List<QualBuildMange>> wXqueryByParentId(@RequestBody QualBuildMange qualBuildMange) {
        List<QualBuildMange> list = IQualBuildMangeService.wXqueryByParentId(qualBuildMange);
        return Result.ok(list);
    }
}

