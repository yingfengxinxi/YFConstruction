package com.kakarote.build.qual.controller;


import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.qual.entity.PO.QualQuestions;
import com.kakarote.build.qual.service.IQualQuestionsService;
import com.kakarote.core.common.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 质量问题库 前端控制器
 * </p>
 *
 * @author pz
 * @since 2021-03-22
 */
@RestController
@RequestMapping("/qualQuestions")
public class QualQuestionsController {

    @Autowired
    private IQualQuestionsService qualQuestionsService;

    @GetMapping("/queryByParentId/{parentId}")
    @ApiOperation("根据上级查询质量问题库")
    @OperateLog(detail = "根据上级查询质量问题库", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<QualQuestions>> queryByParentId(@PathVariable("parentId") Integer parentId) {
        List<QualQuestions> list = qualQuestionsService.queryByParentId(parentId);
        return Result.ok(list);
    }

    @GetMapping("/getById/{questionId}")
    @ApiOperation("获取质量问题详情")
    public Result<Object> getMenuById(@PathVariable Integer questionId) {
        return Result.ok(qualQuestionsService.getById(questionId));
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存质量问题库")
    @OperateLog(detail = "保存质量问题库", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result<Long> saveOrUpdateMessage(@RequestBody QualQuestions qualQuestions) {
        qualQuestionsService.saveOrUpdate(qualQuestions);
        return Result.ok();
    }

    @GetMapping("/del/{questionId}")
    @ApiOperation("删除质量问题库")
    @OperateLog(detail = "删除质量问题库", types = LogTypes.BUILD, behavior = LogBehavior.DELETE)
    public Result del(@PathVariable Integer questionId) {
        return R.ok(qualQuestionsService.removeListById(questionId));
    }

    /**
     * 该接口查询的数据作为质量隐患检查下的隐患描述文本
     * 和问题类型数据使用
     * @return
     */
    @RequestMapping("/queryTextList")
    @ApiOperation("查询质量问题库所有数据")
    @OperateLog(detail = "查询质量问题库所有数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> queryTextList() {
        List<Map<String, Object>> list = qualQuestionsService.queryTextList();
        return Result.ok(list);
    }


    @RequestMapping("/queryTreeList")
    @ApiOperation("查询质量问题库树形数据")
    @OperateLog(detail = "查询质量问题库树形数据", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<List<Map<String, Object>>> queryTreeList() {
        List<Map<String, Object>> list = qualQuestionsService.queryAllList(0);
        return Result.ok(list);
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入质量问题")
    @OperateLog(detail = "excel导入质量问题", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file) {
        JSONObject object = qualQuestionsService.excelImport(file);
        return R.ok(object);
    }

    @PostMapping("/selectQualHiddenDanger")
    @ApiOperation("质量隐患-总数和未解决数")
    @OperateLog(detail = "质量隐患-总数和未解决数", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectQualHiddenDanger(@RequestBody Map<String,Object>map){
        List<Map<String, Object>> list = qualQuestionsService.selectQualHiddenDanger(map);
        return R.ok(list);
    }

    @PostMapping("/selectHiddenDangerNumber")
    @ApiOperation("质量隐患-总数和未解决数新版")
    @OperateLog(detail = "质量隐患-总数和未解决数新版", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result selectHiddenDangerNumber(@RequestBody Map<String,Object>map){
        List<Map<String, Object>> list = qualQuestionsService.selectHiddenDangerNumber(map);
        return R.ok(list);
    }
}

