package com.kakarote.build.wifi.controller;


import com.kakarote.build.wifi.entity.BO.WifiTestBO;
import com.kakarote.build.wifi.service.IWifiTestService;
import com.kakarote.core.common.*;
import com.kakarote.core.entity.BasePage;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 生成试卷 前端控制器
 * </p>
 *
 * @author wnj
 * @since 2021-05-25
 */
@RestController
@RequestMapping("/wifiTest")
public class WifiTestController {
    @Autowired
    private IWifiTestService iWifiTestService;
    @PostMapping("/getAllTestPaper")
    @ApiOperation("查询所有已生成的试卷列表")
    @OperateLog(detail = "查询所有试卷列表", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result<BasePage<Map<String,Object>>> getAllTestPaper(@RequestBody WifiTestBO wifiTestBO){
      BasePage<Map<String,Object>> list= iWifiTestService.getAllTestPaper(wifiTestBO);
        return R.ok(list);
    }
    @PostMapping("/getPaperDetail/{id}")
    @ApiOperation("查询试卷详情")
    @OperateLog(detail = "查询试卷详情", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getPaperDetail(@PathVariable("id") String testId){
        return R.ok(iWifiTestService.getPaperDetail(testId));
    }

    @PostMapping("/createTestPaper")
    @ApiOperation("生成试卷")
    @OperateLog(detail = "生成试卷并返回", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result createTestPaper(@RequestBody WifiTestBO wifiTestBO) throws  Exception{
        return R.ok( iWifiTestService.createTestPaper(wifiTestBO));
    }

    @PostMapping("/SubmitTestPaper")
    @ApiOperation("提交试卷")
    @OperateLog(detail = "提交试卷审阅并返回审阅结果", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result SubmitTestPaper(@RequestBody Map<String,Object> map) {
        return R.ok(iWifiTestService.SubmitTestPaper(map));
    }

    @PostMapping("/getMapByTestTypeToEcharts")
    @ApiOperation("教育答题统计")
    @OperateLog(detail = "教育答题统计", types = LogTypes.BUILD, behavior = LogBehavior.SELECT)
    public Result getMapByTestTypeToEcharts(){
        Map<String, Object> mapByWorkTypeIdToEcharts = iWifiTestService.getMapByWorkTypeIdToEcharts();
        return R.ok(mapByWorkTypeIdToEcharts);
    }


}

