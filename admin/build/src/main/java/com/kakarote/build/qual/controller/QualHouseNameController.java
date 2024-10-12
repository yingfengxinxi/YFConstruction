package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.service.IQualHouseNameService;
import com.kakarote.build.qual.service.IQualHouseTypeMangeService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 施工质量--分户验收--户型管理--子表--房间名称 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-06-24
 */
@RestController
@RequestMapping("/qualHouseName")
public class QualHouseNameController {
    @Autowired
    private IQualHouseNameService iQualHouseNameService;
    @GetMapping("/getById/{id}")
    @ApiOperation("获取户型模板详情")
    @OperateLog(detail = "获取户型模板详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<Object> getMenuById(@PathVariable Integer id) {
        return Result.ok(iQualHouseNameService.getByHouseTypeId2(id));
    }

    @PostMapping("/getInfoByMap")
    @ApiOperation("据户型id和验收工序查询模板信息--分户验收填报使用")
    @OperateLog(detail = "据户型id和验收工序查询模板信息--分户验收填报使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getInfoByMap(@RequestBody Map map) {

        return Result.ok(iQualHouseNameService.getInfoByMap(map));
    }

}

