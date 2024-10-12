package com.kakarote.gateway.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.kakarote.core.common.Const;
import com.kakarote.core.common.Result;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.redis.Redis;
import com.kakarote.gateway.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * @author zhangzhiwei
 * gateway全局拦截
 */
@Component
@Slf4j
public class GatewayFilter implements GlobalFilter, Ordered {

    protected static ThreadLocal<String> ip = new ThreadLocal<>();

    //需要cookies
    private static final String[] AUTH_SPECIAL_URLS = {"/adminFile/down/*", "/crmPrint/down", "/crmPrint/preview.pdf"
            , "/socket*"//socket只校验cookie即可
            /*2021-09-03 V1.5 追加架构工具相关*/
            , "/jmreport/*"//报表工具只校验cookie
            , "/dzdata/dzgtjSignLog/pre/*"//证照预览

    };
    //不需要cookies
    private static final List<String> NOT_AUTH_URLS = Lists.newArrayList("/crmMarketing/queryMarketingId",
            "/crmMarketing/queryAddField", "/crmMarketing/saveMarketingInfo", "/adminUser/querySystemStatus",
            "/adminUser/initUser", "/build/wxAppointment/save", "/build/bWxFeedback/add", "/build/projectInfo/WxslelctAll",
            "/build/bWxFeedback/listWx", "/build/bWxFeedback/selectByIdWx", "/build/bWxFeedback/editWx",
            "/adminFile/uploadWx", "/adminFile/queryFileListWx", "/adminFile/downWx", "/build/qualBuildMange/wXqueryByQualBuild",
            "/build/qualHouseTypeMange/wxGetByIdDiy", "/build/qualHouseAcceptFillMain/wxSaveOrUpdate", "/build/qualHouseAcceptFillMain/wxlist",
            "/build/qualHouseAcceptFillMain/wxselectById", "/build/qualHouseAcceptFillSon/wxSelectByPid", "/build/wx/msg", "/build/wx/getOpenId", "/build/wxUser/save",
            "/build/wxAppointment/wxSelectByPage", "/build/wxAppointment/wxGetById", "/build/qualCheckPersonnel/wxlist",
            "/build/SiteDataUpload/*"//工地数据上传接口  token放行
            , "/datarecept/*"//设备数据上传
            ,"/build/api*/**"//品茗上传接口  token放行
            //,"/brecept/**"//2022-10-19 监管数据接收
            , "/dzdata/in/**"//业务接口
    );

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private Redis redis;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethodValue();
        String url = request.getPath().value();
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        request.getCookies().keySet().forEach(str -> {
            if (Objects.equals("ServerIp", str)) {
                HttpCookie first = cookies.getFirst(str);
                ip.set(first != null ? first.getValue() : "");
            }
        });
        log.info("url:{},method:{},headers:{}", url, method, request.getHeaders());
        for (String u : NOT_AUTH_URLS) {
            if (WildcardMatchUtil.isMatch(url, u)) {
                return chain.filter(exchange);
            }
        }
        if (url.endsWith(SwaggerProvider.API_URI) || NOT_AUTH_URLS.contains(url)) {
            return chain.filter(exchange);

        }
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        if (route != null) {
            Map<String, Object> metadata = route.getMetadata();
            if (Objects.equals(GatewayConst.ROUTER_INTERCEPT_OK, metadata.get(GatewayConst.ROUTER_INTERCEPT))) {
                return chain.filter(exchange);
            }
        }
        String token = request.getHeaders().getFirst(Const.TOKEN_NAME);
        if (StrUtil.isEmpty(token)) {
            boolean isSpecialUrl = false;
            for (String specialUrl : AUTH_SPECIAL_URLS) {
                if (specialUrl.contains("*")) {
                    if (url.startsWith(specialUrl.replace("*", ""))) {
                        isSpecialUrl = true;
                        break;
                    }
                } else {
                    if (url.equals(specialUrl)) {
                        isSpecialUrl = true;
                        break;
                    }
                }
            }
            if (isSpecialUrl) {
                HttpCookie first = request.getCookies().getFirst(Const.TOKEN_NAME);
                token = first != null ? first.getValue() : "";
            }
        }
        if (StrUtil.isEmpty(token)) {
            throw new CrmException(SystemCodeEnum.SYSTEM_NOT_LOGIN);
        }
        UserInfo userInfo = redis.get(token);
        List<String> noAuthMenuUrls = Optional.ofNullable(userInfo).orElse(new UserInfo()).getNoAuthMenuUrls();
        boolean permission = isHasPermission(noAuthMenuUrls, url);
        if (!permission) {
            //return unauthorized(exchange);
        }
        //socket 追加header key  2022-12-13 改为免侵入
        /*if (url.startsWith("/socket")) {
            String target = request.getQueryParams().getFirst("target");
            if (StringUtils.isEmpty(target)) {
                throw new CrmException(SystemCodeEnum.NO_SOCKET_TARGET);
            }
            if (userInfo != null) {
                request.mutate().header("WS-Key", target +
                        "-" + String.valueOf(userInfo.getTenantId()) +//租户
                        "-" + String.valueOf(userInfo.getUserId())
                        + "-" + UUID.randomUUID()).build();
            }
        }*/
        return chain.filter(exchange);
    }

    /**
     * 判断有无权限访问
     *
     * @param noAuthMenuUrls 无权限路由
     * @param url
     * @return boolean
     * @date 2020/8/21 13:35
     **/
    private boolean isHasPermission(List<String> noAuthMenuUrls, String url) {
        //用户信息丢失 | 错误
        if (noAuthMenuUrls == null) {
            return false;
        }
        //管理员
        if (noAuthMenuUrls.size() == 0) {
            return true;
        }
        //没有任何权限
        if (noAuthMenuUrls.size() == 1 && "/*/**".equals(noAuthMenuUrls.get(0))) {
            return false;
        }
        boolean permission = true;
        for (String noAuthMenuUrl : noAuthMenuUrls) {
            if (noAuthMenuUrl.contains("*")) {
                if (noAuthMenuUrl.contains(",")) {
                    boolean isNoAuth = false;
                    for (String noAuthUrl : noAuthMenuUrl.split(",")) {
                        if (url.startsWith(noAuthUrl.replace("*", ""))) {
                            isNoAuth = true;
                            break;
                        }
                    }
                    if (isNoAuth) {
                        permission = false;
                        break;
                    }
                } else {
                    if (url.startsWith(noAuthMenuUrl.replace("*", ""))) {
                        permission = false;
                        break;
                    }
                }
            } else {
                if (noAuthMenuUrl.contains(",")) {
                    if (Arrays.asList(noAuthMenuUrl.split(",")).contains(url)) {
                        permission = false;
                        break;
                    }
                } else {
                    if (noAuthMenuUrl.equals(url)) {
                        permission = false;
                        break;
                    }
                }
            }
        }
        return permission;
    }

    /**
     * 网关拒绝，返回401
     *
     * @param
     */
    private Mono<Void> unauthorized(ServerWebExchange serverWebExchange) {
        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        Result result = Result.error(SystemCodeEnum.SYSTEM_NO_AUTH);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(result.toJSONString().getBytes(CharsetUtil.systemCharset()));
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
