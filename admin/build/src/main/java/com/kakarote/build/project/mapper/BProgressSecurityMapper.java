package com.kakarote.build.project.mapper;

import com.kakarote.build.project.entity.BO.BProgressSecurityBO;
import com.kakarote.build.project.entity.PO.BProgressSecurity;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.feign.admin.entity.AdminMessage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 施工建筑安全检查 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2022-06-01
 */
public interface BProgressSecurityMapper extends BaseMapper<BProgressSecurity> {

    BasePage<BProgressSecurityBO> selectDataList(BasePage<AdminMessage> parse, @Param("data") BProgressSecurityBO bProgressSecurityBO);
}
