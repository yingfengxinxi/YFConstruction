package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.LabourSafetyOfficerBO;
import com.kakarote.build.labour.entity.PO.LabourOddVisa;
import com.kakarote.build.labour.entity.PO.LabourSafetyOfficer;
import com.kakarote.build.labour.service.ILabourSafetyOfficerService;
import com.kakarote.core.common.*;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 证件管理--安全从业人员证件台账 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-11-10
 */
@RestController
@RequestMapping("/labourSafetyOfficer")
public class LabourSafetyOfficerController {

    @Autowired
    private ILabourSafetyOfficerService ILabourSafetyOfficerService;

    @PostMapping("getList")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getList(@RequestBody LabourSafetyOfficerBO labourSafetyOfficerBO) {
        return R.ok(ILabourSafetyOfficerService.getList(labourSafetyOfficerBO));
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable String id) {
        return Result.ok(ILabourSafetyOfficerService.getInfoById(Integer.valueOf(id)));
    }

    @Transactional
    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存修改安全员证件台账信息")
    @OperateLog(detail = "保存修改安全员证件台账信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody LabourSafetyOfficer labourSafetyOfficer) {
        if(labourSafetyOfficer.getId()==null){
            labourSafetyOfficer.setCreateBy(String.valueOf(UserUtil.getUserId()));
            labourSafetyOfficer.setCreateTime(new Date());
            return R.ok(ILabourSafetyOfficerService.save(labourSafetyOfficer));
        }else{
            labourSafetyOfficer.setUpdateBy(String.valueOf(UserUtil.getUserId()));
            labourSafetyOfficer.setUpdateTime(new Date());
            return R.ok(ILabourSafetyOfficerService.updateById(labourSafetyOfficer));
        }
    }

    @Transactional
    @PostMapping("/delByIds")
    @ApiOperation("批量删除安全员证件台账信息")
    @OperateLog(detail = "批量删除安全员证件台账信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delByIds(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        return R.ok(ILabourSafetyOfficerService.removeByIds(ids));
    }

    /**
     * 取消项目过滤时使用该接口，该接口返回项目名称
     * @param labourSafetyOfficerBO
     * @return
     */
    @PostMapping("getListNew")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getListNew(@RequestBody LabourSafetyOfficerBO labourSafetyOfficerBO) {
        return R.ok(ILabourSafetyOfficerService.getListNew(labourSafetyOfficerBO));
    }
}

