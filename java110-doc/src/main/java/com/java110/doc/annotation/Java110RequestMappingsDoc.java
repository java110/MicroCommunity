package com.java110.doc.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Java110RequestMappingsDoc {

    Java110RequestMappingDoc[] mappingsDocs() default @Java110RequestMappingDoc(name="",resource = "",url = "");
}
