package com.kakarote.build.wxapp.mapper;

import com.kakarote.build.wxapp.entity.BO.BWxuserBindBO;
import com.kakarote.build.wxapp.entity.PO.BWxuserBind;
import com.kakarote.core.entity.BasePage;
import com.kakarote.core.servlet.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 微信用户绑定表
 Mapper 接口
 * </p>
 *
 * @author wudw
 * @since 2022-03-10
 */
public interface BWxuserBindMapper extends BaseMapper<BWxuserBind> {

    BasePage<BWxuserBind> selectByPage(BasePage<BWxuserBind> parse, @Param("data") BWxuserBindBO bWxuserBindBO);

}
