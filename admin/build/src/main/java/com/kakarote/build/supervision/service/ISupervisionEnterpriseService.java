package com.kakarote.build.supervision.service;

import com.kakarote.build.supervision.entity.BO.SupervisionEnterpriseBO;
import com.kakarote.build.supervision.entity.PO.SupervisionEnterprise;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lzy
 * @since 2022-11-29
 */
public interface ISupervisionEnterpriseService extends BaseService<SupervisionEnterprise> {

    BasePage<Map<String,Object>> selectByPage(SupervisionEnterpriseBO supervisionEnterpriseBO);

    Map<String, Object> selectDataById(String id);

    void insertOrupdateData(SupervisionEnterprise supervisionEnterprise);

    List<Map<String, Object>> selectSupeList();

    List<SupervisionEnterprise> selectByProjectId(SupervisionEnterprise supervisionEnterprise);
}
