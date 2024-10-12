package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.LabourSpeworktypeBO;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.build.labour.service.ILabourSpeworktypeService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 特殊工种--证件管理（分包单位--特殊工种--证件台账） 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-03-19
 */
@RestController
@RequestMapping("/labourSpeworktype")
public class LabourSpeworktypeController {

    @Autowired
    private ILabourSpeworktypeService iLabourSpeworktypeService;

    @ApiOperation(value = "查询所有特殊工种--证件管理信息")
    @RequestMapping("/query")
    @OperateLog(detail = "查询所有特殊工种--证件管理信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<LabourSpeworktype>> query(){
        return R.ok(iLabourSpeworktypeService.list());
    }

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<LabourSpeworktype>> list(@RequestBody LabourSpeworktypeBO LabourSpeworktypeBO) {
        BasePage<LabourSpeworktype> list = iLabourSpeworktypeService.selectList(LabourSpeworktypeBO);
        return Result.ok(list);
    }

    @PostMapping("/queryById/{valveId}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "根据主键查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryById(@PathVariable @NotNull Integer valveId){
        return R.ok(iLabourSpeworktypeService.getById(valveId));
    }

    @PostMapping("/add")
    @ApiOperation("新增特殊工种--证件管理信息")
    @OperateLog(detail = "新增特殊工种--证件管理信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result add(@RequestBody LabourSpeworktype labourSpeworktype) {
        labourSpeworktype.setCreateTime(new Date());
        labourSpeworktype.setCreateUserId(UserUtil.getUserId());
        return Result.ok(iLabourSpeworktypeService.saveOrUpdate(labourSpeworktype));
    }


    @PostMapping("/update")
    @ApiOperation("修改特殊工种--证件管理信息")
    @OperateLog(detail = "修改特殊工种--证件管理信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result edit(@Validated @RequestBody LabourSpeworktype labourWorktype) {
        labourWorktype.setUpdateTime(new Date());
        labourWorktype.setUpdateUserId(UserUtil.getUserId());
        return Result.ok(iLabourSpeworktypeService.saveOrUpdate(labourWorktype));
    }


    @PostMapping("/delete/{id}")
    @ApiOperation("删除特殊工种--证件管理信息")
    @OperateLog(detail = "删除特殊工种--证件管理信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable Integer id) {
        return Result.ok(iLabourSpeworktypeService.removeById(id));
    }

    @PostMapping("/queryByInfoId/{infoId}")
    @ApiOperation("根据采集信息主键查询信息")
    @OperateLog(detail = "根据采集信息主键查询信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryByInfoId(@PathVariable @NotNull Integer infoId){
        return R.ok(iLabourSpeworktypeService.selectByInfoId(infoId));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或者修改特殊工种--证件管理信息")
    @OperateLog(detail = "新增或者修改特殊工种--证件管理信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result saveOrUpdate(@Validated @RequestBody LabourSpeworktype labourWorktype) {
        return Result.ok(iLabourSpeworktypeService.saveOrUpdate(labourWorktype));
    }

    @Transactional
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除信息")
    @OperateLog(detail = "删除信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            iLabourSpeworktypeService.removeById(id);
        }

        return Result.ok();
    }

    @PostMapping("/selectTabsByInfo")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询填报主表信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map>> selectTabsByInfo(@RequestBody LabourSpeworktypeBO LabourSpeworktypeBO) {
        BasePage<Map> list = iLabourSpeworktypeService.selectTabsByInfo(LabourSpeworktypeBO);
        return Result.ok(list);
    }

    /**
     * 取消项目过滤时使用该接口，该接口返回项目名称
     * @param LabourSpeworktypeBO
     * @return
     */
    @PostMapping("/selectTabsByInfoNew")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询填报主表信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map>> selectTabsByInfoNew(@RequestBody LabourSpeworktypeBO LabourSpeworktypeBO) {
        BasePage<Map> list = iLabourSpeworktypeService.selectTabsByInfoNew(LabourSpeworktypeBO);
        return Result.ok(list);
    }
}

