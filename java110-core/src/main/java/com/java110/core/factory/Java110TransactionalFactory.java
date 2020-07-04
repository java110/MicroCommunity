package com.java110.core.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Java110TransactionalFactory
 * @Description TODO
 * @Author wuxw
 * @Date 2020/7/3 22:30
 * @Version 1.0
 * add by wuxw 2020/7/3
 **/
public class Java110TransactionalFactory {

    //全局事务ID
    public static final String T_ID = "t-id";

    //当前服务ID
    public static final String SERVICE_ID = "service-id";

    private static ThreadLocal<Map<String, String>> threadLocal = new ThreadLocal<Map<String, String>>() {
        @Override
        protected Map<String, String> initialValue() {
            return new HashMap<String, String>();
        }
    };

    public static String put(String key, String value) {
        return threadLocal.get().put(key, value);
    }

    public static String get(String key) {
        return threadLocal.get().get(key);
    }

    public static String remove(String key) {
        return threadLocal.get().remove(key);
    }

    public static Map<String, String> entries() {
        return threadLocal.get();
    }

    public static String getOrCreateTId(){
        return "";
    }

}
