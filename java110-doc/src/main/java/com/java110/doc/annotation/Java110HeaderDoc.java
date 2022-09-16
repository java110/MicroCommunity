package com.java110.doc.annotation;

public @interface Java110HeaderDoc {

    String name();

    String defaultValue() default "";

    String description() default "";
}
