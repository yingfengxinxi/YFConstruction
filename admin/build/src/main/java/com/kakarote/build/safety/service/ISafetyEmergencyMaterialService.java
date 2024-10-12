package com.kakarote.build.safety.service;

import com.kakarote.build.safety.entity.BO.BSafetySafetyDataBO;
import com.kakarote.build.safety.entity.BO.SafetyEmergencyMaterialBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyMaterial;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 应急物资管理 服务类
 * </p>
 *
 * @author wnj
 * @since 2021-04-06
 */
public interface ISafetyEmergencyMaterialService extends BaseService<SafetyEmergencyMaterial> {
    /**
     * 查询数据列表
     *
     */
    public BasePage<SafetyEmergencyMaterial> selectList(SafetyEmergencyMaterialBO SafetyEmergencyMaterialBO);
    /**
     * 新增数据信息
     *
     */
    public  int add(SafetyEmergencyMaterial safetyEmergencyMaterial);

    public  int edit(SafetyEmergencyMaterial safetyEmergencyMaterial);
    public  int delete(String id );

    public SafetyEmergencyMaterial selectById(String id);
}
