package com.qdoner.message.mapper;

import com.kakarote.core.entity.BasePage;
import com.qdoner.message.entity.BO.MessageBO;
import com.qdoner.message.entity.PO.Message;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-04-08
 */
public interface MessageMapper extends BaseMapper<Message> {
    BasePage<Message> selectByPage(BasePage<Message> parse, @Param("data") MessageBO messageBO);
}
