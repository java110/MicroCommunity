package com.java110.core.annotation;


import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Java110Synchronized {

    @AliasFor("key")
    String value() default "";

    @AliasFor("value")
    String key() default "";
}
