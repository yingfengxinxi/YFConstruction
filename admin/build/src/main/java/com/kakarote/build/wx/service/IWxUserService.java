package com.kakarote.build.wx.service;

import com.kakarote.build.wx.entity.PO.WxUser;
import com.kakarote.core.servlet.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author shz
 * @since 2021-07-28
 */
public interface IWxUserService extends BaseService<WxUser> {

    WxUser getWxUserByOpenId(String id);

    Integer add(WxUser wxUser);

}
