package com.kakarote.core.message;

/**
 * 注意：
 *  这里对应wm_queues中的code
 */
public class MessageType {

    //系统公告
    public static final String NOTICE_NOTICE = "SYS_XTGG";

    //参考）审批通知
    public static final String MESSAGE_EXAMINE_SUBMIT = "BUS_EXA_SUBMIT";//提交
    public static final String MESSAGE_EXAMINE_PASS = "BUS_EXA_TG";//通过
    public static final String MESSAGE_EXAMINE_REFUSE = "BUS_EXA_REF";//拒绝

    //应急事件
    public static final String MESSAGE_SAFETY_EMERGENCY = "SAFETY_EMERGENCY";
    //安全隐患
    public static final String MESSAGE_SECURITY_RISKS = "SECURITY_RISKS";

    //设备报警--环境
    public static final String ALARM_EQU_ENVRMT = "ALARM_EQU_ENVRMT";
    //设备报警--塔机
    public static final String ALARM_EQU_TOWER = "ALARM_EQU_TOWER";

    //举例
    public static final String WARNING_TEMP = "OTHER_MSG";

    //设备离线超时报警
    public static final String EQUI_OFFLINE_TIMEOUT = "EQUI_OFFLINE_TIMEOUT";

    //监控设备上线
    public static final String MONITOR_EQUI_ONLINE = "MONITOR_EQUI_ONLINE";
    //监控设备离线
    public static final String MONITOR_EQUI_OFFLINE = "MONITOR_EQUI_OFFLINE";
    //监控设备报警
    public static final String  MONITOR_EQUI_WARN = "MONITOR_EQUI_WARN";

    //安全隐患排查
    public static final String MESSAGE_SECURITY_RISKS_DETECTION = "SECURITY_RISKS_DETECTION";

    //质量隐患排查
    public static final String QUAL_RISKS_DETECTION = "QUAL_RISKS_DETECTION";

    //企业提报
    public static final String ENTERPRISE_SUBMISSION = "ENTERPRISE_SUBMISSION";
}
