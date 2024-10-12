package com.kakarote.admin.mapper;

import com.kakarote.admin.entity.BO.AdminDeptConfigBO;
import com.kakarote.admin.entity.PO.AdminDeptConfig;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author author
 * @since 2020-12-16
 */
public interface AdminDeptConfigMapper extends BaseMapper<AdminDeptConfig> {

    BasePage<AdminDeptConfig> queryList(BasePage<AdminDeptConfig> page, @Param("data") AdminDeptConfigBO adminDeptConfigBO);

    List<AdminDeptConfig> checkList(AdminDeptConfig adminDeptConfig);
}
