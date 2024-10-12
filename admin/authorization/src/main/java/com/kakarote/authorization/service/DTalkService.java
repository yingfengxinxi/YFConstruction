package com.kakarote.authorization.service;

import com.kakarote.core.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("dtalk")
public interface DTalkService {

    @GetMapping("/dtalk/{code}/{configId}")
    Result getPhone(@PathVariable("code") String code,@PathVariable("configId") String configId);
}
