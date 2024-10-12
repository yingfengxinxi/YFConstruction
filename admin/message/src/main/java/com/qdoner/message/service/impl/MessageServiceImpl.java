package com.qdoner.message.service.impl;

import com.kakarote.core.entity.BasePage;
import com.kakarote.core.utils.UserUtil;
import com.qdoner.message.entity.BO.MessageBO;
import com.qdoner.message.entity.PO.Message;
import com.qdoner.message.mapper.MessageMapper;
import com.qdoner.message.service.IMessageService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-04-08
 */
@Service
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, Message> implements IMessageService {

    @Override
    public BasePage<Message> queryPageList(MessageBO messageBO) {
        if(null == messageBO.getToUser()){//考虑指定用户的情况
            messageBO.setToUser(UserUtil.getUserId());
        }
        return this.getBaseMapper().selectByPage(messageBO.parse(), messageBO);
    }
}
