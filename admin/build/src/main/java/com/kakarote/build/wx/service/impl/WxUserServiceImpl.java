package com.kakarote.build.wx.service.impl;

import com.kakarote.build.wx.entity.PO.WxUser;
import com.kakarote.build.wx.mapper.WxUserMapper;
import com.kakarote.build.wx.service.IWxUserService;
import com.kakarote.core.servlet.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author shz
 * @since 2021-07-28
 */
@Service
public class WxUserServiceImpl extends BaseServiceImpl<WxUserMapper, WxUser> implements IWxUserService {

    @Override
    public WxUser getWxUserByOpenId(String id) {
        return getBaseMapper().getWxUserByOpenId(id);
    }

    @Override
    public Integer add(WxUser wxUser) {
        WxUser wxUser1 = getBaseMapper().getWxUserByOpenId(wxUser.getOpenId());
        Integer id = null;
        if(wxUser1 == null){
            Long i =getBaseMapper().add(wxUser);
            id = i.intValue();
        }else {
            getBaseMapper().updateWxUser(wxUser);
            id = wxUser.getId();
        }

        return id;
    }
}
