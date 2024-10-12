package com.kakarote.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.admin.entity.PO.AdminDept;
import com.kakarote.admin.entity.PO.AdminInstrumentInfo;
import com.kakarote.admin.entity.PO.AdminInstrumentRole;
import com.kakarote.admin.entity.PO.AdminMenu;
import com.kakarote.admin.mapper.AdminInstrumentInfoMapper;
import com.kakarote.admin.service.IAdminInstrumentInfoService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 仪表盘名称表 服务实现类
 * </p>
 *
 * @author author
 * @since 2020-12-08
 */
@Service
public class AdminInstrumentInfoServiceImpl extends BaseServiceImpl<AdminInstrumentInfoMapper, AdminInstrumentInfo> implements IAdminInstrumentInfoService {

//    @Override
//    public String getNameByModelId(Integer modelId) {
//        AdminInstrumentInfo adminInstrumentInfo = query().select("model_name").eq("model_id", modelId).one();
//        if (adminInstrumentInfo == null) {
//            return "";
//        }
//        return adminInstrumentInfo.getModelName();
//    }

    @Override
    public int update(AdminInstrumentInfo adminInstrumentInfo) {
        LambdaQueryWrapper<AdminInstrumentInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminInstrumentInfo::getModelId, adminInstrumentInfo.getModelId());
        AdminInstrumentInfo oldInfo = getBaseMapper().selectOne(wrapper);
        oldInfo.setModelName(adminInstrumentInfo.getModelName());
        return getBaseMapper().update(oldInfo,wrapper);
    }
}
