package com.kakarote.build.wxapp.service;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kakarote.build.wxapp.common.WxApiService;
import com.kakarote.build.wxapp.entity.PO.BWxuserBind;
import com.kakarote.core.common.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WxAppService {

    @Autowired
    private WxApiService wxApiService;

    @Autowired
    private IBWxuserBindService wxuserBindService;

    /**
     * 绑定
     *
     * @param
     * @return
     */
    public Result bind(JSONObject bindData) {
        //获取openID
        JSONObject re = wxApiService.getOpenIDByJsCode(bindData.getString("jsCode"));
        Object errObject = re.get("errcode");
        if (null != errObject) {
            Integer errcode = Integer.valueOf(errObject.toString());
            if(0 != errcode){
                return Result.error(re.getIntValue("errcode"), re.getString("errmsg"));
            }
        }
        String openId = re.getString("openid");
        //String openId = "test123";
        //
        BWxuserBind wxuserBind = new BWxuserBind();
        wxuserBind.setUsername(bindData.getString("username"));
        wxuserBind.setPassword(bindData.getString("password"));
        wxuserBind.setName(bindData.getString("name"));
        wxuserBind.setPhone(bindData.getString("phone"));
        wxuserBind.setTenantId(bindData.getInteger("tenantId"));
        //
        wxuserBind.setOpenid(openId);
        //
        wxuserBind.setCreateTime(new Date());
        //防止重复绑定错误
        wxuserBindService.removeById(openId);
        wxuserBindService.save(wxuserBind);
        return Result.ok(openId);
    }

    /**
     * 登录
     *
     * @return
     */
    public Result login(String jsCode) {
        JSONObject re = wxApiService.getOpenIDByJsCode(jsCode);
        Object errObject = re.get("errcode");
        if (null != errObject) {
            Integer errcode = Integer.valueOf(errObject.toString());
            if(0 != errcode){
                return Result.error(re.getIntValue("errcode"), re.getString("errmsg"));
            }
        }
        String openId = re.getString("openid");
        //String openId = "test123";
        //查询绑定信息
        BWxuserBind wxuserBind = wxuserBindService.getById(openId);
        if (wxuserBind == null) {
            return Result.error(10, "未绑定账号");
        }
        //校验状态
        if (!"0".equals(wxuserBind.getStatus())) {
            return Result.error(20, "此微信号被限制登录");
        }
        JSONObject reData = new JSONObject();
        BeanUtil.copyProperties(wxuserBind,reData);
        return Result.ok(reData);
    }
}
