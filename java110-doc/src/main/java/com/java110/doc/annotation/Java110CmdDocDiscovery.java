package com.java110.doc.annotation;

import com.java110.doc.registrar.Java110CmdDocDiscoveryRegistrar;
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
@Import(Java110CmdDocDiscoveryRegistrar.class)
public @interface Java110CmdDocDiscovery {

    String[] basePackages() default {};

    String[] value() default {};

    Class<?> cmdDocClass();
}
