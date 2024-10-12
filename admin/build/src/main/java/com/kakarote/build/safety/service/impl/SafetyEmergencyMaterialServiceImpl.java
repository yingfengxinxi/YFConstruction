package com.kakarote.build.safety.service.impl;

import com.kakarote.build.safety.entity.BO.SafetyEmergencyMaterialBO;
import com.kakarote.build.safety.entity.PO.BSafetySafetyData;
import com.kakarote.build.safety.entity.PO.SafetyEmergencyMaterial;
import com.kakarote.build.safety.mapper.SafetyEmergencyMaterialMapper;
import com.kakarote.build.safety.service.ISafetyEmergencyMaterialService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 应急物资管理 服务实现类
 * </p>
 *
 * @author wnj
 * @since 2021-04-06
 */
@Service
public class SafetyEmergencyMaterialServiceImpl extends BaseServiceImpl<SafetyEmergencyMaterialMapper, SafetyEmergencyMaterial> implements ISafetyEmergencyMaterialService {
    /**
     * 查询数据列表
     *
     */
    @Override
    public BasePage<SafetyEmergencyMaterial> selectList(SafetyEmergencyMaterialBO SafetyEmergencyMaterialBO) {
        BasePage<SafetyEmergencyMaterial> adminMessageBasePage = getBaseMapper().selectDataList(SafetyEmergencyMaterialBO.parse(),SafetyEmergencyMaterialBO);
        return adminMessageBasePage;
    }
    /**
     * 新增数据信息
     *
     */
    @Override
    public int add(SafetyEmergencyMaterial safetyEmergencyMaterial) {
        boolean yz = save(safetyEmergencyMaterial);
        int row=1;
        return row;
    }

    /**
     * 编辑单条数据信息
     * @param safetyEmergencyMaterial
     * @return
     */
    @Override
    public int edit(SafetyEmergencyMaterial safetyEmergencyMaterial) {
        boolean yz =updateById(safetyEmergencyMaterial);
        int row =1;
        return row;
    }

    /**
     * 删除单条数据信息
     *
     */
    @Override
    public int delete(String id)
    {
        boolean yz = removeById(id);
        int row =1;
        return row;
    }

    /**
     * 查询单条数据信息
     *
     */
    @Override
    public SafetyEmergencyMaterial selectById(String id)
    {
        return getById(id);
    }
}
