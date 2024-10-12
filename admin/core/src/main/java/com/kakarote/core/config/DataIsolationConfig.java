package com.kakarote.core.config;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 由于sqlparse继承了抽象类，无法进行spring注入，创建配置类进行维护
 * #数据隔离配置 可对系统进行租户隔离及项目隔离
 * data-isolation:
 *   #租户隔离，必须生效
 *   tenant:
 *     #忽略表，支持通配
 *     ignore-tables: help_topic,wm_*
 *     #忽略mapper文件，支持通配
 *     ignore-mappers:
 *     #忽略mapper中方法名称，支持通配
 *     ignore-mapper-actions:
 *   #项目隔离，选择行生效
 *   project:
 *     auto: true #是否配置项目隔离
 *     #隔离表，支持通配
 *     filter-tables: b_*
 *     #忽略mapper文件，支持通配
 *     ignore-mappers: *GrpMapper
 *     #忽略mapper中方法名称，支持通配
 *     ignore-mapper-actions: *Grp
 */
public class DataIsolationConfig {
    //租户隔离
    public static List<String > TenantIgnoreTables = Lists.newArrayList(
            "help_topic","wm_exchanges","wm_queues","wm_message","wm_message_his","util_hour"
            ,"b_equipment_monitor_status"//20211011设备数据上传忽略Tenant校验
            ,"xxl_job_group"
            ,"xxl_job_info"
            ,"xxl_job_lock"
            ,"xxl_job_log"
            ,"xxl_job_log_report"
            ,"xxl_job_logglue"
            ,"xxl_job_registry"
            ,"xxl_job_user",
            //数据推送
            "b_transmit_busint",
            //其他项目
            "dzgtj_*"
    );
    public static List<String > TenantIgnoreMappers = Lists.newArrayList(
    );
    public static List<String > TenantIgnoreMapperActions = Lists.newArrayList(
            //实时数据
            "EquipmentMonitorDataAtMapper.selectById",
            "EquipmentMonitorDataAtMapper.updateById",
            //聚合数据
            "MonitorDataMonthMapper.insert",
            "MonitorDataDayMapper.insert",
            "MonitorDataYearMapper.insert",
            "MonitorDataDayMapper.updateById",
            "MonitorDataMonthMapper.updateById",
            "MonitorDataYearMapper.updateById",
            "MonitorDataDayMapper.selectById",
            "MonitorDataMonthMapper.selectById",
            "MonitorDataYearMapper.selectById",
            //登录相关
            "AdminDeptMapper.selectById",
            //小程序登录
            "BWxuserBindMapper.selectById",
            "BWxuserBindMapper.deleteById",
            //job
            "BEquipmentAssetsMapper.selectById"

    );
    //项目隔离
    public static boolean ProjectAuto = false;//false/true
    public static List<String > ProjectFilterTables = Lists.newArrayList(
            "b_*"
    );
    public static List<String > ProjectIgnoreMappers = Lists.newArrayList(
            "*GrpMapper"
            ,"BTransmitBusintMapper*","BTransmitPlatformMapper"
            ,"BTransmitLogMapper","BTransmitJobbusintMapper"
            ,"BTransmitPlatintMapper","BTransmitPlatproMapper"
    );
    public static List<String > ProjectIgnoreMapperActions = Lists.newArrayList(
            "*Grp",
            "MonitorDataDayMapper.insert",
            "MonitorDataMonthMapper.insert",
            "MonitorDataYearMapper.insert",
            "MonitorDataDayMapper.updateById",
            "MonitorDataMonthMapper.updateById",
            "MonitorDataYearMapper.updateById",
            "MonitorDataDayMapper.selectById",
            "MonitorDataMonthMapper.selectById",
            "MonitorDataYearMapper.selectById",
            //登录相关
            "AdminMenuMapper.queryMenuList",
            "AdminMenuMapper.selectList",
            "AdminDeptMapper.selectById",
            "AdminLoginLogMapper.queryLast",
            "BWxuserBindMapper.deleteById",
            //查询config配置 /adminConfig/queryConfigByNameAndTenantId
            "AdminConfigMapper.selectList",
            //job
            "BEquipmentAssetsMapper.selectById"

    );
}
