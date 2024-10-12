package com.qdoner.message.mapper;

import com.kakarote.core.entity.BasePage;
import com.qdoner.message.entity.BO.QueuesBO;
import com.qdoner.message.entity.PO.Queues;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-04-14
 */
public interface QueuesMapper extends BaseMapper<Queues> {

    @Select("select * from wm_queues where code = #{code} and tenant_id != -1")
    List<Queues> queryByCode(@Param("code") String code);

    BasePage<Queues> selectByPage(BasePage<Queues> parse, @Param("data")QueuesBO queuesBO);
}
