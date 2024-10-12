package com.kakarote.core.message;

import com.kakarote.core.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 统一消息推送接口
 */
@FeignClient(name = "message", contextId = "messageSend")
public interface MessageFeignService {

    /**
     * 推送消息
     *
     * @param messageEntity
     * @return
     */
    @PostMapping("/msg")
    Result send(@RequestBody MessageEntity messageEntity);

    /**
     * 推送消息
     */
    @PostMapping("/msg/registerCid")
    Result registerCid(@RequestBody Map<String,String> map);

    /**
     * 查询消息类型推送人员
     */
    @PostMapping("/msgQueues/getQueuesUserList/{code}")
    List<Long> getQueuesUserList(@PathVariable("code") String code);

    /**
     * 查询消息类型推送人员,需传递租户id的调用
     */
    @PostMapping("/msgQueues/getQueuesUserList/{code}/{tenantId}")
    List<Long> getQueuesUserList(@PathVariable("code") String code, @PathVariable("tenantId") Integer tenantId);
}
