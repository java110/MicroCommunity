package com.java110.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Java110CmdDoc {

    /**
     *  api title
     * @return
     */
    String title();

    String serviceCode();

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

    /**
     * http method
     * @return
     */
    String httpMethod() default "";

    /**
     * request url
     * @return
     */
    String url();

    /**
     * @Java110RequestMappingDoc resource
     * @return
     */
    String resource();

    String author() default "";

    int seq() default 999;


}
