package com.ruoyi.framework.aspectj.lang.annotation;

import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChangedAmount {
    /**
     * 模块
     */
    public int handleAccountType() default 0;
}
