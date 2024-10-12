package com.kakarote.build.eval.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kakarote.build.eval.entity.BO.EvalItemsBO;
import com.kakarote.build.eval.entity.BO.EvalLevelBO;
import com.kakarote.build.eval.entity.PO.EvalItems;
import com.kakarote.build.eval.entity.PO.EvalLevel;
import com.kakarote.build.eval.service.IEvalItemsService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lzy
 * @since 2022-02-22
 */
@RestController
@RequestMapping("/evalItems")
public class EvalItemsController {

    @Autowired
    private IEvalItemsService evalItemsService;

    @PostMapping("/list")
    @ApiOperation("查询安全检查计划")
    @OperateLog(detail = "查询安全检查计划", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<EvalItems>> list(@RequestBody EvalItemsBO evalItemsBO) {
        return Result.ok(evalItemsService.selectByPage(evalItemsBO));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新打分项数据")
    @OperateLog(detail = "保存或更新安全检查计划", types = LogTypes.BUILD, behavior = LogBehavior.INSERT_OR_UPDATE)
    public Result saveOrUpdate(@RequestBody EvalItems evalItems) {
        Long userId = UserUtil.getUser().getUserId();
        if (evalItems.getId() != null) {
            evalItems.setUpdateBy(userId);
            evalItems.setUpdateTime(new Date());
            evalItemsService.updateById(evalItems);
        } else {
            String uuid= UUID.randomUUID().toString().replaceAll("-", "");
            evalItems.setId(uuid);
            evalItems.setCreateBy(userId);
            evalItems.setCreateTime(new Date());
            evalItemsService.save(evalItems);
        }
        return Result.ok();
    }

    /**
     * 获取详情
     */
    @PostMapping("/getById/{id}")
    @ApiOperation("查询打分项数据")
    @OperateLog(detail = "查询打分项数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getById(@PathVariable String id) {
        return Result.ok(evalItemsService.getById(id));
    }

    /**
     * 更新打分项状态
     * id 必填
     * status 0:停用 1：启用 -1逻辑删除
     * @param evalItemsParams
     * @return
     */
    @PostMapping("/updateStatus")
    @ApiOperation("更新打分项状态")
    @OperateLog(detail = "更新打分项状态", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result updateStatus(@RequestBody EvalItems evalItemsParams) {

        if (evalItemsParams.getId() == null) {
            throw new CrmException(500, "参数id不能为null");
        }

        if (evalItemsParams.getStatus() == null || evalItemsParams.getStatus().equals("")) {
            throw new CrmException(500, "参数status不能为null");
        }

        EvalItems evalItems = new EvalItems();
        evalItems.setId(evalItemsParams.getId());
        evalItems.setStatus(evalItemsParams.getStatus());

        evalItemsService.updateById(evalItems);
        return Result.ok();
    }

    @PostMapping("/selectList")
    @ApiOperation("查询打分项列表")
    @OperateLog(detail = "查询打分项列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> selectList(@RequestBody EvalItemsBO evalItemsBO) {
        return Result.ok(evalItemsService.selectList(evalItemsBO));
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入")
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = evalItemsService.excelImport(file);
        return R.ok(object);
    }
}

