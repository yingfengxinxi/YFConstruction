package com.kakarote.crm.constant;

import com.kakarote.core.utils.UserUtil;

/**
 * @author zhangzhiwei
 * crm模块枚举
 */

public enum CrmEnum {
    /**
     * 线索
     */
    LEADS(1, "线索"),
    /**
     * 客户
     */
    CUSTOMER(2, "客户"),
    /**
     * 联系人
     */
    CONTACTS(3, "联系人"),
    /**
     * 产品
     */
    PRODUCT(4, "产品"),
    /**
     * 商机
     */
    BUSINESS(5, "商机"),
    /**
     * 合同
     */
    CONTRACT(6, "合同"),
    /**
     * 回款
     */
    RECEIVABLES(7, "回款"),
    /**
     * 回款计划
     */
    RECEIVABLES_PLAN(8, "回款计划"),
    /**
     * 公海
     */
    CUSTOMER_POOL(9, "公海"),
    /**
     * 市场活动
     */
    MARKETING(10, "市场活动"),
    /**
     * 回访
     */
    RETURN_VISIT(17, "回访跟踪"),
    /**
     * 发票
     */
    INVOICE(18, "发票"),

    /**
     * 费用
     */
    EXPENSE(50, "费用"),
    /**
     * 费用
     */
    EXPENSE_EXAMINE(51, "费用审批")
    ;

    CrmEnum(Integer type, String remarks) {
        this.type = type;
        this.remarks = remarks;
    }

    private Integer type;
    private String remarks;

    public String getRemarks() {
        return remarks;
    }

    public Integer getType() {
        return type;
    }

    public static CrmEnum parse(Integer type) {
        for (CrmEnum crmEnum : CrmEnum.values()) {
            if (crmEnum.getType().equals(type)) {
                return crmEnum;
            }
        }
        return null;
    }

    public static CrmEnum parse(String name) {
        for (CrmEnum crmEnum : CrmEnum.values()) {
            if (crmEnum.name().equals(name)) {
                return crmEnum;
            }
        }
        return null;
    }

    public String getIndex() {
        return "t_" + UserUtil.getUser().getTenantId() + "_wk_single_" + name().toLowerCase();
    }

    public String getTable() {
        return name().toLowerCase();
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
