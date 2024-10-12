package com.kakarote.build.ai.controller;



import com.kakarote.build.ai.entity.BO.BAiDregCarCleaningBO;
import com.kakarote.build.ai.entity.PO.BAiDregCarCleaning;
import com.kakarote.build.ai.service.IBAiDregCarCleaningService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 车辆清洗记录 前端控制器
 * </p>
 *
 * @author author
 * @since 2021-09-22
 */
@RestController
@RequestMapping("/bAiDregCarCleaning")
public class BAiDregCarCleaningController {

    @Autowired
    private IBAiDregCarCleaningService iBAiDregCarCleaningService;

    @PostMapping("/selectByPage")
    @ApiOperation("查询渣土车记录列表")
    @OperateLog(detail = "查询渣土车记录列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<BAiDregCarCleaning>> selectByPage(@RequestBody BAiDregCarCleaningBO bAiDregCarCleaningBO){
        return Result.ok(iBAiDregCarCleaningService.selectByPage(bAiDregCarCleaningBO));
    }


    /**
     * 获取详情
     */

    @GetMapping("/selectById/{id}")
    public Result getById(@PathVariable String id) {
        return Result.ok(iBAiDregCarCleaningService.getById(id));
    }

    /**
     * 该接口宁津相关项目使用
     *
     * @return
     */
    @PostMapping("/selectTypeProportion")
    @ApiOperation("智能分析-车辆清洗报警类型占比")
    @OperateLog(detail = "报警数据分析-车辆清洗报警类型占比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectTypeProportion() {
        return Result.ok(iBAiDregCarCleaningService.selectTypeProportion());
    }

    /**
     * 查询前10名车牌号报警数量对比
     *
     * @return
     */
    @PostMapping("/selectTop10List")
    @ApiOperation("AI识别报警-查询前10名车牌号报警数量对比")
    @OperateLog(detail = "AI识别报警-查询前10名车牌号报警数量对比", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectTop10List() {
        return Result.ok(iBAiDregCarCleaningService.selectTop10List());
    }

}

