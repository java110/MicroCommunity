package com.java110.doc.annotation;

import com.java110.doc.registrar.Java110ApiDocDiscoveryRegistrar;
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
@Import(Java110ApiDocDiscoveryRegistrar.class)
public @interface Java110ApiDocDiscovery {

    String[] basePackages() default {};

    String[] value() default {};

    Class<?> apiDocClass();
}
