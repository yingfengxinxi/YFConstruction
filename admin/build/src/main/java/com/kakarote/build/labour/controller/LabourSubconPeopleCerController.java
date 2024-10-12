package com.kakarote.build.labour.controller;


import com.kakarote.build.labour.entity.BO.LabourSpeworktypeBO;
import com.kakarote.build.labour.entity.BO.LabourSubconPeopleCerBO;
import com.kakarote.build.labour.entity.PO.LabourSubconPeopleCer;
import com.kakarote.build.labour.entity.PO.LabourSubconPeopleCer;
import com.kakarote.build.labour.service.ILabourSubconPeopleCerService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 证件管理---分包管理人员证件台账 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-03-26
 */
@RestController
@RequestMapping("/labourSubconPeopleCer")
public class LabourSubconPeopleCerController {

    @Autowired
    private ILabourSubconPeopleCerService iLabourSubconPeopleCerService;

    @PostMapping("/list")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<LabourSubconPeopleCer>> list(@RequestBody LabourSubconPeopleCerBO LabourSubconPeopleCerBO) {
        BasePage<LabourSubconPeopleCer> list = iLabourSubconPeopleCerService.selectList(LabourSubconPeopleCerBO);
        return Result.ok(list);
    }

    @PostMapping("/queryById/{valveId}")
    @ApiOperation("根据主键查询信息")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result queryById(@PathVariable @NotNull Integer valveId){
        return R.ok(iLabourSubconPeopleCerService.getById(valveId));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("新增或者修改证件管理信息")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result add(@RequestBody LabourSubconPeopleCer LabourSubconPeopleCer) {
        return Result.ok(iLabourSubconPeopleCerService.saveOrUpdate(LabourSubconPeopleCer));
    }

    @Transactional
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除信息")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            iLabourSubconPeopleCerService.removeById(id);
        }

        return Result.ok();
    }

    @PostMapping("/selectTabsByInfo")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map>> selectTabsByInfo(@RequestBody LabourSpeworktypeBO LabourSpeworktypeBO) {
        BasePage<Map> list = iLabourSubconPeopleCerService.selectTabsByInfo(LabourSpeworktypeBO);
        return Result.ok(list);
    }

    /**
     * 取消项目过滤时使用该接口，该接口返回项目名称
     * @param LabourSpeworktypeBO
     * @return
     */
    @PostMapping("/selectTabsByInfoNew")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map>> selectTabsByInfoNew(@RequestBody LabourSpeworktypeBO LabourSpeworktypeBO) {
        BasePage<Map> list = iLabourSubconPeopleCerService.selectTabsByInfoNew(LabourSpeworktypeBO);
        return Result.ok(list);
    }


    /**
    * @Description:添加用户信息
    * @author shz
    * @date 2022-4-27 17:11
    * @param 
    * @return 
    */
    @PostMapping("/addUser/{pid}/{roleId}")
    @ApiOperation("添加用户信息")
    @OperateLog(detail = "添加用户信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result addUser(@PathVariable @NotNull Integer pid, @PathVariable @NotNull Integer roleId){
        return iLabourSubconPeopleCerService.addUser(pid,roleId);
    }
}

