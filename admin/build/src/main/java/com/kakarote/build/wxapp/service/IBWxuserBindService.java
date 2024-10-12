package com.kakarote.build.wxapp.service;

import com.kakarote.build.wxapp.entity.BO.BWxuserBindBO;
import com.kakarote.build.wxapp.entity.PO.BWxuserBind;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 * 微信用户绑定表
 服务类
 * </p>
 *
 * @author wudw
 * @since 2022-03-10
 */
public interface IBWxuserBindService extends BaseService<BWxuserBind> {
    BasePage<BWxuserBind> selectByPage(BWxuserBindBO planBO);
}
