package com.java110.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * request param
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Java110ResponseDoc {


    Java110HeaderDoc[] headers() default @Java110HeaderDoc(name = "");

    Java110ParamDoc[] params() default @Java110ParamDoc(name = "");


}
