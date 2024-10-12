package com.kakarote.build.supervision.mapper;

import com.kakarote.build.supervision.entity.BO.SupervisionEnterpriseBO;
import com.kakarote.build.supervision.entity.PO.SupervisionEnterprise;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lzy
 * @since 2022-11-29
 */
public interface SupervisionEnterpriseMapper extends BaseMapper<SupervisionEnterprise> {

    BasePage<Map<String,Object>> selectByPage(BasePage<Object> parse, @Param("data") SupervisionEnterpriseBO supervisionEnterpriseBO);

    Map<String, Object> selectDataById(@Param("id") String id);

    List<Map<String, Object>> selectSupeList();

    List<SupervisionEnterprise> selectByProjectId(@Param("data")  SupervisionEnterprise supervisionEnterprise);
}
