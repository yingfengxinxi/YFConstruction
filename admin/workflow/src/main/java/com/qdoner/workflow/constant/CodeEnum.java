package com.qdoner.workflow.constant;

import com.kakarote.core.common.ResultCode;

/**
 * @author zhangzhiwei
 * crm响应错误代码枚举类
 */

public enum CodeEnum implements ResultCode {
    //客户模块管理
    NO_APPROVAL_STEP_CANNOT_BE_SAVED(2023, "没有审批步骤，无法保存"),
    CRM_EXAMINE_RECHECK_ERROR(2026, "该审批已撤回"),
    CRM_EXAMINE_AUTH_ERROR(2027, "当前用户没有审批权限"),
    CRM_EXAMINE_RECHECK_PASS_ERROR(2028, "该审核已通过，不能撤回"),

    ;


    CodeEnum(int code, String msg) {
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
