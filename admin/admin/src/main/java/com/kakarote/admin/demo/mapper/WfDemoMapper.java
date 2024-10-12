package com.kakarote.admin.demo.mapper;

import com.kakarote.admin.demo.entity.BO.WfDemoBO;
import com.kakarote.admin.demo.entity.PO.WfDemo;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * DEMO表 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2021-03-15
 */
public interface WfDemoMapper extends BaseMapper<WfDemo> {
    BasePage<WfDemo> selectByPage(BasePage<WfDemo> parse, @Param("data") WfDemoBO planBO);
}
