package com.kakarote.core.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.kakarote.core.common.SystemCodeEnum;
import com.kakarote.core.config.sql.ProjectHandler;
import com.kakarote.core.config.sql.ProjectSqlParser;
import com.kakarote.core.config.sql.WTenantHandler;
import com.kakarote.core.config.sql.WTenantSqlParser;
import com.kakarote.core.entity.UserInfo;
import com.kakarote.core.exception.CrmException;
import com.kakarote.core.utils.UserUtil;
import com.kakarote.core.utils.WildcardMatchUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class MybatisPlusConfig {
    private static final Snowflake SNOWFLAKE = IdUtil.getSnowflake(1, 1);


    public static Long getNextId() {
        return SNOWFLAKE.nextId();
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        // 创建SQL解析器集合
        List<ISqlParser> sqlParserList = new ArrayList<>();

        //A:租户 创建租户SQL解析器
        //多租户 2020-11-15 wudw
        WTenantSqlParser tenantSqlParser = new WTenantSqlParser();

        // 设置租户处理器
        tenantSqlParser.setWTenantHandler(new WTenantHandler() {
            @Override
            public Expression getTenantId(boolean where) {
                // 设置当前租户ID
                UserInfo userInfo = UserUtil.getUser();
                Long tenantId = userInfo.getTenantId();
                if (tenantId == null) {
                    throw new CrmException(SystemCodeEnum.NO_LOCK_TENANT);
                }
                return new LongValue(String.valueOf(tenantId));
            }

            @Override
            public String getTenantIdColumn() {
                // 对应数据库租户ID的列名
                return "tenant_id";
            }

            @Override
            public boolean doTableFilter(String tableName, Expression where) {
                //忽略某些条件的表
                for (String nameMatch : DataIsolationConfig.TenantIgnoreTables) {
                    if (WildcardMatchUtil.isMatch(tableName, nameMatch)) {
                        return true;
                    }
                }
                //是否单独处理了tenant
                if (where != null) {
                    if (StringUtils.containsIgnoreCase(where.toString(), getTenantIdColumn())) {
                        return true;
                    }
                }
                return false;

            }
        });

        sqlParserList.add(tenantSqlParser);

        //B:项目 创建项目SQL解析器 2021-04-12 wudw
        ProjectSqlParser projectSqlParser = new ProjectSqlParser();

        // 设置项目处理器
        projectSqlParser.setProjectHandler(new ProjectHandler() {
            @Override
            public Expression getProjectId(boolean where) {
                // 设置当前租户ID
                UserInfo userInfo = UserUtil.getUser();
                Long projectId = userInfo.getProjectId();
                if (projectId == null) {
                    throw new CrmException(SystemCodeEnum.NO_LOCK_PROJECT);
                }
                return new LongValue(String.valueOf(projectId));
            }

            @Override
            public String getProjectIdColumn() {
                // 对应数据库租户ID的列名
                return "project_id";
            }

            @Override
            public boolean doTableFilter(String tableName, Expression where) {
                //过滤某些条件的表
                for (String nameMatch : DataIsolationConfig.ProjectFilterTables) {
                    if (!WildcardMatchUtil.isMatch(tableName, nameMatch)) {
                        return true;
                    }
                }
                //是否单独处理了projectId
                if (where != null) {
                    if (StringUtils.containsIgnoreCase(where.toString(), getProjectIdColumn())) {
                        return true;
                    }
                }
                //todo 2022.03.07 识别用户属性
                UserInfo userInfo = UserUtil.getUser();

                //2022.05.06 校验是否临时过滤
                if(userInfo.getEmpFilterPro()){
                    //System.out.println("EmpFilterPro inner=>"+userInfo.getEmpFilterPro());
                    return false;
                }

                Integer dpid = userInfo.getDeptParentId();
                if (1 == dpid || 0 == dpid) {
                    return true;
                }
                return false;

            }
        });

        sqlParserList.add(projectSqlParser);


        paginationInterceptor.setSqlParserList(sqlParserList);
        //设置过滤器链
        paginationInterceptor.setSqlParserFilter(new ISqlParserFilter() {
            @Override
            public boolean doFilter(MetaObject metaObject) {
                //以下次序不得改变
                //1、登录过程所-p用的mapper，则不需要过滤
                MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
                // 对应Mapper、dao中的方法
                if ("com.kakarote.admin.mapper.AdminUserMapper.selectCount".equals(ms.getId())
                        || "com.kakarote.admin.mapper.AdminUserMapper.findByUsername".equals(ms.getId())
                        || "com.kakarote.admin.mapper.AdminUserRoleMapper.selectObjs".equals(ms.getId())
                        || "com.kakarote.admin.mapper.AdminDictTypeMapper.selectDictTypeAll".equals(ms.getId())
                        || "com.kakarote.admin.mapper.AdminDictMatchMapper.selectAll".equals(ms.getId())
                        || "com.kakarote.admin.mapper.AdminDictDataMapper.selectDictDataByType".equals(ms.getId())
                        || "com.kakarote.core.mapper.AdminLoginLogMapper.insert".equals(ms.getId())//登录日志
                        || "com.kakarote.build.safety.mapper.SafetyInspectionInfoMapper.batchSaveInfo".equals(ms.getId()) // 安全检查计划
                        || "com.kakarote.build.safety.mapper.SafetyInspectionPlanMapper.batchUpdatePlan".equals(ms.getId()) // 安全检查计划
                        || "com.kakarote.build.labour.mapper.LabourInfocollectionMapper.selectAttendanceTodayByProjectId".equals(ms.getId())//集团数据 今日施工在场人数
                        || "com.kakarote.build.safety.mapper.BSafetyDangerSourceMapper.selectSafetyHiddenDanger".equals(ms.getId())//首页+集团数据 安全隐患
                        || "com.kakarote.build.qual.mapper.QualQuestionsMapper.selectQualHiddenDanger".equals(ms.getId())//首页+集团数据 质量隐患
                        || "com.kakarote.build.equipment.mapper.EquipmentMonitorPointMapper.selectAll".equals(ms.getId()) // 监控点定时任务方法
                        || "com.kakarote.build.equipment.mapper.MonitorPointAlertMapper.insert".equals(ms.getId()) // 监控点定时任务方
                        || "com.kakarote.build.equipment.mapper.MonitorPointAlertMapper.updateById".equals(ms.getId()) // 监控点定时任务方
                        || "com.kakarote.build.equipment.mapper.MonitorPointAlertMapper.selectList".equals(ms.getId()) // 监控点定时任务方
                        || "com.kakarote.build.equipment.mapper.EquipmentSetMapper.insert".equals(ms.getId()) // 设备维护计划定时任务
                        || "com.kakarote.build.equipment.mapper.EquipmentPlanMapper.batchUpdatePlan".equals(ms.getId()) // 设备维护计划定时任务
                        || "com.kakarote.build.equipment.mapper.EquipmentSpraySystemMapper.insert".equals(ms.getId()) // 自动喷淋系统定时任务
                        || "com.kakarote.build.app.mapper.AppVersionMapper.selectNewVersion".equals(ms.getId()) // 版本号查询
                        || "com.kakarote.build.wx.mapper.WxAppointmentMapper.add".equals(ms.getId()) // 版本号查询
                        || "com.kakarote.build.wx.mapper.BWxFeedbackMapper.add".equals(ms.getId()) // 新增问题反馈
                        || "com.kakarote.build.project.mapper.ProjectInfoMapper.slelctAll".equals(ms.getId()) // 新增问题反馈
                        || "com.kakarote.build.wx.mapper.BWxFeedbackMapper.selectDataListWx".equals(ms.getId()) // 查询问题反馈
                        || "com.kakarote.build.wx.mapper.BWxFeedbackMapper.updateByIdWx".equals(ms.getId()) // 更新问题反馈
                        || "com.kakarote.build.wx.mapper.BWxFeedbackMapper.getByIdWx".equals(ms.getId()) // 单条查询问题反馈
                        || "com.kakarote.admin.mapper.AdminFileMapper.add".equals(ms.getId()) // 微信附件新增接口
                        || "com.kakarote.admin.mapper.AdminFileMapper.selectListWx".equals(ms.getId()) // 微信附件列表查询接口
                        || "com.kakarote.admin.mapper.AdminFileMapper.getByIdWx".equals(ms.getId()) // 微信附件下载查询接口
                        || "com.kakarote.build.wx.mapper.WxAppointmentMapper.add".equals(ms.getId()) // 新增预约
                        || "com.kakarote.build.qual.mapper.QualHouseTypeMangeMapper.wxGetByIdDiy".equals(ms.getId()) // 新增预约
                        || "com.kakarote.build.qual.mapper.QualHouseAcceptFillMainMapper.wxSave".equals(ms.getId()) // 新增预约
                        || "com.kakarote.build.qual.mapper.QualHouseAcceptFillSonMapper.wxSave".equals(ms.getId()) // 新增预约
                        || "com.kakarote.build.qual.mapper.QualHouseAcceptFillMainMapper.wxselectByPage".equals(ms.getId()) // 新增预约
                        || "com.kakarote.build.qual.mapper.QualHouseAcceptFillMainMapper.wxselectById".equals(ms.getId()) // 新增预约
                        || "com.kakarote.build.qual.mapper.QualHouseAcceptFillSonMapper.wxSelectByPid".equals(ms.getId()) // 新增预约
                        || "com.kakarote.build.wx.mapper.WxUserMapper.getWxUserByOpenId".equals(ms.getId()) // 新增预约
                        || "com.kakarote.build.wx.mapper.WxUserMapper.add".equals(ms.getId()) // 新增预约
                        || "com.kakarote.build.wx.mapper.WxUserMapper.updateWxUser".equals(ms.getId()) // 新增预约
                        || "com.kakarote.build.wx.mapper.WxAppointmentMapper.wxSelectByPage".equals(ms.getId()) // 新增预约
                        || "com.kakarote.build.qual.mapper.QualCheckPersonnelMapper.selectWxList".equals(ms.getId())
                        || "com.kakarote.core.mapper.SequenceMapper.insertTenantId".equals(ms.getId())
                        || "com.kakarote.build.env.mapper.EnvPostLogMapper.saveLog".equals(ms.getId()) // 环境监测传输日志保存接口
                        || "com.kakarote.build.env.mapper.EnvPostDataMapper.batchSaveData".equals(ms.getId()) // 环境监测传输数据保存接口
                        || "com.kakarote.build.equipment.mapper.EquipmentAssetsMapper.selectVideoMonitor".equals(ms.getId())//查询视频流地址
                        || "com.kakarote.build.equipment.mapper.EquipmentMonitorPointMapper.listAllProject".equals(ms.getId())//监控项设置查询
                        || "com.qdoner.datarecept.mapper.getOfflineTower".equals(ms.getId())//塔吊离线数据查询
//                        || "com.kakarote.build.equipment.mapper.EquipmentOfflineMapper.selectOfflineTower".equals(ms.getId())//升降机离线数据查询
//                        || "com.kakarote.build.equipment.mapper.EquipmentOfflineMapper.selectOfflineElevator".equals(ms.getId())//塔吊离线数据查询
                        || "com.qdoner.datarecept.mapper.EquipmentOfflineMapper.selectOfflineTower".equals(ms.getId())//升降机离线数据查询
                        || "com.qdoner.datarecept.mapper.EquipmentOfflineMapper.selectOfflineElevator".equals(ms.getId())//塔吊离线数据查询
                        || "com.qdoner.datarecept.mapper.EquipmentOfflineMapper.getOfflineTower".equals(ms.getId())//塔吊离线数据查询
                        || "com.qdoner.datarecept.mapper.EquipmentOfflineMapper.getOfflineElevator".equals(ms.getId())//升降机离线数据查询



                        ) {
                    return true;
                }
                return false;
            }
        });

        //crm系统分页配置
        paginationInterceptor.setLimit(-1);
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));

        return paginationInterceptor;
    }

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return i -> i.setObjectWrapperFactory(new MybatisMapWrapperFactory());
    }

    @Bean
    public IdentifierGenerator idGenerator() {
        return new CustomIdGenerator();
    }

    public class CustomIdGenerator implements IdentifierGenerator {
        @Override
        public Long nextId(Object entity) {
            return getNextId();
        }
    }
}
