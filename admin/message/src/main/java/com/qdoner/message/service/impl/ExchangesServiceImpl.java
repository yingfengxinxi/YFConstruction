package com.qdoner.message.service.impl;

import com.kakarote.core.entity.BasePage;
import com.qdoner.message.entity.BO.ExchangesBO;
import com.qdoner.message.entity.PO.Exchanges;
import com.qdoner.message.mapper.ExchangesMapper;
import com.qdoner.message.service.IExchangesService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2021-04-14
 */
@Service
public class ExchangesServiceImpl extends BaseServiceImpl<ExchangesMapper, Exchanges> implements IExchangesService {

    @Override
    public BasePage<Exchanges> queryPageList(ExchangesBO exchangesBO){
        return this.getBaseMapper().selectByPage(exchangesBO.parse(),exchangesBO);
    }
}
