package com.kakarote.build.wxapp.service.impl;

import com.kakarote.build.wxapp.entity.BO.BWxuserBindBO;
import com.kakarote.build.wxapp.entity.PO.BWxuserBind;
import com.kakarote.build.wxapp.mapper.BWxuserBindMapper;
import com.kakarote.build.wxapp.service.IBWxuserBindService;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 微信用户绑定表
 服务实现类
 * </p>
 *
 * @author wudw
 * @since 2022-03-10
 */
@Service
public class BWxuserBindServiceImpl extends BaseServiceImpl<BWxuserBindMapper, BWxuserBind> implements IBWxuserBindService {
    @Override
    public BasePage<BWxuserBind> selectByPage(BWxuserBindBO planBO){
        return this.getBaseMapper().selectByPage(planBO.parse(),planBO);
    }
}
