package com.kakarote.core.feign.job;

import com.kakarote.core.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "xxl-job-admin",contextId = "xxl-job-admin")
public interface JobService {

    @PostMapping("/jobinfo/listInfoAll")
    public Result listJobInfoAll();
}
