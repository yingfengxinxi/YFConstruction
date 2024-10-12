package com.qdoner.message.controller;

import com.kakarote.core.common.Result;
import com.kakarote.core.message.MessageEntity;
import com.kakarote.core.message.MessageFeignService;
import com.kakarote.core.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开发测试消息类型
 */
@RestController
@RequestMapping("/msgTest")
public class TestContrller {
    @Autowired
    private MessageFeignService messageFeignService;
    /**
     * 获取详情
     */
    @PostMapping("/send")
    public Result getById(@RequestBody MessageEntity entity) {
        //当前用户
        entity.setTenantId(Integer.parseInt(UserUtil.getUser().getTenantId().toString()));
        entity.setProjectId(UserUtil.getUser().getProjectId());
        Result result = messageFeignService.send(entity);
        return result;
    }
}
