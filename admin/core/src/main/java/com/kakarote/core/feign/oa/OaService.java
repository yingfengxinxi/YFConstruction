package com.kakarote.core.feign.oa;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import com.kakarote.core.feign.oa.entity.ExamineVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "oa",contextId = "eventJob")
public interface OaService {
    @PostMapping("/oaEventJob/eventNoticeCron")
    Result eventNoticeCron();

    @PostMapping("/oaExamine/transfer")
    @ApiOperation("转换审批")
    public Result<List<ExamineVO>> transfer(@RequestBody List<ExamineVO> recordList);

    @GetMapping("/oadata/initData/{tenantId}")
    @ApiOperation("初始化OA数据")
    Result initData(@PathVariable("tenantId") Integer tenantId);

    @PostMapping("/oaExamine/queryMoney")
    Result<BasePage<Map<String, Object>>> queryMoney(@RequestParam("customerId") Integer customerId, PageEntity pageEntityCopy);

    @PostMapping("/oaExamine/contractQueryMoney")
    Result<BasePage<Map<String, Object>>> contractQueryMoney(@RequestParam("contractId") Integer contractId, PageEntity pageEntityCopy);

    @PostMapping("/oaExamine/businessQueryMoney")
    Result<BasePage<Map<String, Object>>> businessQueryMoney(@RequestParam("businessId") Integer businessId, PageEntity pageEntityCopy);

    @GetMapping("/oaExamine/getOaExamineInfo/{tenantId}/{examineId}")
    Result<JSONObject> getOaExamineInfo(@PathVariable("tenantId") Long tenantId, @PathVariable("examineId") String examineId);

    @PostMapping("/oaExamine/queryExamineRecordList")
    Result<JSONObject> queryExamineRecordList(@RequestParam("recordId") String recordId);
}
