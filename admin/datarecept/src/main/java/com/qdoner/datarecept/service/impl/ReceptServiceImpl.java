package com.qdoner.datarecept.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.kakarote.core.common.Result;
import com.kakarote.core.feign.build.BuildService;
import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.ReflectionUtils;
import com.kakarote.core.utils.WildcardMatchUtil;
import com.qdoner.datarecept.entity.PO.BEquipmentAssets;
import com.qdoner.datarecept.entity.PO.EquipmentReceptPlatform;
import com.qdoner.datarecept.service.EqStatusCommonService;
import com.qdoner.datarecept.service.IBEquipmentAssetsService;
import com.qdoner.datarecept.service.IEquipmentReceptPlatformService;
import com.qdoner.datarecept.service.IReceptService;
import com.qdoner.datarecept.utils.CRCUtils;
import com.qdoner.datarecept.utils.HexEcodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ReceptServiceImpl implements IReceptService {
    @Autowired
    private IBEquipmentAssetsService assetsService;
    @Autowired
    private IEquipmentReceptPlatformService receptPlatformService;
    @Autowired
    private BuildService buildService;

    @Autowired
    private EqStatusCommonService statusCommonService;

    /**
     * 平台对接
     *
     * @param secret
     * @param object
     * @return
     */
    @Override
    public Result receptHttp(String secret, String ip, JSONObject object) {
        //1、校验密钥
        if (StringUtils.isEmpty(secret)) {
            return Result.error(-1, "未获取到密钥");
        }
        //2、校验secret
        List<EquipmentReceptPlatform> receptPlatformList = receptPlatformService.lambdaQuery().eq(EquipmentReceptPlatform::getSecret, secret).eq(EquipmentReceptPlatform::getStatus, "1")
                .isNotNull(EquipmentReceptPlatform::getProjectId).isNotNull(EquipmentReceptPlatform::getTenantId).list();
        if (receptPlatformList.size() < 1) {
            return Result.error(-1, "无效的密钥");
        }
        EquipmentReceptPlatform receptPlatform = receptPlatformList.get(0);
        //IP白名单
        boolean noAccess = true;
        String ipStr = receptPlatform.getIps();
        if (!StringUtils.isEmpty(ipStr)) {
            String[] ips = ipStr.split(",");
            for (String ipE : ips) {
                if (WildcardMatchUtil.isMatch(ip, ipE)) {
                    noAccess = false;
                    break;
                }
            }
        }
        if (noAccess) {
            log.error("IP解析器异常：IP不在白名单内{}", ip);
            return Result.error(-1, "IP不在白名单内=>" + ip);
        }
        //获取设备码

        String resCode = receptPlatform.getResCode();
        if (StringUtils.isEmpty(resCode)) {
            return Result.error(-1, "Code 解析器错误");
        }
        String eqCode;
        try {
            Class resCodeClass = Class.forName(resCode);
            Method method = resCodeClass.getDeclaredMethod("codeResolver", JSONObject.class);
            Object result = method.invoke(resCodeClass.newInstance(), object);
            eqCode = String.valueOf(result);
        } catch (Exception e) {
            log.error("Code解析器异常：", e);
            return Result.error(-1, "Code 解析器异常");
        }
        if (StringUtils.isEmpty(eqCode)) {
            return Result.error(-1, "Code 解析错误 NULL");
        }
        //解析结果
        List<BEquipmentAssets> assetsList = assetsService.lambdaQuery().isNotNull(BEquipmentAssets::getProjectId).isNotNull(BEquipmentAssets::getTenantId)
                .eq(BEquipmentAssets::getPlatform, receptPlatform.getId())
                .eq(BEquipmentAssets::getPlatformEqCode, eqCode)
                .list();
        if (assetsList.size() < 1) {
            return Result.error(-1, "未配置设备");
        }
        BEquipmentAssets assets = assetsList.get(0);
        //2023-02-20 及时刷新设备状态
        statusCommonService.handleOfflineSatus(assets.getAssetId(),"1");
        String resolver = assets.getDataResolver();
        if (StringUtils.isEmpty(resolver)) {
            //尝试加载默认解析器
            resolver = receptPlatform.getDefEqResolver();
            if (StringUtils.isEmpty(resolver)) {
                return Result.error(-1, "未配置解析器");
            }
        }
        JSONObject jsonObject;
        try {
            Class resClass = Class.forName(resolver);
            Method method = resClass.getMethod("dataResolver", JSONObject.class, JSONObject.class);
            JSONObject jot = new JSONObject();
            BeanUtil.copyProperties(assets, jot);
            Object result = method.invoke(resClass.newInstance(), object, jot);
            jsonObject = JSONObject.parseObject(String.valueOf(result));
        } catch (Exception e) {
            log.error("解析器异常：" + resolver, e);
            return Result.error(-1, "解析器异常");
        }
        //3、传输数据
        buildService.receptEqData(jsonObject);
        return Result.ok("传输完成……");
    }

    /**
     * 设备报文接收
     *
     * @param data 报文内容
     * @param ip   来源IP
     * @return
     */
    @Override
    public String receptMsg(String data, String ip, int port) {

        List<EquipmentReceptPlatform> platformList = receptPlatformService.lambdaQuery().eq(EquipmentReceptPlatform::getStatus, "1")
                .eq(EquipmentReceptPlatform::getType, "1")
                .eq(EquipmentReceptPlatform::getPort, port)
                .isNotNull(EquipmentReceptPlatform::getTenantId).list();
        if (platformList.size() < 1) {
            log.error("端口解析器异常：端口配置不存在或未生效");
            return "Error:port";
        }
        EquipmentReceptPlatform config = platformList.get(0);
        //校验白名单
        boolean access = false;
        String ips = config.getIps();
        if (!StringUtils.isEmpty(ips)) {
            String[] ipsA = ips.split(",");
            for (String ipE : ipsA) {
                if (WildcardMatchUtil.isMatch(ip, ipE)) {
                    access = true;
                    break;
                }
            }
        } else {
            access = true;
        }
        if (!access) {
            log.error("IP解析器异常：IP不在白名单内{}", ip);
            return "Error:ip";
        }
        //校验设备编码
        String resCode = config.getResCode();
        if (StringUtils.isEmpty(resCode)) {
            log.error("Code解析器异常：端口配置未生效");
            return "Code 解析器错误";
        }
        String eqCode;
        try {
            Class resCodeClass = Class.forName(resCode);
            Method method = resCodeClass.getDeclaredMethod("codeResolver", String.class);
            Object result = method.invoke(resCodeClass.newInstance(), data);
            eqCode = String.valueOf(result);
        } catch (Exception e) {
            log.error("Code解析器异常：", e);
            return "Code 解析器错误";
        }
        if (StringUtils.isEmpty(eqCode)) {
            return "Code 解析错误 NULL";
        }
        //解析结果
        List<BEquipmentAssets> assetsList = assetsService.lambdaQuery().isNotNull(BEquipmentAssets::getProjectId).isNotNull(BEquipmentAssets::getTenantId)
                .eq(BEquipmentAssets::getPlatform, config.getId())
                .eq(BEquipmentAssets::getPlatformEqCode, eqCode)
                .list();
        if (assetsList.size() < 1) {
            log.error("设备解析器异常：未配置设备=>" + eqCode);
            return "设备解析异常：未配置设备";
        }
        BEquipmentAssets assets = assetsList.get(0);
        //2023-02-20 及时刷新设备状态
        statusCommonService.handleOfflineSatus(assets.getAssetId(),"1");
        String resolver = assets.getDataResolver();
        if (StringUtils.isEmpty(resolver)) {
            //尝试加载默认解析器
            resolver = config.getDefEqResolver();
            if (StringUtils.isEmpty(resolver)) {
                return "设备解析器异常：未配置解析器";
            }
        }
        //组装数据
        try {
            JSONObject jot = new JSONObject();
            BeanUtil.copyProperties(assets, jot);
            Class intBeClass = Class.forName(resolver);
            Object intBeBean = ApplicationContextHolder.getBean(intBeClass);
            ReflectionUtils.invokeMethod(intBeBean, "dataResolver",
                    new Class[]{String.class,JSONObject.class}, new Object[]{data,jot});
            /*Class resClass = Class.forName(resolver);
            Method method = resClass.getMethod("dataResolver", String.class, JSONObject.class);
            method.invoke(resClass.newInstance(), data, jot);*/
        } catch (Exception e) {
            log.error("数据解析器异常：" + resolver, e);
            return "数据解析器异常";
        }
        //响应报文
        String response;
        try {
            Class resClass = Class.forName(resolver);
            Method method = resClass.getMethod("responseMsg", String.class);
            Object result = method.invoke(resClass.newInstance(), data);
            response = String.valueOf(result);
        } catch (Exception e) {
            log.error("报文响应异常：" + resolver, e);
            return "报文响应异常";
        }
        return response;
    }

    @Override
    public String replyMsg(String str) {
        String hf = null;
        if("01".equals(str.substring(4, 6))){
            SimpleDateFormat ft = new SimpleDateFormat("yyMMddHHmmss");
            Date date = new Date();
            String time = ft.format(date);
            String huifu = str.substring(0, 4) + "02" + str.substring(6, 14) + "01" + time + "0F";
            hf = huifu + CRCUtils.makeChecksum(huifu) + "0FCC33C33C";
        }else if("60".equals(str.substring(4, 6))){
            SimpleDateFormat ft = new SimpleDateFormat("yyMMddHHmmss");
            Date date = new Date();
            String time = ft.format(date);
            String huifu = str.substring(0, 4) + "61" + str.substring(6, 14) + "01" + time + "0F";
            hf = huifu + CRCUtils.makeChecksum(huifu) + "0FCC33C33C";
        }
        log.info(" 发送数据1: <<<<<<" + hf);
        return hf;
    }

}
