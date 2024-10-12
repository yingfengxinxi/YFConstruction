package com.kakarote.core.common;

/**
 * @author zhangzhiwei
 * 基础业务配置枚举
 * 作用范围  自定义字段、流程审批
 */

public enum BusinessTypeEnum {

    NULL(-1, null, null, "未匹配到的类型", ""),
//    DEMO(10000, "qdoner_wf_demo", "id", "content", "测试DEMO"),
    //专项安全方案
    SAFETY_PLAN(10010, "b_safety_plan", "id", "content", "专项安全方案"),
    //安全巡检-整改通知单
    SAFETT_INSPECTION_INFO(10011, "b_safett_inspection_info", "id", "plan_name", "安全巡检-整改通知单"),
    //质量巡检-整改通知单
    QUAL_CHECK(10012, "b_qual_check", "check_id", "qual_check_type", "质量巡检-整改通知单"),
    //创优评优申报
    APPRASING_DECLARE(10020, "b_qual_appraisingdeclare_main_grp", "id", "content", "创优评优申报"),
    //月计划管理
    PLAN_MONTH(10030, "b_progress_plan_month_details", "id", "name", "月计划管理"),

    SELF_EVALUATE(10040, "b_progress_self_evaluate", "id", "mark", "月度自评"),

    SELF_EVALUATE_YEAR(10050, "b_progress_self_evaluate_year", "id", "mark", "年度自评"),

    ;


    /**
     * 参数解释
     *
     * @param type      业务类型code
     * @param tableName 业务主表名称（即参与审批的表）
     * @param idName    业务主表主键名称
     * @param colName   主表中表述字段的字段名称
     * @param name      业务类型描述
     */
    BusinessTypeEnum(Integer type, String tableName, String idName, String colName, String name) {
        this.type = type;
        this.tableName = tableName;
        this.idName = idName;
        this.colName = colName;
        this.name = name;
    }

    private Integer type;//类型

    private String tableName;//表名

    private String idName;//主键

    private String colName;//备注说明

    private String name;//业务名称

    public Integer getType() {
        return type;
    }

    public String getTableName() {
        return tableName;
    }

    public String getIdName() {
        return idName;
    }

    public String getColName() {
        return colName;
    }

    public String getName() {
        return name;
    }

    public static BusinessTypeEnum parse(int type) {
        for (BusinessTypeEnum Enum : BusinessTypeEnum.values()) {
            if (Enum.getType() == type) {
                return Enum;
            }
        }
        return NULL;
    }
}
