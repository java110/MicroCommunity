package com.java110.core.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 侦听注入
 * Created by wuxw on 2018/7/2.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(Java110ListenerDiscoveryRegistrar.class)
public @interface Java110ListenerDiscovery {

    String[] basePackages() default {};

    String[] value() default {};

    Class<?> listenerPublishClass();
}
