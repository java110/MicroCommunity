package com.java110.core.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created by wuxw on 2018/7/2.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Java110Listener {
    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";
}
