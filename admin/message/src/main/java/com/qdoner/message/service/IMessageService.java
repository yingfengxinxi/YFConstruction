package com.qdoner.message.service;

import com.kakarote.core.entity.BasePage;
import com.qdoner.message.entity.BO.MessageBO;
import com.qdoner.message.entity.PO.Message;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-04-08
 */
public interface IMessageService extends BaseService<Message> {
    BasePage<Message> queryPageList(MessageBO messageBO);
}
