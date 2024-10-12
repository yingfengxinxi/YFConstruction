package com.kakarote.build.eval.controller;


import com.kakarote.build.eval.entity.PO.EvalApplyItems;
import com.kakarote.build.eval.service.IEvalApplyItemsService;
import com.kakarote.core.common.LogBehavior;
import com.kakarote.core.common.LogTypes;
import com.kakarote.core.common.OperateLog;
import com.kakarote.core.common.Result;
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
 *  前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-02-23
 */
@RestController
@RequestMapping("/evalApplyItems")
public class EvalApplyItemsController {

    @Autowired
    private IEvalApplyItemsService applyItemsService;

    @PostMapping("/updateItemList")
    @ApiOperation("更新打分项数据")
    @OperateLog(detail = "更新打分项数据", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result updateItemList(@RequestBody List<EvalApplyItems> params) {
        return Result.ok(applyItemsService.updateItemList(params));
    }

}

