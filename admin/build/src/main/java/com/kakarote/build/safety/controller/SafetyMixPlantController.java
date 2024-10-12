package com.kakarote.build.safety.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.safety.entity.BO.SafetyMixPlantBO;
import com.kakarote.build.safety.entity.PO.SafetyMixPlant;
import com.kakarote.build.safety.service.ISafetyMixPlantService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 拌合站监控 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-06-18
 */
@RestController
@RequestMapping("/safetyMixPlant")
public class SafetyMixPlantController {
    @Autowired
    private ISafetyMixPlantService ISafetyMixPlantService;

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result list(@RequestBody SafetyMixPlantBO SafetyMixPlantBO) {
        BasePage<SafetyMixPlant> list =  ISafetyMixPlantService.selectByPage(SafetyMixPlantBO);
        return Result.ok(list);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新")
    @OperateLog(detail = "保存或更新", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody SafetyMixPlant SafetyMixPlant) {
        if (SafetyMixPlant.getId() == null) {
            SafetyMixPlant.setCreateBy(String.valueOf(UserUtil.getUserId()));
            SafetyMixPlant.setCreateTime(new Date());
            ISafetyMixPlantService.save(SafetyMixPlant);
        }else{
            SafetyMixPlant.setUpdateBy(String.valueOf(UserUtil.getUserId()));
            SafetyMixPlant.setUpdateTime(new Date());
            SafetyMixPlant.setHandleUserId(UserUtil.getUserId());
            ISafetyMixPlantService.updateById(SafetyMixPlant);
        }
        return Result.ok();
    }
    @PostMapping("/getById/{id}")
    @ApiOperation("获取详情")
    @OperateLog(detail = "获取详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable String id) {
        return Result.ok(ISafetyMixPlantService.getBaseMapper().selectById(id));
    }

    @PostMapping("/delTrueByIds")
    @ApiOperation("数据删除")
    @OperateLog(detail = "数据删除", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            ISafetyMixPlantService.removeById(id);
        }
        return Result.ok();
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入拌合站监控报警数据")
    @OperateLog(detail = "excel导入拌合站监控报警数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) throws Exception{
        JSONObject object = ISafetyMixPlantService.excelImport(file);
        return R.ok(object);
    }
}

