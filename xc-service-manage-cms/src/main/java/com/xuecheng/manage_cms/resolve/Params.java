package com.xuecheng.manage_cms.resolve;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * project name : xuecheng
 * Date:2018/12/18
 * Author: yc.guo
 * DESC:
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Params {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    boolean required() default false;

    boolean notEmpty() default true;

    int maxLength() default Integer.MAX_VALUE;

    String defaultValue() default "";
    
}
