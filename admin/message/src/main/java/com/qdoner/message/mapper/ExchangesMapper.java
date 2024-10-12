package com.qdoner.message.mapper;

import com.kakarote.core.entity.BasePage;
import com.qdoner.message.entity.BO.ExchangesBO;
import com.qdoner.message.entity.PO.Exchanges;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-04-14
 */
public interface ExchangesMapper extends BaseMapper<Exchanges> {

    BasePage<Exchanges> selectByPage(BasePage<Exchanges> parse, @Param("data") ExchangesBO planBO);

}
