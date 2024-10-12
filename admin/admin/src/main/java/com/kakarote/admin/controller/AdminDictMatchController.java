package com.kakarote.admin.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.admin.common.AdminDictUtils;
import com.kakarote.admin.entity.BO.AdminDictMathBO;
import com.kakarote.admin.entity.PO.AdminDictMatch;
import com.kakarote.admin.service.IAdminDictMatchService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-11-30
 */
@RestController
@RequestMapping("/adminDictMatch")
public class AdminDictMatchController {


    @Autowired
    private IAdminDictMatchService adminDictMatchService;

    @PostMapping("/list")
    @ApiOperation("查询列表")
    @OperateLog(detail = "查询列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<AdminDictMatch>> list(@RequestBody AdminDictMathBO adminDictMathBO) {
        BasePage<AdminDictMatch> list = adminDictMatchService.selectByPage(adminDictMathBO);
        return Result.ok(list);
    }

    @PostMapping("/get/{id}")
    @ApiOperation("查询字典转换详情")
    @OperateLog(detail = "查询字典转换详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<AdminDictMatch> get(@PathVariable Integer id) {
        AdminDictMatch adminDictMatch = adminDictMatchService.getById(id);
        return Result.ok(adminDictMatch);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("字典转换数据保持或更新")
    @OperateLog(detail = "字典转换数据保持或更新", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result save(@RequestBody AdminDictMatch adminDictMatch) {
        UserInfo userInfo = UserUtil.getUser();
        if (adminDictMatch.getId() == null) {
            if(adminDictMatchService.save(adminDictMatch)){
                /*字典表关联对接数据 redis处理  邵化振*/
                AdminDictUtils.setDictMatchCache(userInfo.getTenantId().intValue(),adminDictMatch.getMatchDict(),adminDictMatch.getSelfDictValue(), JSON.toJSONString(adminDictMatch));
            }
        } else {
            if(adminDictMatchService.updateById(adminDictMatch)){
                AdminDictUtils.clearRelationDictCache(userInfo.getTenantId(),adminDictMatch.getMatchDict(),adminDictMatch.getSelfDictValue());
                AdminDictUtils.setDictMatchCache(userInfo.getTenantId().intValue(),adminDictMatch.getMatchDict(),adminDictMatch.getSelfDictValue(), JSON.toJSONString(adminDictMatch));
            }
        }
        return Result.ok();
    }

    @PostMapping("/delete/{selfDict}/{selfDictValue}")
    @ApiOperation("根据本系统类型和本系统值删除字典转数据")
    @OperateLog(detail = "根据本系统类型和本系统值删除字典转数据", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@PathVariable String selfDict, @PathVariable String selfDictValue) {
        Map<String, Object> where = new HashMap<>();
        where.put("self_dict",selfDict);
        where.put("self_dict_value",selfDictValue);
        adminDictMatchService.removeByMap(where);
        return R.ok();
    }

    @PostMapping("/delete/{id}")
    @ApiOperation("删除字典转数据")
    @OperateLog(detail = "删除字典转数据", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@PathVariable Integer id) {
        adminDictMatchService.removeById(id);
        return R.ok();
    }

    @PostMapping("/selectSuggestList")
    @ApiOperation("查询建议列表")
    @OperateLog(detail = "查询建议列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectSuggestList(@RequestBody Map<String, Object> params) {
        List<Map<String, Object>> list = adminDictMatchService.selectSuggestList(params);
        return Result.ok(list);
    }
}

