package com.kakarote.build.safety.service;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.safety.entity.BO.SafetyDynamicRiskBO;
import com.kakarote.build.safety.entity.PO.SafetyDynamicRisk;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 动态风险数据 服务类
 * </p>
 *
 * @author shz
 * @since 2021-11-10
 */
public interface ISafetyDynamicRiskService extends BaseService<SafetyDynamicRisk> {

    BasePage<SafetyDynamicRisk> selectList(SafetyDynamicRiskBO safetyDynamicRisk);

    /**
     * excel导入
     * @param file file
     */
    public JSONObject excelImport(MultipartFile file);
}
