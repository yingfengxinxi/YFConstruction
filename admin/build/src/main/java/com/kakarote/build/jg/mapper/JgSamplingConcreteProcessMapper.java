package com.kakarote.build.jg.mapper;

import com.kakarote.build.jg.entity.BO.JgSamplingConcreteProcessBO;
import com.kakarote.build.jg.entity.PO.JgSamplingConcreteProcess;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2021-11-27
 */
public interface JgSamplingConcreteProcessMapper extends BaseMapper<JgSamplingConcreteProcess> {

    BasePage<JgSamplingConcreteProcess> selectByPage(BasePage<Object> parse, @Param("data") JgSamplingConcreteProcessBO samplingConcreteProcessBO);

    List<JgSamplingConcreteProcess> selectDataListPid(@Param("pid") String pid);
}
