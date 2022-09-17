package com.java110.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Java110RequestMappingDoc {

    String name();

    String resource() ;

    int seq() default 1;

    String url();

    String startWay() default "cloud";
}
