package com.kakarote.build.qual.controller;


import com.kakarote.build.qual.entity.BO.QualCheckBO;
import com.kakarote.build.qual.entity.PO.QualCheck;
import com.kakarote.build.qual.service.IQualCheckService;
import com.kakarote.build.qual.service.IQualHiddenTroubleService;
import com.kakarote.build.utils.CodeUtil;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.service.ISequenceService;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--质量检查管理 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-03-23
 */
@RestController
@RequestMapping("/buildQualCheck")
public class QualCheckController {

    @Autowired
    private IQualCheckService qualCheckService;
    @Autowired
    private ISequenceService sequenceService;

    @Autowired
    private IQualHiddenTroubleService hiddenTroubleService;

    @PostMapping("/list")
    @ApiOperation("查询质量检查列表")
    @OperateLog(detail = "查询质量检查列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<QualCheck>> list(@RequestBody QualCheckBO qualCheckBO) {
        return Result.ok( qualCheckService.selectByPage(qualCheckBO));
    }

    @PostMapping("/generateDocumentNumber")
    @ApiOperation("质量检查生成单据编号")
    @OperateLog(detail = "查询质量检查列表", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<String> generateDocumentNumber() {
//        String code = "";
//        List<QualCheck>list =qualCheckService.lambdaQuery().orderByDesc(QualCheck::getCreateTime).list();
//        if (list.size() == 0) {
//            code = CodeUtil.getCode(null);
//        } else {
//            QualCheck qualCheck = list.get(0);
//            code = CodeUtil.getCode(qualCheck.getQualDocumentNumber());
//        }
        UserInfo userInfo = UserUtil.getUser();
        Integer projectId = userInfo.getProjectId().intValue();
        return Result.ok(sequenceService.getDateSeq("1", projectId));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存质量检查信息")
    @OperateLog(detail = "保存质量检查信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result saveOrUpdate(@RequestBody Map<String, Object> params) {
        return Result.ok(qualCheckService.saveOrUpdateInfo(params));
    }

    @PostMapping("/receiptInfo")
    @ApiOperation("质量检查回执信息")
    @OperateLog(detail = "质量检查回执信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result receiptInfo(@RequestBody Map<String, Object> params) {
        return Result.ok(qualCheckService.receiptInfo(params));
    }

    @PostMapping("/getById/{checkId}")
    @ApiOperation("根据Id查询质量检查信息")
    @OperateLog(detail = "根据Id查询质量检查信息", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable @NotNull Integer checkId) {
        return Result.ok(qualCheckService.seletcCheckInfoById(checkId));
    }

    /**
     * 删除质量检查信息
     */
    @PostMapping("/delTrueByIds")
    @ApiOperation("删除质量检查信息")
    @OperateLog(detail = "删除质量检查信息", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result delete(@ApiParam(name = "ids", value = "id列表") @RequestBody List<String> ids) {
        for (String checkId : ids) {
            // 根据checkId删除隐患信息
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("check_id", checkId);
            hiddenTroubleService.removeByMap(columnMap);
            // 删除基本信息
            qualCheckService.removeById(checkId);
        }
        return Result.ok();
    }

    @PostMapping("/getPrintData/{id}")
    @ApiOperation("查询整改通知单数据")
    @OperateLog(detail = "查询整改通知单数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getPrintData(@PathVariable @NotNull Integer id) {
        Map<String, Object> data = qualCheckService.getPrintData(id);
        return Result.ok(data);
    }
}

