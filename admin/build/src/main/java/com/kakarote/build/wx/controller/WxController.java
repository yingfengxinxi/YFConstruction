package com.kakarote.build.wx.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.build.wx.entity.PO.WxUser;
import com.kakarote.build.wx.service.IWxUserService;
import com.kakarote.core.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/wx")
public class WxController {

    @Autowired
    private IWxUserService iWxUserService;

    @RequestMapping("/msg")
    public String  msg(String signature,String echostr,String timestamp,String nonce)
    {

        return echostr;
    }

    @RequestMapping("/getOpenId")
    public Result  getOpenId(String code)
    {
        String str = HttpUtil.get("https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxfc48e055b06116a5&secret=c75a0ed6b8ebc93737bd8ba7f6c2bbe1&code="+code+"&grant_type=authorization_code");
        JSONObject obj = JSON.parseObject(str);
        String openid  =  obj.getString("openid");
        WxUser wxUser = iWxUserService.getWxUserByOpenId(openid);
        Map map = new HashMap();
        map.put("wxUser",wxUser);
        map.put("openId",openid);
        return Result.ok(map);
    }
}
