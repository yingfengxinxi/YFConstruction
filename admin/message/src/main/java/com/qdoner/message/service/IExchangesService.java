package com.qdoner.message.service;

import com.kakarote.core.entity.BasePage;
import com.qdoner.message.entity.BO.ExchangesBO;
import com.qdoner.message.entity.PO.Exchanges;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wudw
 * @since 2021-04-14
 */
public interface IExchangesService extends BaseService<Exchanges> {

    BasePage<Exchanges> queryPageList(ExchangesBO exchangesBO);

}
