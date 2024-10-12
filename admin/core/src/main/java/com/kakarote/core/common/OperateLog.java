package com.kakarote.core.common;

import java.lang.annotation.*;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateLog {

    //操作详情
    String detail() default "";
    //模块
    LogTypes types() default LogTypes.OTHER;
    //
    LogBehavior behavior() default LogBehavior.OTHER;
}
