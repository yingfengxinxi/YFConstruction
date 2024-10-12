package com.kakarote.build.qual.controller;

import com.kakarote.build.qual.service.IQualHiddenTroubleService;
import com.kakarote.core.common.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 施工质量--质量检查管理--安全隐患 前端控制器
 * </p>
 *
 * @author lzy
 * @since 2021-03-23
 */
@RestController
@RequestMapping("/buildQualHiddenTrouble")
public class QualHiddenTroubleController {

    @Autowired
    private IQualHiddenTroubleService hiddenTroubleService;


    /**
     * 质量隐患按负责人分析
     * @return
     */
    @PostMapping("/selectQualDataByPrinipal")
    @ApiOperation("质量隐患按负责人分析")
    @OperateLog(detail = "质量隐患按负责人分析", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectQualDataByPrinipal(){
        List<Map<String, Object>> list = hiddenTroubleService.selectQualDataByPrinipal();
        return R.ok(list);
    }

    /**
     * 质量问题数量
     * @return
     */
    @PostMapping("/selectQualQuestionsQuantity")
    @ApiOperation("质量问题数量")
    @OperateLog(detail = "质量问题数量", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectQualQuestionsQuantity(){
        Map<String, Object> list = hiddenTroubleService.selectQualQuestionsQuantity();
        return R.ok(list);
    }
}

