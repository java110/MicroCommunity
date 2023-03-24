package com.java110.doc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Java110ParamsDoc {

    Java110HeaderDoc[] headers() default {
            @Java110HeaderDoc(name="APP-ID",defaultValue = "通过dev账户分配应用",description = "应用APP-ID"),
            @Java110HeaderDoc(name="TRANSACTION-ID",defaultValue = "uuid",description = "交易流水号"),
            @Java110HeaderDoc(name="REQ-TIME",defaultValue = "20220917120915",description = "请求时间 YYYYMMDDhhmmss"),
            @Java110HeaderDoc(name="JAVA110-LANG",defaultValue = "zh-cn",description = "语言中文"),
            @Java110HeaderDoc(name="USER-ID",defaultValue = "-1",description = "调用用户ID 一般写-1"),
            @Java110HeaderDoc(name="Authorization",defaultValue = "Bearer xxx",description = "除了登录接口以外，其他接口必传token ,例如 Bearer token"),
    };

    Java110ParamDoc[] params() default @Java110ParamDoc(name = "");


}
