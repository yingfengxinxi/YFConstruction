package com.kakarote.build.wx.mapper;

import com.kakarote.build.wx.entity.PO.WxUser;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shz
 * @since 2021-07-28
 */
public interface WxUserMapper extends BaseMapper<WxUser> {

    WxUser getWxUserByOpenId(String id);

    Long add(@Param("data")WxUser wxUser);

    int updateWxUser(@Param("data")WxUser wxUser);
}
