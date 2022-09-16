package com.java110.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * java110 api document annotation
 *
 * add by wuxw
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Java110ApiDoc {

    /**
     *  api title
     * @return
     */
    String title();

    /**
     * description api
     * @return
     */
    String description() default "";


    /**
     *  api  version
     * @return
     */
    String version() default "v1.0";



    String company() default "";

}
