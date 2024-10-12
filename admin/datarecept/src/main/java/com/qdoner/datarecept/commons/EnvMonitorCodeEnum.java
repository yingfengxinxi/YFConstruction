package com.qdoner.datarecept.commons;

import com.kakarote.core.common.ResultCode;

/**
 * @author zhangzhiwei
 * 权限后台响应错误代码枚举类
 */

public enum EnvMonitorCodeEnum implements ResultCode {

    ENV_MONITOR_SUCCESS(0, "成功"),
    ENV_MONITOR_SIGN_VERIFY(10, "签名校验失败"),
    ENV_MONITOR_PUSH_TRANSFINITE(20, "推送频次超过限制,15分钟内只允许上传一次数据"),
    ENV_MONITOR_PROJECT_NOT_EXIST(30, "appid不存在,请项目施工单位先在住建委云平台申请后再进行数据推送"),
    ENV_MONITOR_TIMESTAMP_TIMEOUT(40, "timestamp校验失败,时间戳与服务器时间误差不能超过1分钟,格式为yyyyMMddhhmmss"),
    ENV_MONITOR_FAIL_SAVE(50, "异步查询结果：保存失败"),
    ENV_MONITOR_PARAM_FORMAT_ERROR(97, "参数格式错误,请根据文档核对"),
    ENV_MONITOR_MISSING_PARAM(98, "缺少必要的参数,请根据文档核对!"),
    ENV_MONITOR_SYSTEM_ERROR(99, "系统异常");


    EnvMonitorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
