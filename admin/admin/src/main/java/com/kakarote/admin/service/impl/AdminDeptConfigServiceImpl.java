package com.kakarote.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kakarote.admin.common.AdminDictUtils;
import com.kakarote.admin.entity.BO.AdminCompanyBO;
import com.kakarote.admin.entity.BO.AdminDeptConfigBO;
import com.kakarote.admin.entity.PO.AdminDeptConfig;
import com.kakarote.admin.entity.PO.AdminRole;
import com.kakarote.admin.entity.VO.AdminUserVO;
import com.kakarote.admin.mapper.AdminDeptConfigMapper;
import com.kakarote.admin.service.IAdminDeptConfigService;
import com.kakarote.core.common.Result;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import com.kakarote.core.utils.UserCacheUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2020-12-16
 */
@Service
public class AdminDeptConfigServiceImpl extends BaseServiceImpl<AdminDeptConfigMapper, AdminDeptConfig> implements IAdminDeptConfigService {

    @Override
    public BasePage<AdminDeptConfig> queryConfigList(AdminDeptConfigBO adminDeptConfigBO) {

        BasePage<AdminDeptConfig> basePage = getBaseMapper().queryList(adminDeptConfigBO.parse(), adminDeptConfigBO);
        return basePage;
    }

    @Override
    public Result addOrUpdateConfig(AdminDeptConfig adminDeptConfig) {

        for(String t : adminDeptConfig.getMessageType().split(",")){
            AdminDeptConfig temp = BeanUtil.copyProperties(adminDeptConfig,AdminDeptConfig.class);
            temp.setMessageType(t);
            List<AdminDeptConfig> list = getBaseMapper().checkList(temp);
            if(list.size()>0){
                return Result.error(200,"选择的消息类型【"+ AdminDictUtils.getDictLabel("sys_message_type",t) +"】已在其他应用中配置");
            }
        }
        saveOrUpdate(adminDeptConfig);
        return Result.ok();
    }
}
