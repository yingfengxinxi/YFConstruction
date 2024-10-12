package com.kakarote.build.jg.mapper;

import com.kakarote.build.jg.entity.BO.JgSamplingConcreteBO;
import com.kakarote.build.jg.entity.PO.JgSamplingConcrete;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-11-27
 */
public interface JgSamplingConcreteMapper extends BaseMapper<JgSamplingConcrete> {

    BasePage<JgSamplingConcrete> selectByPage(BasePage<Object> parse, @Param("data") JgSamplingConcreteBO samplingConcreteBO);
}
