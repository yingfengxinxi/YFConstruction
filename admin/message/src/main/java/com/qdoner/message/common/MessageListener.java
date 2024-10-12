package com.qdoner.message.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * mq消息绑定及监听
 * 公告测试
 */
@Slf4j
@Component
public class MessageListener {
    /**
     * 公告
     *
     * @param params
     */
//    @RabbitListener(queues = "SYS_XTGG")
//    public void notice(Map<String, Object> params) {
//        log.info("MQ-Notice：" + params);
//    }

}
