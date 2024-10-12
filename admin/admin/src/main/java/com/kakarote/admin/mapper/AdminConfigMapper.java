package com.kakarote.admin.mapper;

import com.baomidou.mybatisplus.annotation.SqlParser;
import com.kakarote.admin.entity.PO.AdminConfig;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 客户规则 Mapper 接口
 * </p>
 *
 * @author zhangzhiwei
 * @since 2020-04-27
 */

public interface AdminConfigMapper extends BaseMapper<AdminConfig> {

//    @SqlParser(filter = true)
    void updateAdminConfigNoTenantId(@Param("data") AdminConfig adminConfig);
}
