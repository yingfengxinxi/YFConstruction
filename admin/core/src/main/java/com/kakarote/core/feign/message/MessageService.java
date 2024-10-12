package com.kakarote.core.feign.message;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "message")
public interface MessageService {

}
