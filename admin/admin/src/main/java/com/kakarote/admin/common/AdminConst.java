package com.kakarote.admin.common;

import com.kakarote.core.common.Const;

/**
 * @author zhangzhiwei
 * 系统管理模块的常量
 */
public class AdminConst extends Const {
    /**
     * 默认的权限缓存KEY
     */
    public static final String DEFAULT_AUTH_CACHE_NAME = "CRM:AUTH";

    /**
     * 默认的密码强度正则
     */
    public static final String DEFAULT_PASSWORD_INTENSITY = "^(?=.*[a-zA-Z])(?=.*\\d).{6,20}$";

    /** 校验返回结果码 */
    public final static String UNIQUE = "0";

    public final static String NOT_UNIQUE = "1";

    public static final String SYS_DICT_KEY = "sd-";

    public static final String RELATION_DICT_KEY="rd-";
}
