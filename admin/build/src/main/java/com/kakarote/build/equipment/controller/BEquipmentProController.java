package com.kakarote.build.equipment.controller;


import com.kakarote.build.equipment.entity.BO.BEquipmentProBO;
import com.kakarote.build.equipment.entity.PO.BEquipmentPro;
import com.kakarote.build.equipment.service.IBEquipmentProService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 基坑/高支模工程管理  前端控制器
 * </p>
 *
 * @author wudw
 * @since 2021-12-23
 */
@RestController
@RequestMapping("/bEquipmentPro")
public class BEquipmentProController {
    @Autowired
    private IBEquipmentProService proService;

    @PostMapping("/list")
    @ApiOperation("查询工程列表")
    @OperateLog(detail = "工程列表", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<BasePage<BEquipmentPro>> list(@RequestBody BEquipmentProBO bEquipmentProBO) {
        BasePage<BEquipmentPro> list = proService.selectByPage(bEquipmentProBO);
        return Result.ok(list);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存工程信息")
    @OperateLog(detail = "保存工程信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody BEquipmentPro bEquipmentPro) {
        if (bEquipmentPro.getId() == null) {
            proService.save(bEquipmentPro);
        } else {
            bEquipmentPro.setUpdateUserId(UserUtil.getUserId());
            proService.updateById(bEquipmentPro);
        }
        return Result.ok();
    }


    @PostMapping("/getById/{id}")
    @ApiOperation("查询工程详情")
    @OperateLog(detail = "查询工程详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result get(@PathVariable Integer id) {
        BEquipmentPro bEquipmentPro = proService.getById(id);
        return Result.ok(bEquipmentPro);
    }

    /**
     * 删除工程
     */
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除工程")
    @OperateLog(detail = "删除工程", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String id : ids) {
            proService.removeById(id);
        }
        return Result.ok();
    }

    @PostMapping("/{type}/listDict")
    @ApiOperation("根据类型查询工程列表")
    @OperateLog(detail = "根据类型查询工程列表", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<List<BEquipmentPro>> listDict(@PathVariable String type) {
        List<BEquipmentPro> list = proService.lambdaQuery().eq(BEquipmentPro::getType,type)
                .orderByDesc(BEquipmentPro::getSort).list();
        return Result.ok(list);
    }

    //================大屏接口======================
    @GetMapping("/dplist/{type}")
    @ApiOperation("查询工程列表")
    @OperateLog(detail = "工程列表", types = LogTypes.SYS, behavior = LogBehavior.SELECT)
    public Result<List<BEquipmentPro>> list(@PathVariable String type) {
        if(StringUtils.isEmpty(type)){
            return Result.error(-1,"Parameter[type] is invalid");
        }
        List<BEquipmentPro> list = proService.lambdaQuery().eq(BEquipmentPro::getType,type)
                .orderByDesc(BEquipmentPro::getSort).list();
        return Result.ok(list);
    }
}

