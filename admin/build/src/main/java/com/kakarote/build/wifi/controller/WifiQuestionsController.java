package com.kakarote.build.wifi.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.kakarote.build.labour.entity.BO.LabourSpeworktypeBO;
import com.kakarote.build.labour.entity.PO.LabourSpeworktype;
import com.kakarote.build.project.entity.PO.ProgressModelset;
import com.kakarote.build.project.service.IProgressModelsetDetailsService;
import com.kakarote.build.project.service.IProgressModelsetService;
import com.kakarote.build.wifi.entity.BO.WifiQuestionsBO;
import com.kakarote.build.wifi.entity.PO.WifiQuestions;
import com.kakarote.build.wifi.service.IWifiQuestOptionService;
import com.kakarote.build.wifi.service.IWifiQuestionsService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * wifi教育答题--问题表 前端控制器
 * </p>
 *
 * @author zy
 * @since 2021-05-25
 */
@RestController
@RequestMapping("/wifiQuestions")
public class WifiQuestionsController {
    @Autowired
    private IWifiQuestionsService iWifiQuestionsService;
    @Autowired
    private IWifiQuestOptionService iWifiQuestOptionService;

    @PostMapping("/getQuestionList")
    @ApiOperation("查询信息列表")
    @OperateLog(detail = "查询信息列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> getQuestionList(@RequestBody WifiQuestionsBO wifiQuestionsBO) {
        BasePage<Map<String,Object>> list = iWifiQuestionsService.selectList(wifiQuestionsBO);
        return Result.ok(list);
    }

    @PostMapping("/save")
    @ApiOperation("新增信息")
    @Transactional
    @OperateLog(detail = "新增信息", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result edit(@Validated @RequestBody WifiQuestions wifiQuestions) {
        iWifiQuestionsService.save(wifiQuestions);
        return Result.ok(wifiQuestions.getId());
    }

    @PostMapping("/updateById")
    @ApiOperation("修改信息")
    @Transactional
    @OperateLog(detail = "修改信息", types = LogTypes.BUILD, behavior = LogBehavior.UPDATE)
    public Result updateById(@Validated @RequestBody WifiQuestions wifiQuestions) {
        boolean b = iWifiQuestionsService.updateById(wifiQuestions);
        return Result.ok(wifiQuestions.getId());
    }

    @PostMapping("/selectModels")
    @ApiOperation("查询问题表")
    @OperateLog(detail = "查询问题表",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectModels(){
        List<WifiQuestions> list = iWifiQuestionsService.query().select("id", "name").ne("project_id",0).orderByDesc("create_time").list();
        return Result.ok(list);
    }

    /**
     * 删除方法 已屏蔽tenant_id 和 project_id
     * @param id
     * @return
     */
    @PostMapping("/delById/{id}")
    @ApiOperation("根据主键删除信息")
    @Transactional
    @OperateLog(detail = "根据主键删除信息",types = LogTypes.BUILD,behavior = LogBehavior.DELETE)
    public Result delById(@PathVariable String id){

        iWifiQuestOptionService.deleteByQuestId(id);

        iWifiQuestionsService.deleteQuestionById(id);

        return Result.ok();
    }

    @PostMapping("/selectById/{id}")
    @ApiOperation("根据主键查询信息")
    @Transactional
    @OperateLog(detail = "根据主键查询信息",types = LogTypes.BUILD,behavior = LogBehavior.SELECT)
    public Result selectById(@PathVariable String id){

        WifiQuestions one = iWifiQuestionsService.query()
                .eq("id", id)
                .ne("tenant_id",0)
                .and(wrapper -> wrapper
                        .ne("project_id", 0)
                        .or()
                        .isNull("project_id")
                ).
                one();

//        WifiQuestions byId = iWifiQuestionsService.getById(id);
        return Result.ok(one);
    }

    @PostMapping("/excelImport")
    @ApiOperation("excel导入wifi题库")
    @OperateLog(detail = "excel导入wifi题库", types = LogTypes.BUILD, behavior = LogBehavior.INSERT)
    public Result<JSONObject> excelImport(@RequestParam("file") MultipartFile file,
                                          @RequestParam("questTypeId") @NotNull String questTypeId) throws Exception{
        JSONObject object = iWifiQuestionsService.excelImport(file,questTypeId);
        return R.ok(object);
    }


}

