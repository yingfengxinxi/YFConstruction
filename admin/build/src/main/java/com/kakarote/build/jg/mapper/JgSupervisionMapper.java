package com.kakarote.build.jg.mapper;

import com.kakarote.build.jg.entity.BO.JgSupervisionBO;
import com.kakarote.build.jg.entity.PO.JgSupervision;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 现场安全监督 Mapper 接口
 * </p>
 *
 * @author wnj
 * @since 2021-11-25
 */
public interface JgSupervisionMapper extends BaseMapper<JgSupervision> {

    BasePage<JgSupervision> selectByPage(BasePage<Object> parse,@Param("data") JgSupervisionBO jgSupervisionBO);
}
