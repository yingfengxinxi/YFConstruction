package com.kakarote.build.safety.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.safety.entity.BO.SafetyDynamicRiskBO;
import com.kakarote.build.safety.entity.PO.SafetyDynamicRisk;
import com.kakarote.build.safety.entity.PO.SafetyStaticRisk;
import com.kakarote.build.safety.service.impl.SafetyDynamicRiskServiceImpl;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.service.ISequenceService;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 动态风险数据 前端控制器
 * </p>
 *
 * @author shz
 * @since 2021-11-10
 */

@RestController
@RequestMapping("/safetyDynamicRisk")
public class SafetyDynamicRiskController {

    @Autowired
    private SafetyDynamicRiskServiceImpl safetyDynamicRiskService;

    @Autowired
    private ISequenceService sequenceService;

    @RequestMapping("/list")
    @ApiOperation("查询动态风险数据列表")
    @OperateLog(detail = "查询动态风险数据列表",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result<BasePage<SafetyDynamicRisk>> list(@RequestBody SafetyDynamicRiskBO safetyDynamicRisk)
    {
        BasePage<SafetyDynamicRisk> list = safetyDynamicRiskService.selectList(safetyDynamicRisk);
        return Result.ok(list);
    }

    @RequestMapping("/add")
    @ApiOperation("新增动态风险数据")
    @OperateLog(detail = "新增动态风险数据",types= LogTypes.SYS ,behavior = LogBehavior.INSERT)
    public Result add(@RequestBody SafetyDynamicRisk safetyDynamicRisk)
    {
        safetyDynamicRisk.setPublishDate(new Date());
        safetyDynamicRisk.setCreateBy(UserUtil.getUserId());
        safetyDynamicRiskService.save(safetyDynamicRisk);
        return Result.ok();
    }

    @PostMapping("/generateDocumentNumber")
    @ApiOperation("生成单据编号")
    @OperateLog(detail = "查询质量检查列表", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<String> generateDocumentNumber() {
        UserInfo userInfo = UserUtil.getUser();
        Integer projectId = userInfo.getProjectId().intValue();
        return Result.ok("DR" + sequenceService.getDateSeq("6", projectId));
    }

    @RequestMapping("/edit")
    @ApiOperation("修改动态风险数据")
    @OperateLog(detail = "修改动态风险数据",types= LogTypes.SYS ,behavior = LogBehavior.UPDATE)
    public Result edit(@RequestBody SafetyDynamicRisk safetyDynamicRisk)
    {
        safetyDynamicRisk.setUpdateDate(new Date());
        safetyDynamicRiskService.saveOrUpdate(safetyDynamicRisk);
        return Result.ok();
    }

    @GetMapping(value = "/selectById/{id}")
    @ApiOperation("查询动态风险数据详细")
    @OperateLog(detail = "查询动态风险数据详细",types= LogTypes.SYS ,behavior = LogBehavior.SELECT)
    public Result getInfo(@PathVariable String id)
    {
        return Result.ok(safetyDynamicRiskService.getById(id));
    }


    @GetMapping(value = "/deleteDetails/{id}")
    @ApiOperation("删除安全风险分级管控数据详情")
    @OperateLog(detail = "删除安全风险分级管控数据详情",types= LogTypes.SYS ,behavior = LogBehavior.DELETE)
    public Result remove(@PathVariable String id)
    {
        return Result.ok( safetyDynamicRiskService.removeById(id));
    }

    /**
     * 下拉框使用
     * @return
     */
    @PostMapping("/selectList")
    @ApiOperation("查询静态风险源列表，下拉框使用")
    @OperateLog(detail = "查询静态风险源列表，下拉框使用", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectList() {

        QueryWrapper<SafetyDynamicRisk> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id, risk_num, risk_name AS name").orderByAsc("publish_date");
        List<Map<String, Object>> list = safetyDynamicRiskService.listMaps(queryWrapper);
        return Result.ok(list);
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入考勤")
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = safetyDynamicRiskService.excelImport(file);
        return R.ok(object);
    }
}

