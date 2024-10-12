package com.kakarote.core.workflow;


import java.lang.annotation.*;

/**
 * 审批注解
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WfAnnotation{
    /**
     * 是否校验审核状态，阻止操作提交
     * @return
     */
    boolean check() default true;

    /**
     * wfVO参数位置
     * @return
     */
    int index() default 0;
}
