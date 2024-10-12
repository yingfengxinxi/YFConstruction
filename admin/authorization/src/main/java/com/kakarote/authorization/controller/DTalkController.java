package com.kakarote.authorization.controller;

import com.kakarote.authorization.service.DTalkLoginService;
import com.kakarote.core.common.Result;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wudw
 * 添加权限的controller
 */
@RestController
@Api(tags = "钉钉登录相关接口")
public class DTalkController {

    @Autowired
    private DTalkLoginService dTalkLoginService;

    /**
     * 换取CorpId
     * @return
     */
    @GetMapping(value = "/dLogin/g")
    public Result doLogin(HttpServletRequest request){
        String cid = request.getHeader("c-id");
        if(StringUtils.isEmpty(cid)){
            return Result.error(-1,"参数不得为空");
        }
        return dTalkLoginService.getCorpId(cid);
    }
    @GetMapping(value = "/dLogin/{code}")
    public Result doLogin(@PathVariable String code, HttpServletRequest request){
        if(StringUtils.isEmpty(code)){
            return Result.error(-1,"code不得为空");
        }
        return dTalkLoginService.login(code,request.getHeader("c-id"));
    }
}
