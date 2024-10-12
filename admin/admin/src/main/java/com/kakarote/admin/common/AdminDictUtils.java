package com.kakarote.admin.common;

import com.alibaba.fastjson.JSONObject;
import com.kakarote.admin.entity.PO.AdminDictMatch;
import com.kakarote.admin.mapper.AdminDictDataMapper;
import com.kakarote.admin.mapper.AdminDictMatchMapper;
import com.kakarote.core.common.Const;
import com.kakarote.core.redis.Redis;
import com.kakarote.admin.entity.PO.AdminDictData;

import java.util.*;

import com.kakarote.core.servlet.ApplicationContextHolder;
import com.kakarote.core.utils.BaseUtil;
import com.kakarote.core.utils.SpringUtils;
import com.kakarote.core.utils.UserUtil;
import org.apache.ibatis.javassist.expr.NewArray;

import java.util.List;

/**
 * @Description 字典项工具类
 * @Author PangZhen
 * @Date 2020-11-16 16:48
 */
public class AdminDictUtils {

    public static final String SYS_DICT_CHAR = "-";

    /**
     * 设置字典缓存
     *
     * @param key 参数键
     * @param dictDatas 字典数据列表
     */
    public static void setDictCache(Long tenantId, String key, List<AdminDictData> dictDatas)
    {
        Redis redis = BaseUtil.getRedis();

//        System.out.println("setDictCache"+ getCacheKey(tenantId, key));
        redis.set(getCacheKey(tenantId, key), dictDatas);
//        System.out.println("setDictCacheOK");
    }

    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static List<AdminDictData> getDictCache(String key)
    {
        Redis redis = BaseUtil.getRedis();
//        System.out.println("getDictCache"+ getCacheKey(UserUtil.getUser().getTenantId(), key));
        Object cacheObj = redis.get(getCacheKey(UserUtil.getUser().getTenantId(), key));
        if (cacheObj != null)
        {
//            System.out.println("cacheObj != null：=="+key);
            List<AdminDictData> dictDatas = (List<AdminDictData>)cacheObj;
            return dictDatas;
        }else{
            List<AdminDictData> dictDatas = ApplicationContextHolder.getBean(AdminDictDataMapper.class)
                    .selectDictDataByType(key, UserUtil.getUser().getTenantId());
            setDictCache(UserUtil.getUser().getTenantId(), key, dictDatas);
//            System.out.println("dictDatas=="+dictDatas.size());
        }
        return null;
    }


    /**
     * 获取字典缓存
     *
     * @param key 参数键
     * @return dictDatas 字典数据列表
     */
    public static String getDictLabel(String key,String value)
    {
        List<AdminDictData> dictDatas = getDictCache(key);
        for(AdminDictData data : dictDatas){
            if(data.getDictValue().equals(value)){
                return data.getDictLabel();
            }
        }
        return null;
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache()
    {
        Redis redis = BaseUtil.getRedis();
        //此处使用*无法匹配相应key，未能实际清除缓存-->改为获取所有后遍历重组
        Set<String> keys = redis.keys(AdminConst.SYS_DICT_KEY + UserUtil.getUser().getTenantId() + SYS_DICT_CHAR + "*");

//        Set<String> keys2 = redis.keys("*");
//        Set<String> keyDel = new HashSet<>();
//        System.out.println("key-size=="+keys2.size());
//        for(String key : keys2){
//            System.out.println("key=="+key);
//            if(key.contains(AdminConst.SYS_DICT_KEY + UserUtil.getUser().getTenantId())){
//                keyDel.add(key);
//            }
//        }
        redis.delKeys(keys);

    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    public static String getCacheKey(Long tenantId, String configKey)
    {
//        System.out.println("getCacheKey"+ (AdminConst.SYS_DICT_KEY + tenantId + SYS_DICT_CHAR + configKey));
        return AdminConst.SYS_DICT_KEY + tenantId + SYS_DICT_CHAR + configKey;
    }

    /**
    * @Description:清除关联字典缓存
    * @author shz
    * @date 2022-4-22 11:40
    * @param 
    * @return 
    */
    public static void clearRelationDictCache(Long tenantId,String matchDict,String value) {
        Redis redis = BaseUtil.getRedis();
        //此处使用*无法匹配相应key，未能实际清除缓存-->改为获取所有后遍历重组
        Set<String> keys = redis.keys(Const.RELATION_DICT_KEY + tenantId + SYS_DICT_CHAR +matchDict+SYS_DICT_CHAR+value);
        redis.delKeys(keys);

    }
    /**
    * @Description:关联字典缓存
    * @author shz
    * @date 2022-4-22 14:39
    * @param 
    * @return 
    */
    public static void setDictMatchCache(Integer tenantId,String matchDict,String value ,String dictDatas) {
        Redis redis = BaseUtil.getRedis();;
        redis.set(Const.RELATION_DICT_KEY + tenantId + SYS_DICT_CHAR +matchDict+SYS_DICT_CHAR+value, dictDatas);
    }
    /**
    * @Description:根据对应系统字典类型获取关联字典表
    * @author shz
    * @date 2022-4-22 14:40
    * @param tenantId 租户id
     * @param matchDict 对应系统代码
     * @param value 本系统相关字典JSON数据
    * @return 对应系统值
    */
    public static String getDictMatcValue (Integer tenantId,String matchDict,String value){
        Redis redis = BaseUtil.getRedis();
        Object cacheObj = redis.get(Const.RELATION_DICT_KEY + tenantId + SYS_DICT_CHAR +matchDict+ SYS_DICT_CHAR +value);
        if (cacheObj != null)
        {
            String dictDatas = (String)cacheObj;
            return dictDatas;
        }else{
            Map dictDatas = ApplicationContextHolder.getBean(AdminDictMatchMapper.class)
                    .selectDictMatchByType(matchDict,value,tenantId);
            String str = "";
            if(dictDatas != null ){
                str = new JSONObject(dictDatas).toJSONString();
                setDictMatchCache(tenantId,matchDict,value,str);
            }
            return str;
        }
    }
}