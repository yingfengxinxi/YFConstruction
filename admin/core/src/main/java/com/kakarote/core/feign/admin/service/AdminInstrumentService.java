package com.kakarote.core.feign.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "admin",contextId = "adminInstrument")
public interface AdminInstrumentService {

    @PostMapping("/adminInstrumentRole/queryModelByRoleIds")
    public JSONObject queryModelByRoleIds(@RequestBody List<Integer> roleIds);

//    @GetMapping("/adminInstrument/getNameByModelId/{modelId}")
//    public String getNameByModelId(@PathVariable("modelId") Integer modelId);

    @PostMapping("/adminInstrumentRole/getMaxDataType")
    public String getMaxDataType(@RequestBody List<Integer> roleIds);
}
