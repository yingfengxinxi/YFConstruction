package com.kakarote.core.feign.work;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.entity.PageEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "work",contextId = "taskJob")
public interface WorkService {

    @PostMapping("/workTask/updateTaskJob")
    Result updateTaskJob(@RequestParam("host") String host);

    @PostMapping("/work/queryProject")
    Result<BasePage<Map<String, Object>>> queryProject(@RequestParam("customerId") Integer customerId, PageEntity pageEntityCopy);

    @GetMapping("/workTask/getTaskInfo/{tenantId}/{taskId}")
    public JSONObject getTaskInfo(@PathVariable("tenantId") Long tenantId, @PathVariable("taskId") Integer taskId);

    @PostMapping("/workTaskComment/getComment/{tenantId}/{commentId}")
    public JSONObject getComment(@PathVariable("tenantId") Long tenantId, @PathVariable("commentId") Integer commentId);
}
