package com.qdoner.message.service;

import com.kakarote.core.entity.BasePage;
import com.qdoner.message.entity.BO.QueuesBO;
import com.qdoner.message.entity.PO.Queues;
import com.kakarote.core.servlet.BaseService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wudw
 * @since 2021-04-14
 */
public interface IQueuesService extends BaseService<Queues> {

    Queues queryByCode(String code);

    List<Queues> queryList();

    BasePage<Queues> queryPageList(QueuesBO queuesBO);

    BasePage<Queues> newQueryPageList(QueuesBO queuesBO);

    List<Long> getQueuesUserList(String code);

    List<Long> getQueuesUserList(String code, Integer tenantId);
}
